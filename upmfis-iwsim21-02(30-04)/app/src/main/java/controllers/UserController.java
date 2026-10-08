package controllers;

import com.google.gson.*;
import models.*;

import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.Set;

import utils.File;
import utils.results.Error;
import utils.results.Result;

// This class represents a controller for users in the system
// Follows the Singleton pattern
public class UserController {

    private static UserController instance = null;

    private Set<User> siters = new HashSet<>();
    private Set<Owner> owners = new HashSet<>();
    private User currentUser;

    private int maxId = 0;
    private final String OWNERS_PATH = "owners.json";
    private final String SITTERS_PATH = "sitters.json";

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    private UserController() {
        Error error = this.loadUsers();
        if (error != null) {
            System.out.println(error.getMessage());
        }
    }
    public User getCurrentUser() {
        return currentUser;
    }

    private void addUser(Owner owner) {
        owners.add(owner);
    }

    private void addUser(User user, String type) {
        this.addUser(UserFactory.createUser(user.getUsername(), user.getUsernameCode(), getNextId() ,type));
    }

    private int getNextId() {
        maxId++;
        return maxId;
    }

    public Result<String> register(String username, String usernameCode, String type) {
        User user = UserFactory.createUser(username, usernameCode, getNextId(),type);
        this.addUser(user, type);
        this.setCurrentUser(user);
        Error error = this.saveUsers();
        if (error != null) {
            return new Result<>(error);
        }
        return new Result<>(user.getUsername());
    }

    public String login(String usernameCode) {
        if (containsUser(usernameCode)) {
            User user = getUser(usernameCode);
            this.setCurrentUser(user);
            return user.getUsername();
        }
        return null;
    }

    private void setCurrentUser(User user) {
        this.currentUser = user;
    }

    //Metodo necesario para saber que tipo de usuario es el user, en este caso marcado para que sea owner
    public boolean containsUser(String usernameCode) {
        for (Owner owner : owners) {
            if (owner.getUsernameCode().equals(usernameCode)) {
                return true;
            }
        }
        for (User siter : siters) {
            if (siter.getUsernameCode().equals(usernameCode)) {
                return true;
            }
        }
        return false;
    }

    public User getUser(String usernameCode) {
        for (Owner owner : owners) {
            if (owner.getUsernameCode().equals(usernameCode)) {
                return owner;
            }
        }
        for (User siter : siters) {
            if (siter.getUsernameCode().equals(usernameCode)) {
                return siter;
            }
        }
        return null;
    }

    private void addUser(User siter) {
        siters.add(siter);
    }

    private Error saveUsers() {
        Gson gson = new Gson();
        String jsonOwners = gson.toJson(owners);
        String jsonSiters = gson.toJson(siters);
        File file = File.getInstance();
        Error error = file.write(OWNERS_PATH, jsonOwners);
        if (error != null) {
            return error;
        }
        return file.write(SITTERS_PATH, jsonSiters);
    }

    private Error loadUsers() {
        File file = File.getInstance();
        String jsonOwners = file.read(OWNERS_PATH).getData();
        String jsonSiters = file.read(SITTERS_PATH).getData();
        if (jsonOwners == null || jsonSiters == null) {
            return new Error("Error loading users");
        }

        JsonParser jsonParser = new JsonParser();

        JsonArray ownersJsonArray = jsonParser.parse(jsonOwners).getAsJsonArray();
        JsonArray sitersJsonArray = jsonParser.parse(jsonSiters).getAsJsonArray();


        int id;
        String username, usernameCode;
        for (JsonElement ownerElement : ownersJsonArray) {
            JsonObject ownerObject = ownerElement.getAsJsonObject();
            id = ownerObject.get("id").getAsInt();
            username = ownerObject.get("username").getAsString();
            usernameCode = ownerObject.get("usernameCode").getAsString();
            this.addUser(new Owner(username, usernameCode, id));
            if (id > maxId) {
                maxId = id;
            }
        }

        for (JsonElement siterElement : sitersJsonArray) {
            JsonObject siterObject = siterElement.getAsJsonObject();
            id = siterObject.get("id").getAsInt();
            username = siterObject.get("username").getAsString();
            usernameCode = siterObject.get("usernameCode").getAsString();
            this.addUser(new PetSitter(username, usernameCode, id));
            if (id > maxId) {
                maxId = id;
            }
        }
        return null;
    }

    public boolean loggedIn() {
        return this.currentUser != null;
    }
}

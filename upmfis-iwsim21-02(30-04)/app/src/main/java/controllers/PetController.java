package controllers;

import com.google.gson.*;
import models.*;
import utils.File;
import utils.results.Error;
import utils.results.Result;
import java.util.HashSet;
import java.util.Set;

// This class represents a controller for pets in the system
// Follows the Singleton pattern
public class PetController {

    private static PetController instance = null;
    Set<Pet> pets = new HashSet<Pet>();
    Set<ExoticPet> exoticPets = new HashSet<ExoticPet>();

    private int maxId = 0;

    private final String PETS_PATH = "pets.json";

    private final String EXOTIC_PATH = "exotics.json";


    private PetController() {
        Error error = this.loadPets();
        if (error != null) {
            System.out.println(error.getMessage());
        }
    }

    public static PetController getInstance() {
        if (instance == null) {
            instance = new PetController();
        }
        return instance;
    }

    private int getNextId() {
        maxId++;
        return maxId;
    }

    public Result<String> createPet(String name, String description, String address, String RIAC, String securePolicy, String legalProcedure, String healthCertificate) {
        Pet petToCreate = PetFactory.createPet(name, description, address, RIAC, securePolicy, getNextId(), legalProcedure, healthCertificate);
        petToCreate.setOwner((Owner) UserController.getInstance().getCurrentUser());
        //petToCreate.getOwner().getPets().add(petToCreate);
        addPet(petToCreate);
        Error error = this.savePets();
        if (error != null) {
            return new Result<>(error);
        }
        return new Result<>(petToCreate.toString());
    }

    public void defaultPets(){
        createPet("toby","golden retriever", "Monte Alicia Ocampo, 8 - The Hammocks, And / 02660", "3936142643", "98314", "", "");
        createPet("rayo","yorkshire", "Muelle Victoria Gaitán, 1 - Leesburg, Man / 00111", "4607761661", "71299", "", "");
        createPet("alfa","chow-chow", "Municipio Carlota 80 - Arlington, Bal / 26592", "5174828926", "43753", "9520853A", "83120FAR");
    }

    public Pet getPet(int id){
        Pet pet1=new Pet();
        ExoticPet exoticPet1=new ExoticPet();
        boolean exotic=false;
        for (Pet pet : pets) {
            if (pet.getId() == id) {
                pet1 = pet;
            } else for (ExoticPet exoticPet: exoticPets) {
                if (exoticPet1.getId() == id) {
                    exoticPet1 = exoticPet;
                    exotic=true;
                }
            }
        } if (exotic){
            return exoticPet1;
        }
            return pet1;
    }

    public Pet existPetOrExotic(int id){
        boolean existPet = existPet(id);
        boolean existExoticPet= existExoticPet(id);
        if (existPet || existExoticPet){
            return getPet(id);
        }
        else return null;
    }
    private boolean existPet(int id) {
        for (Pet pet : pets) {
            if (pet.getId() == id) {
                return true;
            }
        }
        return false;
    }
    private boolean existExoticPet(int id) {
        for (ExoticPet exoticPet : exoticPets) {
            if (exoticPet.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private int getNumberExoticPet(){
        return exoticPets.size();
    }
    private int getNumberPet(){
        return pets.size();
    }
    public int getTotalNumberPets(){
        return getNumberExoticPet()+getNumberPet();
    }

    public void getAllPets(){
        for (Pet pet : pets){
            System.out.println(pet.toString() + " Pet code: " + pet.getId());
        }
        for (ExoticPet exoticPet : exoticPets){
            System.out.println(exoticPet.toString() + " Pet code: " + exoticPet.getId());
        }
    }

    public void getOwnerPets(Owner owner){
        for (Pet pet : owner.getPets()){
            System.out.println(pet.toString() + " Pet code: " + pet.getId());
        }
    }
    private void addPet(Pet pet){
        if (pet.isExotic()) {
            exoticPets.add((ExoticPet) pet);
        }
        else pets.add(pet);
    }
    private Error savePets() {
        Gson gson = new Gson();
        String jsonPets = gson.toJson(pets);
        String jsonExotic = gson.toJson(exoticPets);
        File file = File.getInstance();
        Error error = file.write(PETS_PATH, jsonPets);
        if (error != null) {
            return error;
        }
        return file.write(EXOTIC_PATH, jsonExotic);
    }

    private Error loadPets() {
        File file = File.getInstance();
        String jsonPets = file.read(PETS_PATH).getData();
        String jsonExotics = file.read(EXOTIC_PATH).getData();
        if (jsonPets == null || jsonExotics == null) {
            return new Error("Error loading users");
        }

        JsonParser jsonParser = new JsonParser();

        JsonArray petsJsonArray = jsonParser.parse(jsonPets).getAsJsonArray();
        JsonArray exoticJsonArray = jsonParser.parse(jsonExotics).getAsJsonArray();


        int id;
        String name, description, address, RIAC, securePolicy;
        for (JsonElement petElement : petsJsonArray) {
            JsonObject petObject = petElement.getAsJsonObject();
            name = petObject.get("name").getAsString();
            description = petObject.get("description").getAsString();
            address=petObject.get("address").getAsString();
            RIAC=petObject.get("RIAC").getAsString();
            securePolicy= petObject.get("securePolicy").getAsString();
            id = petObject.get("id").getAsInt();
            this.addPet(new Pet(name,description, address, RIAC, securePolicy, id));
            if (id > maxId) {
                maxId = id;
            }
        }

        String legalProcedure;
        String healthCertificate;
        for (JsonElement exoticElement : exoticJsonArray) {
            JsonObject exoticObject = exoticElement.getAsJsonObject();
            name = exoticObject.get("name").getAsString();
            description = exoticObject.get("description").getAsString();
            address=exoticObject.get("address").getAsString();
            RIAC=exoticObject.get("RIAC").getAsString();
            securePolicy= exoticObject.get("securePolicy").getAsString();
            id = exoticObject.get("id").getAsInt();
            legalProcedure=exoticObject.get("legalProcedure").getAsString();
            healthCertificate=exoticObject.get("healthCertificate").getAsString();

            this.addPet(new ExoticPet(name,description, address, RIAC, securePolicy, id, legalProcedure, healthCertificate));
            if (id > maxId) {
                maxId = id;
            }
        }
        return null;
    }

}

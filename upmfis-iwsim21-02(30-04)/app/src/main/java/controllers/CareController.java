package controllers;

import com.google.gson.Gson;
import models.Care;
import utils.File;
import utils.results.Error;
import utils.results.Result;
import java.util.HashSet;
import java.util.Set;

// This class represents a controller for the pet care in the system
// Follows the Singleton pattern
public class CareController {

    private static CareController instance = null;

    private final PetController petController = PetController.getInstance();

    Set<Care> cares = new HashSet<>();
    private final String PATH = "users.json";

    private CareController() {
        Error error = this.loadCares();
        if (error != null) {
            System.out.println(error.getMessage());
        }
    }

    public static CareController getInstance() {
        if (instance == null) {
            instance = new CareController();
        }
        return instance;
    }

    private void addCare(Care care) {
        cares.add(care);
    }

    public Result<String> addCare(int hashCode, String description, float priceHour, String startHour, String finishHour){
        Care care = new Care(petController.getPet(hashCode),description, priceHour,  startHour,  finishHour);
        this.addCare(care);
        Error error = this.saveCares();
        if (error != null) {
            return new Result<>(error);
        }
        return new Result<>(care.getPet().getName());
    }

    private Error saveCares() {
        Gson gson = new Gson();
        String jsonCares = gson.toJson(cares);
        File file = File.getInstance();
        return file.write(PATH, jsonCares);
    }

    private Error loadCares() {
        File file = File.getInstance();
        Result<String> result = file.read(PATH);
        if (result.hasError()) {
            return result.getError();
        }
        String[] Cares = result.getData().split("\n");
        Gson gson = new Gson();
        for (String care : Cares) {
                Care cares = gson.fromJson(care, Care.class);
                this.addCare(cares);
        }
        return null;
    }
}

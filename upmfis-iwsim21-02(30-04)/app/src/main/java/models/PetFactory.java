package models;

import controllers.PetController;

 public class PetFactory {

    public static Pet createPet(String name, String description, String address, String RIAC, String securePolicy, int id, String legalProcedure, String healthCertificate) {
        if (legalProcedure.isEmpty() && healthCertificate.isEmpty()) {
            return new Pet(name, description, address, RIAC, securePolicy, id);
        } else {
            return new ExoticPet(name, description, address, RIAC, securePolicy, id, legalProcedure, healthCertificate);
        }
    }
}

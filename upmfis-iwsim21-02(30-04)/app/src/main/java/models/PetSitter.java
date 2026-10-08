package models;

import java.util.ArrayList;

public class PetSitter extends User{
    ArrayList<Pet> petsToSit;
    ArrayList<ExoticPet> exoticPetsToSit;

    public PetSitter(String username, String usernameCode, int id) {
        super(username, usernameCode, id);
        this.petsToSit = new ArrayList<>();
    }
    public boolean isOwner(){
        return false;
    }
}

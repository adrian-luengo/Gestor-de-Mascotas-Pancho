package models;

import java.util.ArrayList;

public class Owner extends User {

    ArrayList<Pet> pets;

    public Owner(String username, String usernameCode, int id) {
        super(username, usernameCode, id);
        this.pets = new ArrayList<>();
    }

    public ArrayList<Pet> getPets() {
        return pets;
    }

    public boolean isOwner(){
        return true;
    }
}

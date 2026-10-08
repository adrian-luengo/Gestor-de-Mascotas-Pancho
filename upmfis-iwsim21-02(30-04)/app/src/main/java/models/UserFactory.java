package models;

public class UserFactory {
    public static User createUser(String username, String usernameCode, int id,String type) {
        if (type.equals("owner")) {
            return new Owner(username, usernameCode, id);
        } else {
            return new PetSitter(username, usernameCode, id);
        }
    }
}

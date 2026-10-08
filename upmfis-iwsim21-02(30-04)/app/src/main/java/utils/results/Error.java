package utils.results;

// Basic error class
public class Error {
     private final String message;
     private String name;

    public Error(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}

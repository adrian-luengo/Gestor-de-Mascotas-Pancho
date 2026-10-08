package utils.results;

public class UnauthorizedError extends Error{

    private String name="Unauthorized";

    public UnauthorizedError(String message) {
        super(message);
    }

}

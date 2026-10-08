package utils.results;

// Result class to return data or error
public class Result <T>{
    private final T data;
    private final Error error;

    public Result(T data) {
        this.data = data;
        this.error = null;
    }

    public Result(Error error) {
        this.error = error;
        this.data = null;
    }

    public boolean hasError() {
        return error != null;
    }

    public T getData() {
        return data;
    }

    public Error getError() {
        return error;
    }
}

package views.dispatchers;

import utils.Console;
import utils.results.Error;

// Abstract class for commands
abstract public class Dispatcher {
    String name;
    String description;
    Console console = Console.getInstance();

    protected abstract Error checkSintax(String[] input);

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public abstract void execute(String[] arguments);

    void printError(Error error){
        this.console.printError(error.getMessage());
    }


}

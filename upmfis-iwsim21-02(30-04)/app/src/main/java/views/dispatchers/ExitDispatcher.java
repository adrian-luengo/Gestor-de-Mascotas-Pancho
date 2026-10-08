package views.dispatchers;

import views.CLI;
import utils.results.Error;
import utils.results.InvalidNumberOfArgumentsError;

public class ExitDispatcher extends Dispatcher {
    private final CLI cli;

    public ExitDispatcher(CLI cli){
        this.cli = cli;
        this.name = "exit";
        this.description = "Exit from application.";
    }

    protected Error checkSintax(String[] input) {
        if (input.length != 0){
            return new InvalidNumberOfArgumentsError();
        }
        return null;
    }

    public void execute(String[] arguments) {
        Error error = this.checkSintax(arguments);
        if (error == null){
            this.console.printConfirmation("Exiting...");
            this.cli.stop();
            return;
        }
        this.printError(error);

    }
}

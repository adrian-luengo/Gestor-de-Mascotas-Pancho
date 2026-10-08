package views.dispatchers;

import views.CLI;
import utils.results.Error;
import utils.results.InvalidNumberOfArgumentsError;

public class HelpDispatcher extends Dispatcher {
    private Dispatcher[] dispatchers;

    public HelpDispatcher(CLI cli){
        this.name = "help";
        this.description = "Shows the available commands";
        this.dispatchers = cli.getDispatchers();
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
            for (Dispatcher dispatcher : this.dispatchers) {
                this.console.printConfirmation(dispatcher.getName() + " - " + dispatcher.getDescription());
            }
            this.console.printConfirmation("help - Shows the available commands");
            return;
        }
        this.printError(error);
    }
}

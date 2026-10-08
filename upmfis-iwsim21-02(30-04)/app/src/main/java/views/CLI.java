package views;

import utils.Console;
import views.dispatchers.*;

// Command Line Interface
public class CLI {

    private final Dispatcher[] dispatchers = {
            new views.dispatchers.LoginDispatcher(),
            new views.dispatchers.AddPetDispatcher(),
            new views.dispatchers.ListPetsDispatcher(),
            new views.dispatchers.CareDispatcher(),
            new views.dispatchers.ExitDispatcher(this),
    };
    private final HelpDispatcher helpDispatcher = new HelpDispatcher(this);
    private boolean running = true;

    public Dispatcher[] getDispatchers() {
        return this.dispatchers;
    }

    public void run(){
        // TODO: Implement the CLI
        Console console = Console.getInstance();
        System.out.println("Welcome to PetSitter!");
        helpDispatcher.execute(new String[]{});
        while (this.running) {
            String input = console.read(">_ ");
            String[] parts = input.split(" ");
            String dispatcherName = parts[0];
            Dispatcher dispatcher = getDispatcher(dispatcherName);
            if (dispatcher != null){
                String[] arguments = new String[parts.length - 1];
                System.arraycopy(parts, 1, arguments, 0, parts.length - 1);
                dispatcher.execute(arguments);
            } else {
                console.printError("Command not found");
            }
        }
    }

    private Dispatcher getDispatcher(String dispatcherName) {
        if (dispatcherName.equals(this.helpDispatcher.getName())) {
            return this.helpDispatcher;
        }
        for (Dispatcher dispatcher : this.dispatchers) {
            if (dispatcher.getName().equals(dispatcherName)) {
                return dispatcher;
            }
        }
        return null;
    }

    public void stop() {
        this.running = false;
    }

}

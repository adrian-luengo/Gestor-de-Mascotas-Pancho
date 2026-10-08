package views.dispatchers;

import controllers.PetController;
import controllers.UserController;
import models.Owner;
import models.PetSitter;
import utils.results.Error;
import utils.results.InvalidNumberOfArgumentsError;
import utils.results.UnauthorizedError;

import java.security.InvalidParameterException;
import java.util.Objects;

public class ListPetsDispatcher extends Dispatcher{

    private final PetController petController = PetController.getInstance();

    private final UserController userController = UserController.getInstance();


    public ListPetsDispatcher() {
        this.name = "list-pets";
        this.description = "List the pets of this account";
    }

    protected Error checkSintax(String[] input) {
        if (input.length != 0) {
            return new InvalidNumberOfArgumentsError();
        }
        return null;
    }

    public void execute(String[] arguments) {
        Error error = this.checkSintax(arguments);
        if (error == null) {

            if (userController.getCurrentUser() == null) {
                printError(new UnauthorizedError("You must log in"));
                return;
            }
            if (UserController.getInstance().getCurrentUser().isOwner()) {
                this.console.printConfirmation("Your pets:");
                petController.getOwnerPets((Owner) userController.getCurrentUser());
            } else {
                this.console.printConfirmation("Pets you are sitting:");
            }
        }
    }

}

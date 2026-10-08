package views.dispatchers;

import controllers.PetController;
import controllers.UserController;
import utils.results.Error;
import utils.results.InvalidNumberOfArgumentsError;
import utils.results.UnauthorizedError;

import java.security.InvalidParameterException;
import java.util.Objects;


public class AddPetDispatcher extends Dispatcher {

    private final PetController petController = PetController.getInstance();

    private final UserController userController = UserController.getInstance();


    public AddPetDispatcher() {

        this.name = "add-pet";
        this.description = "Add new Pet to this account(it can be an exotic or common pet";
    }

    protected Error checkSintax(String[] input) {
        if (input.length != 0) {
            return new InvalidNumberOfArgumentsError();
        }
        return null;
    }

    public void execute(String[] arguments) {
        Error error = this.checkSintax(arguments);
        boolean create;
        if (error == null) {

            if (userController.getCurrentUser() == null) {
                printError(new UnauthorizedError("You must log in"));
                return;
            }
            if (!UserController.getInstance().getCurrentUser().isOwner()) {
                printError(new UnauthorizedError("You must be an Owner"));
                return;
            }

            this.console.print("If you want to use default examples write 1, if not just press enter(remind that this examples are correct):");
            String option=this.console.read(">_ ");
            if (Objects.equals(option, "1")){
               petController.defaultPets();
            }else {
                do {
                    this.console.print("Name:");
                    String name = this.console.read(">_ ");

                    this.console.print("Live in:");
                    String address = this.console.read(">_ ");

                    this.console.print("Description:");
                    String description = this.console.read(">_ ");

                    this.console.print("RIAC number:");
                    String RIAC = this.console.read(">_ ");

                    this.console.print("If the pet have a secure policy:");
                    String securePolicy = this.console.read(">_ ");

                    this.console.print("Code of the legal procedure documentation(if not exotic, leave it blank):");
                    String legalProdecudre = this.console.read(">_ ");

                    this.console.print("Code of the health certificate(if not exotic, leave it blank):");
                    String healthCertificate = this.console.read(">_ ");

                    try {
                        petController.createPet(name, description, address, RIAC, securePolicy, legalProdecudre, healthCertificate);
                        create = false;
                    } catch (InvalidParameterException invalidParameterException) {
                        printError(new Error("You must write a name, adress and a valid RIAC number"));
                        create = true;
                    }
                } while (create);
            }
        }
    }
}

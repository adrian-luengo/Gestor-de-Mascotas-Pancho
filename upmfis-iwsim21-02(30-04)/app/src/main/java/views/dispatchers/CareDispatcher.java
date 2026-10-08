package views.dispatchers;

import controllers.CareController;
import controllers.PetController;
import controllers.UserController;
import utils.results.Error;
import utils.results.InvalidNumberOfArgumentsError;
import utils.results.Result;
import utils.results.UnauthorizedError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CareDispatcher extends Dispatcher{

    boolean validInput = false;

    private final String[] Dates = new String[2];
    public CareDispatcher(){
        this.name = "care";
        this.description = "Let owners create a pet care petition for their pets.";
    }
    protected Error checkSintax(String[] input) {
        if (input.length != 0){
            return new InvalidNumberOfArgumentsError();
        }
        return null;
    }

    private float checkPrice() {
        float value = 0.0f;

        while (!validInput) {
            this.console.printConfirmation("Add price per hour.");
            String input = this.console.read(">_ ");

            try {
                value = Float.parseFloat(input);
                validInput = true;
            } catch (NumberFormatException e) {
                this.console.printError("Wrong input.");
            }
        }

        return value;
    }

    public void checkDate() {
        String startDateString;
        String endDateString;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        while (true) {
            try {
                this.console.printConfirmation("Add starting hour.(dd/MM/yyyy HH:mm:ss)");
                startDateString = console.read(">_ ");
                dateFormat.parse(startDateString);
                break;
            } catch (ParseException e) {
                this.console.printError("Wrong format.");
            }
        }

        while (true) {
            try {
                this.console.printConfirmation("Add finishing hour.(dd/MM/yyyy HH:mm:ss)");
                endDateString = console.read(">_ ");
                dateFormat.parse(endDateString);

                Date startDate = dateFormat.parse(startDateString);
                Date endDate = dateFormat.parse(endDateString);

                if (endDate.before(startDate)) {
                    this.console.printError("Finishing hour must be after starting hour.");
                    continue;
                }
                break;
            } catch (ParseException e) {
                this.console.printError("Wrong format.");
            }
        }
        Dates[0] = startDateString;
        Dates[1] = endDateString;
    }


    public void execute(String[] arguments) {
        Error error = this.checkSintax(arguments);
        if (error == null) {

            if (UserController.getInstance().getCurrentUser() == null) {
                printError(new UnauthorizedError("You must log in."));
                return;
            }
            if (!UserController.getInstance().getCurrentUser().isOwner()) {
                printError(new UnauthorizedError("You must be an Owner."));
                return;
            }
            while (!validInput) {
                this.console.printConfirmation("Select pet for care petition: (1" + " - " + PetController.getInstance().getTotalNumberPets() + ")");
                PetController.getInstance().getAllPets();
                String input = console.read(">_ ");
                try {
                    if (PetController.getInstance().existPetOrExotic(Integer.parseInt(input)) != null) {
                        requestCare(Integer.parseInt(input));
                        validInput = true;
                    } else {
                        System.out.println("The pet requested is not in the database.");
                    }
                    return;
                } catch (NumberFormatException e) {
                    this.console.printError("Wrong input.");
                }
            }
        }
    }
    private void requestCare(int hashCode){
        String description;
        float priceHour;
        String startHour;
        String finishHour;
        this.console.printConfirmation("Add description.");
        description = console.read(">_ ");
        priceHour = checkPrice();
        checkDate();
        startHour = Dates[0];
        finishHour = Dates[1];
        Result<String> result = CareController.getInstance().addCare(hashCode,description,priceHour,startHour,finishHour);
        if (result.hasError()) {
            console.printError(result.getError().getMessage());
        } else {
            String petName = result.getData();
            console.printConfirmation("Care for " + petName + " was successfully created.");
        }
    }
}
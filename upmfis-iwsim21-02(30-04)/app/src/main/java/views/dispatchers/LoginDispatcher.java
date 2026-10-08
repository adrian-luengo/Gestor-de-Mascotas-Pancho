package views.dispatchers;

import controllers.UserController;
import servidor.ExternalRRSS;
import utils.results.Error;
import utils.results.Result;

public class LoginDispatcher extends Dispatcher{

    public LoginDispatcher(){
        this.name = "login";
        this.description = "Login in the RRSS";
    }

    protected Error checkSintax(String[] input) {
        return null;
    }

    public void execute(String[] arguments) {
        UserController userController = UserController.getInstance();
        if (userController.loggedIn()){
            this.console.printError("You are already logged in");
        } else {
            String usernameCode = ExternalRRSS.LoginRRSS();
            if (usernameCode != null) {
                if (userController.containsUser(usernameCode)) {
                    String username = userController.login(usernameCode);
                    console.printConfirmation("Welcome back " + username + "!");
                } else {
                    System.out.println("Welcome new user");
                    String type = console.read("Owner or sitter: ");
                    String username = console.read("Enter your username: ");
                    Result<String> result = userController.register(username, usernameCode, type);
                    if (result.hasError()) {
                        console.printError(result.getError().getMessage());
                    } else {
                        String user = result.getData();
                        console.printConfirmation("Welcome " + user + "!");
                    }
                }
            } else {
                System.out.println("Login failed");
            }
        }
    }
}

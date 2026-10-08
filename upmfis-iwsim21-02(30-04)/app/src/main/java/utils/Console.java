package utils;

import java.util.Scanner;

// Console class is a Singleton class that provides methods to print messages and read input from the console
public class Console {
    private static Console instance;

    private Scanner scanner;
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_BLUE = "\u001B[34m";

    public Console() {
        this.scanner = new Scanner(System.in);
    }
    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        this.print(message + "\n");
    }

    public void printConfirmation(String message) {
        System.out.println(this.ANSI_BLUE + "-- " + message + this.ANSI_RESET);
    }

    public void printError(String message) {
        System.out.println(this.ANSI_RED + "-- " + message + this.ANSI_RESET);
    }

    public String read() {
        return scanner.nextLine();
    }

    public String readPassword() {
        return new String(System.console().readPassword());
    }

    public String read(String message) {
        print(message);
        return read();
    }

    public String readPassword(String message) {
        print(message);
        return readPassword();
    }

    public void flush () {
        System.out.flush();
    }

    // Singleton pattern
    public static Console getInstance() {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }

}

package com.sparta.data.views;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class DataHandlerView {
    private static Scanner scan = new Scanner(System.in);
    public static void printWelcomeBanner() {
        System.out.println("+----------- WELCOME -----------+");
        System.out.println("| Thanks for using my data app  |");
        System.out.println("|                               |");
        System.out.println("|   -> Pick a CSV file to <-    |");
        System.out.println("|   ->     work with.     <-    |");
        System.out.println("+-------------------------------+");
    }

    public static String getInput(LinkedHashMap<String, String> acceptableChoices, String message) {
        String choice = "";
        boolean valid = false;
        while (!valid) {
            System.out.println(buildChoicesString(acceptableChoices, message));
            choice = scan.next();
            if (acceptableChoices.containsKey(choice.toLowerCase()))
                valid = true;
        }
        if (choice.equals("x")) {
            System.out.println("Program exiting - Thanks for sorting.");
            System.exit(0);
        }
        return choice;
    }

    private static String buildChoicesString(LinkedHashMap<String, String> acceptableChoices, String choiceType) {
        StringBuilder myString = new StringBuilder();
        myString.append("Please select ").append(choiceType).append(":\s");
        for (String choice : acceptableChoices.keySet()) {
            myString.append("\n -> ").append("'").append(choice).append("'").append(" for ")
                    .append(acceptableChoices.get(choice)).append("\s");
        }
        return myString.toString();
    }
}

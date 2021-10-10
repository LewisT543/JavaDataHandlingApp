package com.sparta.data.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class DataHandlerView {
    private static Scanner scan = new Scanner(System.in);
    public static void printWelcomeBanner() {
        System.out.println("+----------- WELCOME -----------+");
        System.out.println("|   Thanks for using my data    |");
        System.out.println("|         migration app.        |");
        System.out.println("|                               |");
        System.out.println("|   -> Pick a CSV file to <-    |");
        System.out.println("|   ->     work with.     <-    |");
        System.out.println("|                               |");
        System.out.println("|   -> Pick a database to <-    |");
        System.out.println("|   ->  save records to.  <-    |");
        System.out.println("|                               |");
        System.out.println("|   ->   Choose a route.  <-    |");
        System.out.println("|   -> Object oriented or <-    |");
        System.out.println("|   ->     functional.    <-    |");
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

    public static int getIntegerInput(int lowerBound, int upperBound, String message) {
        int choice = 0;
        while (choice < lowerBound || choice > upperBound) {
            System.out.println("Please enter " + message);
            while (!scan.hasNextInt()) {
                scan.nextLine();
                System.out.println("Not a valid number, please try again: ");
                scan.nextLine();
            }
            choice = scan.nextInt();
        }
        return choice;
    }

    public static void displayInitialisationResults(long timeTaken) {
        System.out.println("------ Database Initialisation results ------");
        System.out.println("Time taken to initialise database: " + timeTaken + " ms\n");
    }

    public static void displayQueryResults(String[] stats) {
        System.out.println("------ Displaying Read results ------");
        System.out.println("Size of valid objects array: " + stats[0]);
        System.out.println("Size of rejects: " + stats[1]);
        System.out.println("Invalid entries written to 'Rejects.csv'");
        System.out.println("Total time taken to read: " + stats[2] + " ms");
        System.out.println("Total time taken to create objects: " + stats[3] + " ms\n");
    }

    public static void displayInsertResults(String[] stats) {
        System.out.println("------ Displaying Insertion results ------");
        System.out.println("Number of rows written: " + stats[0]);
        System.out.println("Total time taken to insert: " + stats[1] + " ms\n");
    }

    public static void displayQueryResults(ResultSet rs) {
        System.out.println("------ Displaying Query result ------");
            try {
                while (rs.next()) {
                    System.out.print("ID: " + rs.getInt("id"));
                    System.out.print(", Emp_number: " + rs.getInt("employee_number"));
                    System.out.print(", Prefix_ID: " + rs.getInt("prefix_id"));
                    System.out.print(", First_Name: " + rs.getString("f_name"));
                    System.out.print(", Initial: " + rs.getString("mid_initial"));
                    System.out.print(", Last_Name: " + rs.getString("l_name"));
                    System.out.print(", Gender_ID: " + rs.getInt("gender_id"));
                    System.out.print(", Email: " + rs.getString("email"));
                    System.out.print(", DOB: " + rs.getDate("date_of_birth"));
                    System.out.print(", Joined_On: " + rs.getDate("date_of_joining"));
                    System.out.print(", Salary: " + rs.getInt("salary"));
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
    }
}

package com.sparta.data.models.utils;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator {
    // Using EagerInitialisedSingleton as we will always need this class. But only 1 of them.
    private static final DataValidator instance = new DataValidator();
    private static final Pattern NAME_PREFIX_PATTERN = Pattern.compile("[a-zA-Z]+\\.");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Z]'?[- a-zA-Z]( [a-zA-Z])*$");
    private static final Pattern MIDDLE_INITIAL_PATTERN = Pattern.compile("[a-zA-Z\\.]+");
    private static final Pattern GENDER_PATTERN = Pattern.compile("[FMfm]");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

    private DataValidator() {    }

    public static DataValidator getInstance() { return instance; };

    public static boolean isValidEmployeeData(String[] data) {
        boolean validData = true;
        try {
            int empId = Integer.parseInt(data[0]);
            if (empId < 0) validData = false;

            String namePrefix = data[1];
            Matcher namePrefixMatcher = NAME_PREFIX_PATTERN.matcher(namePrefix);
            if (namePrefix.length() > 5 || !namePrefixMatcher.find()) validData = false;

            String firstName = data[2];
            Matcher nameMatcher = NAME_PATTERN.matcher(firstName);
            if (firstName.length() > 255 || !nameMatcher.find()) validData = false;

            String middleInitial = data[3];
            Matcher middleInitialMatcher = MIDDLE_INITIAL_PATTERN.matcher(middleInitial);
            if (middleInitial.length() > 5 || !middleInitialMatcher.find()) validData = false;

            String lastName = data[4];
            if (lastName.length() > 256 || !nameMatcher.find()) validData = false;

            String gender = data[5];
            Matcher genderMatcher = GENDER_PATTERN.matcher(gender);
            if (genderMatcher.find()) validData = false;

            String email = data[6];
            Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
            if (emailMatcher.find()) validData = false;

            Date dateOfBirth = Date.valueOf(data[7]);
            Date dateOfJoining = Date.valueOf(data[8]);

            int salary  = Integer.parseInt(data[9]);
            if (salary < 0) validData = false;
        } catch (IllegalArgumentException e) {
            validData = false;
            System.err.println("Invalid input");
        }
        if (!validData) System.out.println("Invalid Data.");
        return validData;
    }
}

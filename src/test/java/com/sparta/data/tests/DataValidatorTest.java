package com.sparta.data.tests;

import com.sparta.data.models.utils.DataValidator;
import org.junit.jupiter.api.Test;

public class DataValidatorTest {
    @Test
    public void isValidDataTest() {
        String[] myData = {"1","Mrs.","Lavon","A","Shufelt","F",
                "lavon.shufelt@aol.com","12/19/1977","7/23/2000","184597"};
        DataValidator.isValidEmployeeData(myData);
    }
}

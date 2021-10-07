package com.sparta.data.models;

import java.sql.Date;

public class Employee {
    private int empNumber;
    private String namePrefix;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String gender;
    private String email;
    private Date dateOfBirth;
    private Date dateOfJoining;
    private int salary;

    public Employee(int empNumber, String namePrefix, String firstName, String middleInitial, String lastName,
                    String gender, String email, Date dateOfBirth, Date dateOfJoining, int salary) {
        this.empNumber = empNumber;
        this.namePrefix = namePrefix;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoining = dateOfJoining;
        this.salary = salary;
    }

    public int getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(int empNumber) {
        this.empNumber = empNumber;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return empNumber + "," + namePrefix + "," + firstName + "," + middleInitial + "," + lastName + "," + gender + ","
                + email + "," + dateOfBirth + "," + dateOfJoining + "," + salary;
    }
}

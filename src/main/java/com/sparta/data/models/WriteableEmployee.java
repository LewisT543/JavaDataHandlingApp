package com.sparta.data.models;

import java.sql.Date;

public class WriteableEmployee {
    private Integer newId = null;
    private Integer empId;
    private Integer prefixId;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private Integer genderId;
    private String email;
    private Date dateOfBirth;
    private Date dateOfJoining;
    private int salary;

    public WriteableEmployee(Integer newId, Integer empId, Integer prefixId, String firstName,
                             String middleInitial, String lastName, Integer genderId, String email,
                             Date dateOfBirth, Date dateOfJoining, int salary) {
        this.newId = newId;
        this.empId = empId;
        this.prefixId = prefixId;
        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.genderId = genderId;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoining = dateOfJoining;
        this.salary = salary;
    }

    public WriteableEmployee(Employee employee, int i) {
        this.newId = i;
        this.empId = employee.getEmpID();
        switch (employee.getNamePrefix()) {
            case "Mr."   -> this.prefixId = 1;
            case "Mrs."  -> this.prefixId = 2;
            case "Ms."   -> this.prefixId = 3;
            case "Dr."   -> this.prefixId = 4;
            case "Drs."  -> this.prefixId = 5;
            case "Hon."  -> this.prefixId = 6;
            case "Prof." -> this.prefixId = 7;
        }
        this.firstName = employee.getFirstName();
        this.middleInitial = employee.getMiddleInitial();
        this.lastName = employee.getLastName();
        if (employee.getGender().equals("M"))
            this.genderId = 1;
        else
            this.genderId = 2;
        this.email = employee.getEmail();
        this.dateOfBirth = employee.getDateOfBirth();
        this.dateOfJoining = employee.getDateOfJoining();
        this.salary = employee.getSalary();
    }

    public Integer getNewId() { return newId; }

    public void setNewId(Integer newId) { this.newId = newId; }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public Integer getPrefixId() {
        return prefixId;
    }

    public void setPrefixId(Integer prefixId) {
        this.prefixId = prefixId;
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

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
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
        return empId + "," + prefixId + "," + firstName + "," + middleInitial + "," + lastName + "," + genderId + ","
                + email + "," + dateOfBirth + "," + dateOfJoining + "," + salary;
    }
}

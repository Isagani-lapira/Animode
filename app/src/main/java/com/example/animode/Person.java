package com.example.animode;

public class Person {

    private String FIRST_NAME;
    private String LAST_NAME;
    private String EMAIL;

    public Person(){}
    public Person(String first_name, String last_name, String email) {
        FIRST_NAME = first_name;
        LAST_NAME = last_name;
        EMAIL = email;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }
}

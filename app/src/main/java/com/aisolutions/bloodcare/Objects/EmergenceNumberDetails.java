package com.aisolutions.bloodcare.Objects;/*
Created by Akila Ishan on 2021-06-19.
*/

public class EmergenceNumberDetails {

    String name;
    String number;
    int priority;

    public EmergenceNumberDetails() {
    }

    public EmergenceNumberDetails(String name, String number, int priority) {
        this.name = name;
        this.number = number;
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

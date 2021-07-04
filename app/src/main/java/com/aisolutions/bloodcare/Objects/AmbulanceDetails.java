package com.aisolutions.bloodcare.Objects;/*
Created by Akila Ishan on 2021-06-19.
*/

public class AmbulanceDetails {

    String name;
    String number;
    String location;
    int priority;

    public AmbulanceDetails() {
    }

    public AmbulanceDetails(String name, String number, String location, int priority) {
        this.name = name;
        this.number = number;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

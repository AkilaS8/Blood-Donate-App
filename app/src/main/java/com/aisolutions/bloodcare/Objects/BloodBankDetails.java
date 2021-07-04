package com.aisolutions.bloodcare.Objects;/*
Created by Akila Ishan on 2021-06-19.
*/

public class BloodBankDetails {

    String name;
    String number;
    String location;
    String address;
    int priority;

    public BloodBankDetails() {
    }

    public BloodBankDetails(String name, String number, String location, String address, int priority) {
        this.name = name;
        this.number = number;
        this.location = location;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

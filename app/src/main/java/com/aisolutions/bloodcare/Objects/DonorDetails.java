package com.aisolutions.bloodcare.Objects;/*
Created by Akila Ishan on 2021-05-20.
*/

public class DonorDetails {

    private String donorName;
    private String donorTelephone;
    private String donorProvince;
    private String donorDistrict;
    private String donorCity;
    private String donorAddress;
    private String donorAge;
    private String donorWeight;
    private String donorGender;
    private String donorBloodGroup;
    private String donorHealthConditions;
    private Double donorLat;
    private Double donorLan;

    public DonorDetails() {
    }

    public DonorDetails(String donorName, String donorTelephone, String donorProvince, String donorDistrict, String donorCity, String donorAddress, String donorAge, String donorWeight, String donorGender, String donorBloodGroup, String donorHealthConditions, Double donorLat, Double donorLan) {
        this.donorName = donorName;
        this.donorTelephone = donorTelephone;
        this.donorProvince = donorProvince;
        this.donorDistrict = donorDistrict;
        this.donorCity = donorCity;
        this.donorAddress = donorAddress;
        this.donorAge = donorAge;
        this.donorWeight = donorWeight;
        this.donorGender = donorGender;
        this.donorBloodGroup = donorBloodGroup;
        this.donorHealthConditions = donorHealthConditions;
        this.donorLat = donorLat;
        this.donorLan = donorLan;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorTelephone() {
        return donorTelephone;
    }

    public void setDonorTelephone(String donorTelephone) {
        this.donorTelephone = donorTelephone;
    }

    public String getDonorProvince() {
        return donorProvince;
    }

    public void setDonorProvince(String donorProvince) {
        this.donorProvince = donorProvince;
    }

    public String getDonorDistrict() {
        return donorDistrict;
    }

    public void setDonorDistrict(String donorDistrict) {
        this.donorDistrict = donorDistrict;
    }

    public String getDonorCity() {
        return donorCity;
    }

    public void setDonorCity(String donorCity) {
        this.donorCity = donorCity;
    }

    public String getDonorAddress() {
        return donorAddress;
    }

    public void setDonorAddress(String donorAddress) {
        this.donorAddress = donorAddress;
    }

    public String getDonorAge() {
        return donorAge;
    }

    public void setDonorAge(String donorAge) {
        this.donorAge = donorAge;
    }

    public String getDonorWeight() {
        return donorWeight;
    }

    public void setDonorWeight(String donorWeight) {
        this.donorWeight = donorWeight;
    }

    public String getDonorGender() {
        return donorGender;
    }

    public void setDonorGender(String donorGender) {
        this.donorGender = donorGender;
    }

    public String getDonorBloodGroup() {
        return donorBloodGroup;
    }

    public void setDonorBloodGroup(String donorBloodGroup) {
        this.donorBloodGroup = donorBloodGroup;
    }

    public String getDonorHealthConditions() {
        return donorHealthConditions;
    }

    public void setDonorHealthConditions(String donorHealthConditions) {
        this.donorHealthConditions = donorHealthConditions;
    }

    public Double getDonorLat() {
        return donorLat;
    }

    public void setDonorLat(Double donorLat) {
        this.donorLat = donorLat;
    }

    public Double getDonorLan() {
        return donorLan;
    }

    public void setDonorLan(Double donorLan) {
        this.donorLan = donorLan;
    }
}

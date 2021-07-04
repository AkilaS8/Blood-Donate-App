package com.aisolutions.bloodcare.Objects;/*
Created by Akila Ishan on 2021-05-15.
*/

public class UserDetails {
    private String userName;
    private String userTelephone;
    private String userProvince;
    private String userDistrict;
    private String userCity;
    private String userAddress;
    private String userPassword;

    public UserDetails(String userName, String userTelephone, String userProvince, String userDistrict, String userCity, String userAddress, String userPassword) {
        this.userName = userName;
        this.userTelephone = userTelephone;
        this.userProvince = userProvince;
        this.userDistrict = userDistrict;
        this.userCity = userCity;
        this.userAddress = userAddress;
        this.userPassword = userPassword;
    }

    public UserDetails() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public String getUserProvince() {
        return userProvince;
    }

    public void setUserProvince(String userProvince) {
        this.userProvince = userProvince;
    }

    public String getUserDistrict() {
        return userDistrict;
    }

    public void setUserDistrict(String userDistrict) {
        this.userDistrict = userDistrict;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}

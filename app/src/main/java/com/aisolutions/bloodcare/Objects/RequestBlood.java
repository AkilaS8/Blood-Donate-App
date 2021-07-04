package com.aisolutions.bloodcare.Objects;/*
Created by Akila Ishan on 2021-05-22.
*/

public class RequestBlood {

    private String  requestID = "";
    private String requestName = "";
    private String requestTelephone = "";
    private String requestDistrict = "";
    private String requestForWhom = "";
    private String requestBloodGroup = "";
    private String requestDate = "";

    public RequestBlood() {
    }

    public RequestBlood(String requestID, String requestName, String requestTelephone, String requestDistrict, String requestForWhom, String requestBloodGroup, String requestDate) {
        this.requestID = requestID;
        this.requestName = requestName;
        this.requestTelephone = requestTelephone;
        this.requestDistrict = requestDistrict;
        this.requestForWhom = requestForWhom;
        this.requestBloodGroup = requestBloodGroup;
        this.requestDate = requestDate;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getRequestTelephone() {
        return requestTelephone;
    }

    public void setRequestTelephone(String requestTelephone) {
        this.requestTelephone = requestTelephone;
    }

    public String getRequestDistrict() {
        return requestDistrict;
    }

    public void setRequestDistrict(String requestDistrict) {
        this.requestDistrict = requestDistrict;
    }

    public String getRequestForWhom() {
        return requestForWhom;
    }

    public void setRequestForWhom(String requestForWhom) {
        this.requestForWhom = requestForWhom;
    }

    public String getRequestBloodGroup() {
        return requestBloodGroup;
    }

    public void setRequestBloodGroup(String requestBloodGroup) {
        this.requestBloodGroup = requestBloodGroup;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}

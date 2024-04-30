package com.example.weatherapp.XmlParser;


/*
Name: Sumaiya Juma
ID: s2110905
*/

public class Data {
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getLongT() {
        return longT;
    }

    public void setLongT(double longT) {
        this.longT = longT;
    }

    public double getLatT() {
        return latT;
    }

    public void setLatT(double latT) {
        this.latT = latT;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private String condition;
    private double longT;
    private double latT;
    private String locationName;



}

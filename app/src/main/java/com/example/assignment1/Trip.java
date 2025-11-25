package com.example.assignment1;

public class Trip {
    private String id;
    private String destination;
    private String startDate;
    private String endDate;
    private String tripType;
    private boolean isPacked;
    private boolean hasInsurance;

    public Trip(String id, String destination, String startDate, String endDate,
                String tripType, boolean isPacked, boolean hasInsurance) {
        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripType = tripType;
        this.isPacked = isPacked;
        this.hasInsurance = hasInsurance;
    }

    public String getId() {
        return id;
    }

    public String getDestination() {
        return destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTripType() {
        return tripType;
    }

    public boolean isPacked() {
        return isPacked;
    }

    public boolean hasInsurance() {
        return hasInsurance;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public void setPacked(boolean packed) {
        isPacked = packed;
    }

    public void setHasInsurance(boolean hasInsurance) {
        this.hasInsurance = hasInsurance;
    }
}
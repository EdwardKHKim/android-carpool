package com.example.android_carpool;

public class Ticket {

    public String Origin, Destination, Cost, PhoneNumber;

    public Ticket() {

    }

    public Ticket(String origin, String destination, String cost, String phoneNumber) {
        this.Origin = origin;
        this.Destination = destination;
        this.Cost = cost;
        this.PhoneNumber = phoneNumber;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}

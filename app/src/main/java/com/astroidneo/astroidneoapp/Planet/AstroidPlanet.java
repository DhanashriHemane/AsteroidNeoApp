package com.astroidneo.astroidneoapp.Planet;

public class AstroidPlanet {

    String Id;
    String Magnitude;
    String Velocity;
    String Distance;
    String Date;
    String absolute_magnitude_h;

    public AstroidPlanet(String id, String magnitude, String velocity, String distance, String date, String absolute_magnitude_h) {
        Id = id;
        Magnitude = magnitude;
        Velocity = velocity;
        Distance = distance;
        Date = date;
        this.absolute_magnitude_h = absolute_magnitude_h;
    }

    public String getAbsolute_magnitude_h() {
        return absolute_magnitude_h;
    }

    public void setAbsolute_magnitude_h(String absolute_magnitude_h) {
        this.absolute_magnitude_h = absolute_magnitude_h;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMagnitude() {
        return Magnitude;
    }

    public void setMagnitude(String magnitude) {
        Magnitude = magnitude;
    }

    public String getVelocity() {
        return Velocity;
    }

    public void setVelocity(String velocity) {
        Velocity = velocity;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

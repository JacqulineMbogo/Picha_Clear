package com.example.picha_clear.Bookings;

public class location_model {

    String location_id, location_name, location_price;

    public location_model(String location_id, String location_name, String location_price) {
        this.location_id = location_id;
        this.location_name = location_name;
        this.location_price = location_price;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLocation_price() {
        return location_price;
    }

    public void setLocation_price(String location_price) {
        this.location_price = location_price;
    }
}

package com.example.picha_clear.Bookings;

public class types_model {

    String type_id, type_name, type_cost;

    public types_model(String type_id, String type_name, String type_cost) {
        this.type_id = type_id;
        this.type_name = type_name;
        this.type_cost = type_cost;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_cost() {
        return type_cost;
    }

    public void setType_cost(String type_cost) {
        this.type_cost = type_cost;
    }
}

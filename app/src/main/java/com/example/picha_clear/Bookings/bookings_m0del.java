package com.example.picha_clear.Bookings;

public class bookings_m0del {

    private String  booking_id,booking_type,booking_date_created,booking_date_set,booking_duration,booking_status;

    public bookings_m0del(String booking_id, String booking_type, String booking_date_created, String booking_date_set, String booking_duration, String booking_status) {
        this.booking_id = booking_id;
        this.booking_type = booking_type;
        this.booking_date_created = booking_date_created;
        this.booking_date_set = booking_date_set;
        this.booking_duration = booking_duration;
        this.booking_status = booking_status;
    }


    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getBooking_date_created() {
        return booking_date_created;
    }

    public void setBooking_date_created(String booking_date_created) {
        this.booking_date_created = booking_date_created;
    }

    public String getBooking_date_set() {
        return booking_date_set;
    }

    public void setBooking_date_set(String booking_date_set) {
        this.booking_date_set = booking_date_set;
    }

    public String getBooking_duration() {
        return booking_duration;
    }

    public void setBooking_duration(String booking_duration) {
        this.booking_duration = booking_duration;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }
}

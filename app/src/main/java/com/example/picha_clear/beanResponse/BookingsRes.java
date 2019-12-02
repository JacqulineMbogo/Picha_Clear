package com.example.picha_clear.beanResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingsRes {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("Information")
    @Expose
    private Information information;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }

    public class Information {

        @SerializedName("booking_details")
        @Expose
        private List<BookingDetail> bookingDetails = null;

        public List<BookingDetail> getBookingDetails() {
            return bookingDetails;
        }

        public void setBookingDetails(List<BookingDetail> bookingDetails) {
            this.bookingDetails = bookingDetails;
        }

    }


    public class BookingDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("date_created")
        @Expose
        private String dateCreated;
        @SerializedName("date_set")
        @Expose
        private String dateSet;
        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("total_price")
        @Expose
        private String totalPrice;
        @SerializedName("location")
        @Expose
        private String location;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getDateSet() {
            return dateSet;
        }

        public void setDateSet(String dateSet) {
            this.dateSet = dateSet;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }
        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

    }

}
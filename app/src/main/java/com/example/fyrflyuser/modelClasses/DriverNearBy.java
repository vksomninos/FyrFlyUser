package com.example.fyrflyuser.modelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DriverNearBy {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private List<Detail> details;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
    public class Detail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("online_status")
        @Expose
        private String onlineStatus;
        @SerializedName("drivername")
        @Expose
        private String drivername;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("cartype")
        @Expose
        private String cartype;
        @SerializedName("brand_name")
        @Expose
        private String brandName;
        @SerializedName("vehicle_model")
        @Expose
        private String vehicleModel;
        @SerializedName("vehicle_registration_number")
        @Expose
        private String vehicleRegistrationNumber;
        @SerializedName("vehicle_or_driver_info")
        @Expose
        private String vehicleOrDriverInfo;
        @SerializedName("driver_license")
        @Expose
        private String driverLicense;
        @SerializedName("vehicle_registration")
        @Expose
        private String vehicleRegistration;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("distance_in_meters")
        @Expose
        private String distanceInMeters;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(String onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public String getDrivername() {
            return drivername;
        }

        public void setDrivername(String drivername) {
            this.drivername = drivername;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCartype() {
            return cartype;
        }

        public void setCartype(String cartype) {
            this.cartype = cartype;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getVehicleModel() {
            return vehicleModel;
        }

        public void setVehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
        }

        public String getVehicleRegistrationNumber() {
            return vehicleRegistrationNumber;
        }

        public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
            this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        }

        public String getVehicleOrDriverInfo() {
            return vehicleOrDriverInfo;
        }

        public void setVehicleOrDriverInfo(String vehicleOrDriverInfo) {
            this.vehicleOrDriverInfo = vehicleOrDriverInfo;
        }

        public String getDriverLicense() {
            return driverLicense;
        }

        public void setDriverLicense(String driverLicense) {
            this.driverLicense = driverLicense;
        }

        public String getVehicleRegistration() {
            return vehicleRegistration;
        }

        public void setVehicleRegistration(String vehicleRegistration) {
            this.vehicleRegistration = vehicleRegistration;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDistanceInMeters() {
            return distanceInMeters;
        }

        public void setDistanceInMeters(String distanceInMeters) {
            this.distanceInMeters = distanceInMeters;
        }
    }
}

package com.example.fyrflyuser.modelClasses;

import android.telecom.Call;

public class OtpRoot {

    public int status;
    public String message;
    public Details details;

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public class Details {
        public String id;
        public String firstname;
        public String lastname;
        public String phone;
        public String otp;
        public String auth_token;

        public String getAuth_token() {
            return auth_token;
        }

        public void setAuth_token(String auth_token) {
            this.auth_token = auth_token;
        }

        public String created;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }
}
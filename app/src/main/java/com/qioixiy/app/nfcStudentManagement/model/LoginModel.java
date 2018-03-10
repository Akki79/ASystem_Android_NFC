package com.qioixiy.app.nfcStudentManagement.model;

public class LoginModel {
    static LoginModel loginModel = null;

    Integer userId;

    public static LoginModel instance() {
        if (loginModel == null) {
            loginModel = new LoginModel();
        }

        return  loginModel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

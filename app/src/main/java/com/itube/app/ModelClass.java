package com.itube.app;

public class ModelClass {

    public String FullName;
    String Password;
    String Cpassword;
    String UserName;

    public ModelClass(String fullName, String userName, String password, String cpassword) {
        FullName = fullName;
        UserName = userName;
        Password = password;
        Cpassword = cpassword;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCpassword() {
        return Cpassword;
    }

    public void setCpassword(String cpassword) {
        Cpassword = cpassword;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}

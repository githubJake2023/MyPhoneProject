package com.example.myphoneproject;

public class MyContacts {
    private String key;
    private String name;
    private String phone;
    private String email;
    private String gender;

    public MyContacts() {
    }

    public MyContacts(String key, String name, String phone, String email, String gender) {
        this.key = key;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }

    public String getkey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

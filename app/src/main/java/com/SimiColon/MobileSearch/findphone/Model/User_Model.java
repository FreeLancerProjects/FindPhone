package com.SimiColon.MobileSearch.findphone.Model;

/**
 * Created by Elashry on 12/03/2018.
 */

public class User_Model {

    private String name;
    private String username;
    private String password;
    private String photo;
    private String phone;
    private String email;
    private String country;
    private String city;
    private String address;
    private Integer message;
    private String data;

    public User_Model(String name, String username, String password, String photo, String phone, String email, String country, String city, String address) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.photo = photo;
        this.phone = phone;
        this.email = email;
        this.country = country;
        this.city = city;
        this.address = address;
    }

    public User_Model(Integer message, String data) {
        this.message = message;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public User_Model() {
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}

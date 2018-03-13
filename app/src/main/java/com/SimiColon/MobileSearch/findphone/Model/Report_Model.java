package com.SimiColon.MobileSearch.findphone.Model;

/**
 * Created by Elashry on 12/03/2018.
 */

public class Report_Model {

    private String user_id_fk;
    private String imei;
    private String brand;
    private String owner;
    private String statue;
    private String phone;
    private String email;
    private String adress;
    private String photo;
    private String description;

    public Report_Model(String user_id_fk, String imei, String brand, String owner, String statue, String phone, String email, String adress, String photo, String description) {
        this.user_id_fk = user_id_fk;
        this.imei = imei;
        this.brand = brand;
        this.owner = owner;
        this.statue = statue;
        this.phone = phone;
        this.email = email;
        this.adress = adress;
        this.photo = photo;
        this.description = description;
    }

    public String getUser_id_fk() {
        return user_id_fk;
    }

    public void setUser_id_fk(String user_id_fk) {
        this.user_id_fk = user_id_fk;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

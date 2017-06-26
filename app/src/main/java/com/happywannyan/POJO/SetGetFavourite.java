package com.happywannyan.POJO;

/**
 * Created by su on 6/26/17.
 */

public class SetGetFavourite {

    boolean checkRightValue;
    String name,address,reservation,meet_up,contact,img;


    public boolean isCheckRightValue() {
        return checkRightValue;
    }

    public void setCheckRightValue(boolean checkRightValue) {
        this.checkRightValue = checkRightValue;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public String getMeet_up() {
        return meet_up;
    }

    public void setMeet_up(String meet_up) {
        this.meet_up = meet_up;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

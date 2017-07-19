package com.ptplanner.datatype;

/**
 * Created by ltp on 22/01/16.
 */
public class SetDataType {

    String specifically, amount;

    public SetDataType(String specifically, String amount) {
        this.specifically = specifically;
        this.amount = amount;
    }

    public String getSpecifically() {
        return specifically;
    }

    public void setSpecifically(String specifically) {
        this.specifically = specifically;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
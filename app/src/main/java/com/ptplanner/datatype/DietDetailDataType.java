package com.ptplanner.datatype;

import java.util.ArrayList;

/**
 * Created by su on 24/6/15.
 */
public class DietDetailDataType {

    String meal_title;
    String meal_image;
    String instruction;
    String meal_description;
    ArrayList<SetDataType> setDataTypeArrayList;

    public DietDetailDataType(String meal_title, String meal_image, String meal_description,
                              ArrayList<SetDataType> setDataTypeArrayList, String instruction) {
        this.meal_title = meal_title;
        this.meal_image = meal_image;
        this.meal_description = meal_description;
        this.setDataTypeArrayList = setDataTypeArrayList;
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public ArrayList<SetDataType> getSetDataTypeArrayList() {
        return setDataTypeArrayList;
    }

    public void setSetDataTypeArrayList(ArrayList<SetDataType> setDataTypeArrayList) {
        this.setDataTypeArrayList = setDataTypeArrayList;
    }

    public String getMeal_title() {
        return meal_title;
    }

    public void setMeal_title(String meal_title) {
        this.meal_title = meal_title;
    }

    public String getMeal_image() {
        return meal_image;
    }

    public void setMeal_image(String meal_image) {
        this.meal_image = meal_image;
    }

    public String getMeal_description() {
        return meal_description;
    }

    public void setMeal_description(String meal_description) {
        this.meal_description = meal_description;
    }
}

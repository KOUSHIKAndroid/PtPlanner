package com.ptplanner.datatype;

/**
 * Created by su on 18/6/15.
 */
public class AltrainerDataType {
    String pt_id;
    String pt_name;
    String pt_image;
    String working_address;
    String pt_email;

    public String getPt_email() {
        return pt_email;
    }



    public AltrainerDataType(String pt_id, String pt_name, String pt_image, String working_address,String pt_email) {
        this.pt_id = pt_id;
        this.pt_name = pt_name;
        this.pt_image = pt_image;
        this.working_address = working_address;
        this.pt_email=pt_email;
    }

    public String getPt_id() {
        return pt_id;
    }

    public void setPt_id(String pt_id) {
        this.pt_id = pt_id;
    }

    public String getPt_name() {
        return pt_name;
    }

    public void setPt_name(String pt_name) {
        this.pt_name = pt_name;
    }

    public String getPt_image() {
        return pt_image;
    }

    public void setPt_image(String pt_image) {
        this.pt_image = pt_image;
    }

    public String getWorking_address() {
        return working_address;
    }

    public void setWorking_address(String working_address) {
        this.working_address = working_address;

    }
}
package com.ptplanner.datatype;

/**
 * Created by su on 18/6/15.
 */
public class GraphClientGoalImages {
    String id;
    String image;
    String image_thumbnail;
    String uploaded_date;

    public GraphClientGoalImages(String id, String uploaded_date, String image_thumbnail, String image) {
        this.id = id;
        this.uploaded_date = uploaded_date;
        this.image_thumbnail = image_thumbnail;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_thumbnail() {
        return image_thumbnail;
    }

    public void setImage_thumbnail(String image_thumbnail) {
        this.image_thumbnail = image_thumbnail;
    }

    public String getUploaded_date() {
        return uploaded_date;
    }

    public void setUploaded_date(String uploaded_date) {
        this.uploaded_date = uploaded_date;
    }
}

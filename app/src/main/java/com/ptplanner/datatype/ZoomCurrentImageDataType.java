package com.ptplanner.datatype;

/**
 * Created by ltp on 23/11/15.
 */
public class ZoomCurrentImageDataType {

    String uploadDate, imgLink;

    public ZoomCurrentImageDataType(String imgLink, String uploadDate) {
        this.imgLink = imgLink;
        this.uploadDate = uploadDate;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}

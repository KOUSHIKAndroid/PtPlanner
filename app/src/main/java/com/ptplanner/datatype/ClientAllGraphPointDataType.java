package com.ptplanner.datatype;

/**
 * Created by ltp on 05/08/15.
 */
public class ClientAllGraphPointDataType {

    String x_axis_point, y_axis_point;

    public ClientAllGraphPointDataType(String x_axis_point, String y_axis_point) {
        this.x_axis_point = x_axis_point;
        this.y_axis_point = y_axis_point;
    }

    public String getX_axis_point() {
        return x_axis_point;
    }

    public void setX_axis_point(String x_axis_point) {
        this.x_axis_point = x_axis_point;
    }

    public String getY_axis_point() {
        return y_axis_point;
    }

    public void setY_axis_point(String y_axis_point) {
        this.y_axis_point = y_axis_point;
    }
}
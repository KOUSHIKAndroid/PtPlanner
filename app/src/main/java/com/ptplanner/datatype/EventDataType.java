package com.ptplanner.datatype;

public class EventDataType {

    String markedDay, typeEvent, markedMonth;
    boolean isSelected;

    public EventDataType(String markedDay, String typeEvent, boolean isSelected) {
        this.markedDay = markedDay;
        this.typeEvent = typeEvent;
        this.isSelected = isSelected;
    }

    public String getMarkedMonth() {
        return markedMonth;
    }

    public void setMarkedMonth(String markedMonth) {
        this.markedMonth = markedMonth;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getMarkedDay() {
        return markedDay;
    }

    public void setMarkedDay(String markedDay) {
        this.markedDay = markedDay;
    }

    public String getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(String typeEvent) {
        this.typeEvent = typeEvent;
    }

}

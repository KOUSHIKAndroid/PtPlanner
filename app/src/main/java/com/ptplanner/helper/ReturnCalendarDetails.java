package com.ptplanner.helper;

import java.util.Calendar;
import java.util.Locale;

public class ReturnCalendarDetails {
    Calendar calendar = Calendar.getInstance(Locale.getDefault());

    // static int monthLength;

    public static boolean isLeapYear(int year) {
        if (year > 1582) {
            if (year % 4 != 0) {
                return false;
            } else if (year % 100 == 0 && year % 400 != 0) {
                return false;
            } else if (year % 400 == 0) {
                return true;
            } else {
                return true;
            }
        }
        return false;
    }

    public static int getMonthLenth(int month, int year) {
        int monthLength;
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            monthLength = 30;
        } else if (month == 2) {
            if (isLeapYear(year)) {
                monthLength = 28 + 1;
            } else {
                monthLength = 28;
            }
        } else {
            monthLength = 31;
        }
        return monthLength;
    }

    public static int getPosition(String day) {
        int position = 0;
        if (day.equalsIgnoreCase("sun")) {
            position = 6;
        } else if (day.equalsIgnoreCase("mon")) {
            position = 0;
        } else if (day.equalsIgnoreCase("tue")) {
            position = 1;
        } else if (day.equalsIgnoreCase("wed")) {
            position = 2;
        } else if (day.equalsIgnoreCase("thu")) {
            position = 3;
        } else if (day.equalsIgnoreCase("fri")) {
            position = 4;
        } else if (day.equalsIgnoreCase("sat")) {
            position = 5;
        } else if (day.equalsIgnoreCase("Sön")) {
            position = 6;
        } else if (day.equalsIgnoreCase("Mån")) {
            position = 0;
        } else if (day.equalsIgnoreCase("Tis")) {
            position = 1;
        } else if (day.equalsIgnoreCase("Ons")) {
            position = 2;
        } else if (day.equalsIgnoreCase("Tor")) {
            position = 3;
        } else if (day.equalsIgnoreCase("Fre")) {
            position = 4;
        } else if (day.equalsIgnoreCase("Lör")) {
            position = 5;
        } else {
            position = -1;
        }
//        if (day.equalsIgnoreCase("sun")) {
//            position = 0;
//        } else if (day.equalsIgnoreCase("mon")) {
//            position = 1;
//        } else if (day.equalsIgnoreCase("tue")) {
//            position = 2;
//        } else if (day.equalsIgnoreCase("wed")) {
//            position = 3;
//        } else if (day.equalsIgnoreCase("thu")) {
//            position = 4;
//        } else if (day.equalsIgnoreCase("fri")) {
//            position = 5;
//        } else if (day.equalsIgnoreCase("sat")) {
//            position = 6;
//        } else if (day.equalsIgnoreCase("Sön")) {
//            position = 0;
//        } else if (day.equalsIgnoreCase("Mån")) {
//            position = 1;
//        } else if (day.equalsIgnoreCase("Tis")) {
//            position = 2;
//        } else if (day.equalsIgnoreCase("Ons")) {
//            position = 3;
//        } else if (day.equalsIgnoreCase("Tor")) {
//            position = 4;
//        } else if (day.equalsIgnoreCase("Fre")) {
//            position = 5;
//        } else if (day.equalsIgnoreCase("Lör")) {
//            position = 6;
//        } else {
//            position = -1;
//        }
        return position;
    }

    public static int getCurrentMonth(String month) {
        int monthMosition = 0;
        if (month.equalsIgnoreCase("jan")) {
            monthMosition = 1;
        } else if (month.equalsIgnoreCase("feb")) {
            monthMosition = 2;
        } else if (month.equalsIgnoreCase("mar")) {
            monthMosition = 3;
        } else if (month.equalsIgnoreCase("apr")) {
            monthMosition = 4;
        } else if (month.equalsIgnoreCase("may")) {
            monthMosition = 5;
        } else if (month.equalsIgnoreCase("jun")) {
            monthMosition = 6;
        } else if (month.equalsIgnoreCase("jul")) {
            monthMosition = 7;
        } else if (month.equalsIgnoreCase("aug")) {
            monthMosition = 8;
        } else if (month.equalsIgnoreCase("sep")) {
            monthMosition = 9;
        } else if (month.equalsIgnoreCase("oct")) {
            monthMosition = 10;
        } else if (month.equalsIgnoreCase("okt")) {
            monthMosition = 10;
        } else if (month.equalsIgnoreCase("nov")) {
            monthMosition = 11;
        } else if (month.equalsIgnoreCase("dec")) {
            monthMosition = 12;
        } else {
            monthMosition = -1;
        }
        return monthMosition;
    }

    public static String getPreviousMonthName(String month) {

        String MONTH = "";

        if (AppConfig.LANGUAGE.equals("sv")) {
            if (month == "JANUARI") {
                MONTH = "DECEMBER";
            } else if (month == "FEBRUARI") {
                MONTH = "JANUARI";
            } else if (month == "MARS") {
                MONTH = "FEBRUARI";
            } else if (month == "APRIL") {
                MONTH = "MARS";
            } else if (month == "MAJ") {
                MONTH = "APRIL";
            } else if (month == "JUNI") {
                MONTH = "MAJ";
            } else if (month == "JULI") {
                MONTH = "JUNI";
            } else if (month == "AUGUSTI") {
                MONTH = "JULI";
            } else if (month == "SEPTEMBER") {
                MONTH = "AUGUSTI";
            } else if (month == "OKTOBER") {
                MONTH = "SEPTEMBER";
            } else if (month == "NOVEMBER") {
                MONTH = "OKTOBER";
            } else if (month == "DECEMBER") {
                MONTH = "NOVEMBER";
            } else {
                MONTH = "TOTALMONTH";
            }
        } else {
            if (month == "JANUARY") {
                MONTH = "DECEMBER";
            } else if (month == "FEBRUARY") {
                MONTH = "JANUARY";
            } else if (month == "MARCH") {
                MONTH = "FEBRUARY";
            } else if (month == "APRIL") {
                MONTH = "MARCH";
            } else if (month == "MAY") {
                MONTH = "APRIL";
            } else if (month == "JUNE") {
                MONTH = "MAY";
            } else if (month == "JULY") {
                MONTH = "JUNE";
            } else if (month == "AUGUST") {
                MONTH = "JULY";
            } else if (month == "SEPTEMBER") {
                MONTH = "AUGUST";
            } else if (month == "OCTOBER") {
                MONTH = "SEPTEMBER";
            } else if (month == "NOVEMBER") {
                MONTH = "OCTOBER";
            } else if (month == "DECEMBER") {
                MONTH = "NOVEMBER";
            } else {
                MONTH = "TOTALMONTH";
            }
        }
        return MONTH;
    }

    public static String getNextMonthName(String month) {

        String MONTH = "";

        if (AppConfig.LANGUAGE.equals("sv")) {
            if (month == "JANUARI") {
                MONTH = "FEBRUARI";
            } else if (month == "FEBRUARI") {
                MONTH = "MARS";
            } else if (month == "MARS") {
                MONTH = "APRIL";
            } else if (month == "APRIL") {
                MONTH = "MAJ";
            } else if (month == "MAJ") {
                MONTH = "JUNI";
            } else if (month == "JUNI") {
                MONTH = "JULI";
            } else if (month == "JULI") {
                MONTH = "AUGUSTI";
            } else if (month == "AUGUSTI") {
                MONTH = "SEPTEMBER";
            } else if (month == "SEPTEMBER") {
                MONTH = "OKTOBER";
            } else if (month == "OKTOBER") {
                MONTH = "NOVEMBER";
            } else if (month == "NOVEMBER") {
                MONTH = "DECEMBER";
            } else if (month == "DECEMBER") {
                MONTH = "JANUARI";
            } else {
                MONTH = "TOTALMONTH";
            }
        } else {
            if (month == "JANUARY") {
                MONTH = "FEBRUARY";
            } else if (month == "FEBRUARY") {
                MONTH = "MARCH";
            } else if (month == "MARCH") {
                MONTH = "APRIL";
            } else if (month == "APRIL") {
                MONTH = "MAY";
            } else if (month == "MAY") {
                MONTH = "JUNE";
            } else if (month == "JUNE") {
                MONTH = "JULY";
            } else if (month == "JULY") {
                MONTH = "AUGUST";
            } else if (month == "AUGUST") {
                MONTH = "SEPTEMBER";
            } else if (month == "SEPTEMBER") {
                MONTH = "OCTOBER";
            } else if (month == "OCTOBER") {
                MONTH = "NOVEMBER";
            } else if (month == "NOVEMBER") {
                MONTH = "DECEMBER";
            } else if (month == "DECEMBER") {
                MONTH = "JANUARY";
            } else {
                MONTH = "TOTALMONTH";
            }
        }
        return MONTH;
    }

    public static String getCurrentMonthName(int month) {

        String MONTH = "";

        if (AppConfig.LANGUAGE.equals("sv")) {
            if (month == 1) {
                MONTH = "JANUARI";
            } else if (month == 2) {
                MONTH = "FEBRUARI";
            } else if (month == 3) {
                MONTH = "MARS";
            } else if (month == 4) {
                MONTH = "APRIL";
            } else if (month == 5) {
                MONTH = "MAJ";
            } else if (month == 6) {
                MONTH = "JUNI";
            } else if (month == 7) {
                MONTH = "JULI";
            } else if (month == 8) {
                MONTH = "AUGUSTI";
            } else if (month == 9) {
                MONTH = "SEPTEMBER";
            } else if (month == 10) {
                MONTH = "OKTOBER";
            } else if (month == 11) {
                MONTH = "NOVEMBER";
            } else if (month == 12) {
                MONTH = "DECEMBER";
            } else {
                MONTH = "TOTALMONTH";
            }
        } else {
            if (month == 1) {
                MONTH = "JANUARY";
            } else if (month == 2) {
                MONTH = "FEBRUARY";
            } else if (month == 3) {
                MONTH = "MARCH";
            } else if (month == 4) {
                MONTH = "APRIL";
            } else if (month == 5) {
                MONTH = "MAY";
            } else if (month == 6) {
                MONTH = "JUNE";
            } else if (month == 7) {
                MONTH = "JULY";
            } else if (month == 8) {
                MONTH = "AUGUST";
            } else if (month == 9) {
                MONTH = "SEPTEMBER";
            } else if (month == 10) {
                MONTH = "OCTOBER";
            } else if (month == 11) {
                MONTH = "NOVEMBER";
            } else if (month == 12) {
                MONTH = "DECEMBER";
            } else {
                MONTH = "TOTALMONTH";
            }
        }
        return MONTH;
    }
}

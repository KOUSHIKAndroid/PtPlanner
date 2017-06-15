package com.ptplanner.helper;

import android.content.SharedPreferences;
import android.os.Environment;

import com.ptplanner.datatype.AllExercisesDataType;
import com.ptplanner.datatype.AppointDataType;
import com.ptplanner.datatype.AvailableDateDataType;
import com.ptplanner.datatype.DiaryDataType;
import com.ptplanner.datatype.ExerciseSetsDataype;
import com.ptplanner.datatype.GraphDetailsDataType;
import com.ptplanner.datatype.GraphDetailsPointDataType;
import com.ptplanner.datatype.LoginDataType;
import com.ptplanner.datatype.MealDateDataType;
import com.ptplanner.datatype.ProgramDateDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AppConfig {

//    public static String HOST = "http://esolz.co.in/lab6/ptplanner/";
    //public static String HOST = "http://pt-planner.com/ptp/";
//     public static String HOST = "http://www.pt-planner.com/";
     public static String HOST = "https://pt-planner.com/";

     public static String selected_date_from_calender = "";

    public static boolean firstTimeChoose = false;

    public static String LANGUAGE = "sv"; // language "sv --- > swedish :: en ---- > english"

    public static LoginDataType loginDatatype;

    public static SharedPreferences loginPreferences;
    public static boolean isRemember = true;
    public static String strRemember = "N";

    public static String changeDate = "";

    public static ArrayList<AppointDataType> appointmentArrayList = new ArrayList<AppointDataType>();
    public static ArrayList<ProgramDateDataType> programArrayList = new ArrayList<ProgramDateDataType>();
    public static ArrayList<MealDateDataType> mealArrayList = new ArrayList<MealDateDataType>();
    public static ArrayList<DiaryDataType> diaryArrayList = new ArrayList<DiaryDataType>();
    public static ArrayList<AvailableDateDataType> availableDateArrayList = new ArrayList<AvailableDateDataType>();

    public static String PT_NAME = "";
    public static String PT_ID = "";


    public static ArrayList<AllExercisesDataType> allExercisesDataTypeArrayList/* = new ArrayList<AllExercisesDataType>()*/;
    public static AllExercisesDataType allExercisesDataType;
    public static ArrayList<ExerciseSetsDataype> exerciseSetsDataypeArrayList/* = new ArrayList<ExerciseSetsDataype>()*/;
    public static ExerciseSetsDataype exerciseSetsDataype;

    public static ArrayList<GraphDetailsDataType> graphDetailsDataTypeArrayList;
    public static GraphDetailsDataType graphDetailsDataType;
    public static ArrayList<GraphDetailsPointDataType> graphDetailsPointDataTypeArrayList;
    public static GraphDetailsPointDataType graphDetailsPointDataType;

    public static String appRegId = "";

    public static boolean remindMe = false;

    public static final class FOLDERS {

        private static final String ROOT = File.separator + "CameraModule";

        private static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();

        public static final String PATH = SD_CARD_PATH + ROOT;

    }

    public static Calendar calendar = Calendar.getInstance(Locale.getDefault());
    public static int currentDate = calendar.get(Calendar.DATE);
    public static int currentDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
    public static int currentMonth = (calendar.get(Calendar.MONTH));
    public static int currentYear = calendar.get(Calendar.YEAR);
    public static int firstDayPosition = calendar.get(Calendar.DAY_OF_WEEK);

    public static Calendar calendarBookApp = calendar;
    public static int currentDateBookApp = currentDate;
    public static int currentDayBookApp = currentDay;
    public static int currentMonthBookApp = currentMonth;
    public static int currentYearBookApp = currentYear;
    public static int firstDayPositionBookApp = firstDayPosition;

//    public static Calendar calendarBookApp = Calendar.getInstance(Locale.getDefault());
//    public static int currentDateBookApp = calendarBookApp.get(Calendar.DATE);
//    public static int currentDayBookApp = calendarBookApp.getActualMinimum(Calendar.DAY_OF_MONTH);
//    public static int currentMonthBookApp = (calendarBookApp.get(Calendar.MONTH));
//    public static int currentYearBookApp = calendarBookApp.get(Calendar.YEAR);
//    public static int firstDayPositionBookApp = calendarBookApp.get(Calendar.DAY_OF_WEEK);

    public static String bookAppDateChange = "";

}

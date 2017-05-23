package com.awesome.medifofo;

/**
 * Created by Eunsik on 04/22/2017.
 */

public class Person {

    public static String fistName;
    public static String middleName;
    public static String lastName;
    public static String email;
    public static String gender;
    public static String age;
    public static String country;

    public static String st_place;
    public static String st_main;
    public static int st_scale;
    public static String st_sub;
    public static String st_comment;

    public static String height;
    public static String weight;
    public static String abo;
    public static String medicine;
    public static String allergy;
    public static String history;
    public static String sleeptime;
    public static String dailystride;

    public static void initPerson() {
        height = "-1";
        weight = "-1";
        abo = "-1";
        medicine = "-1";
        allergy = "-1";
        history = "-1";
        sleeptime = "-1";
        dailystride = "-1";
    }

    public static String d_height = "-1"; //detail height
    public static String d_weight = "-1";
    public static String d_abo = "-1";
    public static String d_medicine = "-1";
    public static String d_allergy = "-1";
    public static String d_history = "-1";
    public static String d_sleeptime = "-1";
    public static String d_dailystride = "-1";

}

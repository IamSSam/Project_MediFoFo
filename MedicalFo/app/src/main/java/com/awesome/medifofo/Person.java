package com.awesome.medifofo;

import java.util.Date;

/**
 * Created by yoonjae on 28/11/2016.
 */

public class Person {

    public static String fistName;
    public static String lastName;
    public static String email;
    public static String password;
    public static int gender;
    public static int age;
    public static String country;

    public static String st_place;
    public static String st_main;
    public static int st_scale;
    public static String st_sub;
    public static String st_comment;

    public static String d_height = "-1"; //detail height
    public static String d_weight = "-1";
    public static String d_abo = "-1";
    public static String d_medicine = "-1";
    public static String d_allergy = "-1";
    public static String d_history = "-1";
    public static String d_sleeptime = "-1";
    public static String d_dailystride = "-1";

    public static void initPerson() {
        d_height = "-1";
        d_weight = "-1";
        d_abo = "-1";
        d_medicine = "-1";
        d_allergy = "-1";
        d_history = "-1";
        d_sleeptime = "-1";
        d_dailystride = "-1";
    }
}

package com.awesome.medifofo;

/**
 * Created by Eunsik on 04/22/2017.
 */

public class Person {

    public String fistName;
    public String lastName;
    public String email;
    public String password;
    public String gender;
    public int age;
    public String country;

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

package com.awesome.medifofo.model;

/**
 * Created by 17 on 2017-04-09.
 */

public class ListItem {

    private String text, hospitalName;

    public String getTitle() {
        return text;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setTitle(String text) {
        this.text = text;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

}

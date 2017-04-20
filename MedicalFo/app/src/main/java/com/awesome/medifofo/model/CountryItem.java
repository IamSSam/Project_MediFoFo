package com.awesome.medifofo.model;

/**
 * Created by 17 on 2017-04-03.
 */

public class CountryItem {
    private String countryName;
    private Integer flagId;

    public CountryItem(String countryName){
        this.countryName = countryName;
    }

    public CountryItem(String countryName, Integer flagId){
        this.countryName = countryName;
        this.flagId = flagId;
    }

    public String getCountryName(){
        return countryName;
    }

    public Integer getFlagId(){
        return flagId;
    }

}

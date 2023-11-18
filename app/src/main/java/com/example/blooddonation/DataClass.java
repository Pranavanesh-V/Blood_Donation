package com.example.blooddonation;

public class DataClass
{
    private String dataLocation;
    private String dataName;
    private String dataBlood;

    public String getDataLocation() {
        return dataLocation;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataBlood() {
        return dataBlood;
    }


    public DataClass(String dataName,String dataLocation,String dataBlood)
    {
        this.dataName =dataName;
        this.dataLocation =dataLocation;
        this.dataBlood=dataBlood;
    }
}

package com.example.blooddonation;

public class DataClass2
{
    private String dataLocation;
    private String dataName;
    private String dataBlood;
    private String txt;
    public String getTxt() {
        return txt;
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataBlood() {
        return dataBlood;
    }


    public DataClass2(String dataName,String dataLocation,String dataBlood,String txt)
    {
        this.dataName =dataName;
        this.dataLocation =dataLocation;
        this.dataBlood=dataBlood;
        this.txt=txt;
    }
}

package com.example.blooddonation;

public class DataClass
{
    //This class is for getting the Donor Information
    private String dataLocation;
    private String dataName;
    private String dataBlood;
    private String dataURL;
    public String getDataURL() {
        return dataURL;
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


    public DataClass(String dataName,String dataLocation,String dataBlood,String dataURL)
    {
        this.dataName =dataName;
        this.dataLocation =dataLocation;
        this.dataBlood=dataBlood;
        this.dataURL=dataURL;
    }
}

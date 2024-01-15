package com.example.blooddonation;

public class DataClass2
{
    //This class is for Getting the Requesters Information
    private String dataLocation;
    private String dataName;
    private String dataBlood;
    private String txt;



    public String uri;
    public String getUri() {
        return uri;
    }
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


    public DataClass2(String dataName,String dataLocation,String dataBlood,String txt,String uri)
    {
        this.dataName =dataName;
        this.dataLocation =dataLocation;
        this.dataBlood=dataBlood;
        this.txt=txt;
        this.uri=uri;
    }
}

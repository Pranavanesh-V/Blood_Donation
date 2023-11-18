package com.example.blooddonation;

import android.net.Uri;

public class DataClass2
{
    private String dataLocation;
    private String dataName;
    private String dataBlood;
    private String txt;
    private Uri uri;
    public Uri getUri() {
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


    public DataClass2(String dataName,String dataLocation,String dataBlood,String txt,Uri S_uri)
    {
        this.dataName =dataName;
        this.dataLocation =dataLocation;
        this.dataBlood=dataBlood;
        this.txt=txt;
        this.uri=S_uri;
    }
}

package com.example.blooddonation;

import android.net.Uri;

public class DataClass
{
    private String dataLocation;
    private String dataName;
    private String dataBlood;
    private Uri uri;
    public Uri getUri() {
        return uri;
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


    public DataClass(String dataName,String dataLocation,String dataBlood,Uri S_uri)
    {
        this.dataName =dataName;
        this.dataLocation =dataLocation;
        this.dataBlood=dataBlood;
        this.uri=S_uri;
    }
}

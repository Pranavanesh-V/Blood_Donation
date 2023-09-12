package com.example.blooddonation;

public class DataClass
{
    private String dataPhone;
    private String dataName;
    private String dataBlood;

    public String getDataPhone() {
        return dataPhone;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataBlood() {
        return dataBlood;
    }


    public DataClass(String dataName,String dataPhone,String dataBlood)
    {
        this.dataName =dataName;
        this.dataPhone =dataPhone;
        this.dataBlood=dataBlood;
    }
}

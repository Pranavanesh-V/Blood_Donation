package com.example.blooddonation;

public class History_dataClass
{
    private String Name;
    private String Blood_Group;

    public History_dataClass(String name, String blood_Group) {
        this.Name = name;
        this.Blood_Group = blood_Group;
    }
    public String getName() {
        return Name;
    }
    public String getBlood_Group() {
        return Blood_Group;
    }
}

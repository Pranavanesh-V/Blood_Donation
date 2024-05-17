package com.example.blooddonation;

public class History_dataClass
{
    //Data class used for displaying the history of the user
    //who he has donated to
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

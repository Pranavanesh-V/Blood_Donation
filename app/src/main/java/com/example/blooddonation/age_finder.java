package com.example.blooddonation;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class age_finder
{
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Integer age_Cals(String dob)
    {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //LocalDate dob_l = LocalDate.parse(dob, formatter);
        /*int birthYear = dob_l.getYear();
        int birthMonth =dob_l.getMonthValue(); // January is represented as 0
        int birthDay = dob_l.getDayOfMonth();*/
        int birthYear =Integer.parseInt(dob.substring(0,4));
        int birthMonth =Integer.parseInt(dob.substring(5,7));
        int birthDay=Integer.parseInt(dob.substring(8,10));

        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Set the birthdate in the current year
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(Calendar.YEAR, currentDate.get(Calendar.YEAR));
        birthDate.set(Calendar.MONTH, birthMonth);
        birthDate.set(Calendar.DAY_OF_MONTH, birthDay);

        // Calculate the age
        int ageYears = currentDate.get(Calendar.YEAR) - birthYear;
        int ageMonths = currentDate.get(Calendar.MONTH) - birthMonth;
        int ageDays = currentDate.get(Calendar.DAY_OF_MONTH) - birthDay;

        // Adjust age based on whether the birthday has passed
        if (ageMonths < 0 || (ageMonths == 0 && ageDays < 0)) {
            ageYears--;
            ageMonths += 12;
        }

        System.out.println("Exact Age:");
        System.out.println("Years: " + ageYears);
        System.out.println("Months: " + ageMonths);
        System.out.println("Days: " + ageDays);

        return ageYears;
    }

    public boolean equals_rt(Integer age12,String choice)
    {
        boolean res=false;

        if (choice.equals("18-24")) {
            if (age12 >= 18 && age12 <= 24) {
                res=true;
            }
        } else if ("25-34".equals(choice)) {
            if (age12 >= 25 && age12 <= 34) {
                res=true;
            }
        } else if ("35-44".equals(choice)) {
            if (age12 >= 34 && age12 <= 44) {
                res=true;
            }
        } else if ("45-54".equals(choice)) {
            if (age12 >= 45 && age12 <= 54) {
                res=true;
            }
        } else if ("55 Above".equals(choice)) {
            if (age12 >= 55) {
                res=true;
            }
        }

        return res;
    }
}

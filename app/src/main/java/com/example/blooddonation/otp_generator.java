package com.example.blooddonation;

import java.util.Random;

public class otp_generator
{
    public String generate()
    {
        Random random=new Random();
        String sum = "";

        for (int i=0;i<4;i++)
        {
            int ran=random.nextInt(9);
            //sum.concat(Integer.toString(ran));
            sum+=Integer.toString(ran);
        }
        return sum;
    }
}

package com.example.blooddonation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPageAdapter extends PagerAdapter
{

    //Class is used for Intro pages content binding
    Context context;
    int[] Headings ={
            R.string.Heading1,
            R.string.Heading2,
            R.string.Heading3
    };
    int[] Description ={
            R.string.Desc1,
            R.string.Desc2,
            R.string.Desc3,
    };
    int[] img={R.drawable.intro1,R.drawable.intro2,R.drawable.intro3};


    public ViewPageAdapter(Context context)
    {
        this.context=context;
    }

    public
    ViewPageAdapter() {
    }

    @Override
    public int getCount() {
        return Headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slider_layout,container,false);
        TextView sliderHeading;
        TextView sliderDesc;
        ImageView imageView;
        if (position==0||position==4)
        {

            sliderHeading= view.findViewById(R.id.Head1);
            sliderDesc= view.findViewById(R.id.Desc1);
            imageView=view.findViewById(R.id.pages1);

            sliderHeading.setText(Headings[position]);
            sliderDesc.setText(Description[position]);
            imageView.setImageResource(img[position]);
            container.addView(view);
            return view;

        }
        else if (position==2) {
            sliderHeading= view.findViewById(R.id.Head1);
            sliderDesc= view.findViewById(R.id.Desc1);
            imageView=view.findViewById(R.id.pages1);

            sliderHeading.setText(Headings[position]);
            sliderDesc.setText(Description[position]);
            imageView.setImageResource(img[position]);
            container.addView(view);
            return view;

        }

        sliderHeading= view.findViewById(R.id.Head1);
        sliderDesc= view.findViewById(R.id.Desc1);
        imageView=view.findViewById(R.id.pages1);

        sliderHeading.setText(Headings[position]);
        sliderDesc.setText(Description[position]);
        imageView.setImageResource(img[position]);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
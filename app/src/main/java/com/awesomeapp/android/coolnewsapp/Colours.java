package com.awesomeapp.android.coolnewsapp;

public class Colours {

    private static int colors[] = {R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5};

    public static int choseColour(int position) {

        return colors[position % colors.length];
    }
}

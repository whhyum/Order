package com.example.exp_2020.util;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exp_2020.R;

public class ImageUtil extends AppCompatActivity {
    public int getImageId(String imgFile) {
        String[] names=imgFile.split("\\.");
        if(names.length==0)
            return R.drawable.aa;
        int id = getResources().getIdentifier(names[0], "drawable",
                "com.example.exp_2020");
        if(id==0)
            return R.drawable.aa;
        else
            return id;
    }
}

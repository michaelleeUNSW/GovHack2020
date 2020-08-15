package com.example.boone.app3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageProcessing {
    public ImageProcessing(){

    }

    public static Bitmap decodeFile(String photoPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inPreferQualityOverSpeed = true;

        return BitmapFactory.decodeFile(photoPath, options);
    }
}

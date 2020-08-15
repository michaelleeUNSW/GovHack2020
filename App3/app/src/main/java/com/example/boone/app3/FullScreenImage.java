package com.example.boone.app3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class FullScreenImage extends Activity {

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_screen_image);

        fullScreen();

        final String bitmap = getIntent().getStringExtra("bitmap");
        Bitmap bmp = ImageProcessing.decodeFile(bitmap);


        ImageView imgDisplay, btnClose, btndelete;

        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        btnClose = findViewById(R.id.btnClose);
        btndelete = findViewById(R.id.btnDelete);

        btnClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FullScreenImage.this.finish();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("Bitmap",bitmap);
                setResult(Activity.RESULT_OK,resultIntent);
                FullScreenImage.this.finish();

            }
        });
        imgDisplay.setImageBitmap(bmp);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fullScreen();
    }

    public void fullScreen() {


        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;


        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("TAG", "Turning immersive mode mode off. ");
        } else {
            Log.i("TAG", "Turning immersive mode mode on.");
        }

        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}
package com.example.boone.app3;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.util.ArrayList;

public class Facebook_pop_up {

    public  ArrayList<String> bitmap_array = new ArrayList<>();
    ShareDialog shareDialog;

    public Facebook_pop_up(){

    }

    public void SetUp(MainActivity mainActivity, ArrayList<String> bm_array) {

        bitmap_array = bm_array;
        ClipboardManager clipboard = (ClipboardManager) mainActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        String name = "";
        String price = "";
        String brand = "";

        EditText editText_name = mainActivity.findViewById(R.id.name);
        if (editText_name.getText().toString() != "") {
            name = " " + editText_name.getText().toString();
        }
        EditText editText_price = mainActivity.findViewById(R.id.Price);
        if (editText_price.getText().toString() != "") {
            price = " " + editText_price.getText().toString();
        }
        AutoCompleteTextView autoCompleteTextView = mainActivity.findViewById(R.id.Brand);

        if (autoCompleteTextView.getText().toString() != "") {
            brand = " " + autoCompleteTextView.getText().toString();
        }

        String message = "Selling (a)" + brand + name + " at" + price + ".\nPM if interested. ";
        ClipData clip = ClipData.newPlainText("simple text", message);
        clipboard.setPrimaryClip(clip);

        CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(mainActivity);

        new AlertDialog.Builder(mainActivity)
                .setMessage("Your product details has been added to the clipboard. Just paste them onto your facebook post :)")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageProcessing imageProcessing = new ImageProcessing();

                        SharePhotoContent.Builder builder = new SharePhotoContent.Builder();
                        Log.d("testing_M2", String.valueOf(ShareDialog.canShow(SharePhotoContent.class)));

                        for (String bm : bitmap_array) {
                            Bitmap bitmap_decoded = ImageProcessing.decodeFile(bm);
                            SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap_decoded).build();
                            builder.addPhoto(sharePhoto);
                        }

                        SharePhotoContent shareContent = builder.build();
                        shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
                    }
                }).show();
    }
}

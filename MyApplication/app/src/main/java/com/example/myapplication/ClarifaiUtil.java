package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class ClarifaiUtil {
  private ClarifaiUtil() {
    throw new UnsupportedOperationException("No instances");
  }

  /**
   * @param context
   * @param data
   * @return
   */
  @Nullable
  public static byte[] retrieveSelectedImage(@NonNull Context context, @NonNull Intent data) {
    InputStream inStream = null;
    Bitmap bitmap = null;
    try {
      inStream = context.getContentResolver().openInputStream(data.getData());
      bitmap = BitmapFactory.decodeStream(inStream);
      final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
      return outStream.toByteArray();
    } catch (FileNotFoundException e) {
      return null;
    } finally {
      if (inStream != null) {
        try {
          inStream.close();
        } catch (IOException ignored) {
        }
      }
      if (bitmap != null) {
        bitmap.recycle();
      }
    }
  }

}

package org.story.storyapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CanvasIO {
    private final static String TAG = Canvas.class.getName();

    public static void saveBitmap(Context context, Bitmap saveFile, int id, String path) {
        try {
            String recordPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + MySharedPreference.getId(context) + "/" + path;
            File file = new File(recordPath);
            file.mkdirs();
            FileOutputStream fos;
            if (id < 10) {
                fos = new FileOutputStream(recordPath + "/" + 0 + id + "_" + MySharedPreference.getId(context) + ".png");
            } else {
                fos = new FileOutputStream(recordPath + "/" + id + "_" + MySharedPreference.getId(context) + ".png");
            }
            saveFile.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (IOException e) {
            Log.d(TAG, "Don't save canvas");
            e.printStackTrace();
        }
    }

}
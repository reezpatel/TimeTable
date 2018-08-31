package com.reezx.android.timetable;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.reezx.android.timetable.Constants.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by reezpatel on 01-Jul-17.
 */

public class ProfilePicLoader extends AsyncTask<String, Void, Void> {
    private Context mContext = null;
    private ImageView imageView = null;
    private Bitmap bitmap;

    ProfilePicLoader(Context mContext, ImageView imageView) {
        this.mContext = mContext;
        this.imageView = imageView;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            URL uri = new URL(params[0]);
            InputStream inputStream = uri.openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            //Store Bitmap
            storeImage(bitmap);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void storeImage(Bitmap image) {
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getFilesDir();
        File mypath=new File(directory,"profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.bitmap = image;
        Log.d("TAG",directory.getAbsolutePath());
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ContextWrapper cw = new ContextWrapper(mContext);
        File directory = cw.getFilesDir();
        File mypath=new File(directory,"profile.jpg");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(mypath);
        } catch (FileNotFoundException e) {
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        imageView.setImageBitmap(bitmap);
    }
}

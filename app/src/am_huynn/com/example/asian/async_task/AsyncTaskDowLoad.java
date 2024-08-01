package com.example.asian.async_task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskDowLoad extends AsyncTask<String, Integer, Bitmap> {

    private ImageView mImageView;
    private ProgressBar mPbProgress;

    public AsyncTaskDowLoad(ImageView imageView, ProgressBar pbProgress) {
        this.mImageView = imageView;
        this.mPbProgress = pbProgress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Start");
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            int fileLength = connection.getContentLength();

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Integer value = (int) (total * 100 / fileLength);
                publishProgress(Integer.parseInt(value.toString()));
            }

            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        mPbProgress.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mImageView.setImageBitmap(bitmap);
        System.out.println("success");
    }
}
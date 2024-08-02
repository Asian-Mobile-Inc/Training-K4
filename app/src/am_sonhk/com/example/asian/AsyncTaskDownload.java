package com.example.asian;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


//@SuppressWarnings("ALL")
public class AsyncTaskDownload extends AppCompatActivity {
    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;
    ImageView imageView = null;
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_task);
        Button button = findViewById(R.id.btnAsyncTask);
        imageView = findViewById(R.id.image);
        button.setOnClickListener(v -> {
            AsyncTaskExample asyncTask = new AsyncTaskExample();
            asyncTask.execute("https://images.pexels.com/photos/20765361/pexels-photo-20765361.jpeg?cs=srgb&dl=pexels-kltdinusha-20765361.jpg&fm=jpg&_gl=1*1lobks0*_ga*MTgyMTEzNzc0Ni4xNzIyMzkwNjcy*_ga_8JE65Q40S6*MTcyMjUwNTE1Ni4yLjEuMTcyMjUwNTQ4Ny4wLjAuMA..");
        });
    }

    private class AsyncTaskExample extends android.os.AsyncTask<String, Integer, Bitmap> {
        private int totalSize = 0;
        private boolean downloadFailed = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(AsyncTaskDownload.this);
            p.setMessage("Please wait...It is downloading");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                ImageUrl = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                int fileLength = conn.getContentLength();
                totalSize = fileLength;
                is = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                int totalBytesRead = 0;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;

                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                while ((bytesRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    publishProgress(totalBytesRead, fileLength);
                }

                byte[] imageData = baos.toByteArray();
                bmImg = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);

            } catch (IOException e) {
                e.printStackTrace();
                downloadFailed = true;
            }
            return bmImg;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = (int) ((values[0] / (float) totalSize) * 100);
            p.setProgress(progress);
            p.setMessage("Downloading " + values[0] / 1024 + "KB/" + values[1] / 1024 + "KB");
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (p.isShowing()) {
                p.dismiss();
            }
            if (downloadFailed || bitmap == null) {
                Toast.makeText(AsyncTaskDownload.this, "Failed to download image", Toast.LENGTH_SHORT).show();
            } else {
                imageView.setImageBitmap(bitmap);
                Toast.makeText(AsyncTaskDownload.this, "Download complete", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

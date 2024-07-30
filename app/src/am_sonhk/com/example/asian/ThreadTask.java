package com.example.asian;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThreadTask extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = (String) msg.obj;
            showToast(message);
        }
    };
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_task);

        Button btnDownload = findViewById(R.id.btnDownload);
        EditText edtURL = findViewById(R.id.edtURL);

        mImageView = findViewById(R.id.imageView);


        btnDownload.setOnClickListener(view -> {
            String imageUrl = edtURL.getText().toString();
            downloadAndDisplayImage(imageUrl);
        });

    }

    private void downloadAndDisplayImage(final String imageUrl) {
        Thread downloadThread = new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                Message message = mHandler.obtainMessage(1, "Started to download image");
                mHandler.sendMessage(message);

                InputStream inputStream = connection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                connection.disconnect();

                runOnUiThread(() -> {
                    mImageView.setImageBitmap(bitmap);
                    showToast("Finished to download image");
                });

            } catch (Exception e) {
                Message message = mHandler.obtainMessage(1, "Failed to download image");
                mHandler.sendMessage(message);
            }
        });
        downloadThread.start();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

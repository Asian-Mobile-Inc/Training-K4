package com.example.asian;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThreadTask extends AppCompatActivity {

    private final Handler mHandler = new UIHandler(this);
    private ImageView mImageView;
    private Button mBtnDownload;
    private EditText mEdtURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_task);
        initUI();
        initListener();
    }

    private void initListener() {
        mBtnDownload.setOnClickListener(view -> {
            String imageUrl = mEdtURL.getText().toString();
            downloadAndDisplayImage(imageUrl);
        });
    }

    private void initUI() {
        mBtnDownload = findViewById(R.id.btnDownload);
        mEdtURL = findViewById(R.id.edtURL);
        mImageView = findViewById(R.id.imageView);
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

    // Static inner class to avoid memory leaks
    private static class UIHandler extends Handler {
        private final WeakReference<ThreadTask> mActivity;

        UIHandler(ThreadTask activity) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ThreadTask activity = mActivity.get();
            if (activity != null) {
                String message = (String) msg.obj;
                activity.showToast(message);
            }
        }
    }
}

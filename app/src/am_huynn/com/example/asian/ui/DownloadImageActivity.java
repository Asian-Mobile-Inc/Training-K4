package com.example.asian.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;

import java.io.IOException;
import java.net.URL;

public class DownloadImageActivity extends AppCompatActivity {
    private Button mBtnDownloadThread;
    private ImageView mIvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image);
        initView();
        initListener();
    }

    private void initView() {
        mBtnDownloadThread = findViewById(R.id.btnDownloadThread);
        mIvImage = findViewById(R.id.ivImage);
    }

    private void initListener() {
        mBtnDownloadThread.setOnClickListener(view -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(() -> Toast.makeText(DownloadImageActivity.this, "Start download", Toast.LENGTH_SHORT).show());
                    Bitmap bm;
                    try {
                        bm = loadImageFromNetwork("https://haycafe.vn/wp-content/uploads/2022/01/hinh-anh-galaxy-vu-tru-dep.jpg");
                        mIvImage.post(new Runnable() {
                            @Override
                            public void run() {
                                mIvImage.setImageBitmap(bm);
                                runOnUiThread(() -> Toast.makeText(DownloadImageActivity.this, "Download finished", Toast.LENGTH_SHORT).show());
                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(DownloadImageActivity.this, "Download failure", Toast.LENGTH_SHORT).show());
                    }
                }
            }).start();
        });
    }

    private Bitmap loadImageFromNetwork(String link) throws IOException {

        URL url = new URL(link);
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        return bmp;
    }
}
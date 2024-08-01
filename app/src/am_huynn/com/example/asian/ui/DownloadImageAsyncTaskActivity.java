package com.example.asian.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.asian.R;
import com.example.asian.async_task.AsyncTaskDowLoad;

public class DownloadImageAsyncTaskActivity extends AppCompatActivity {
    private ImageView mIvImage;
    private Button mBtnDownload;

    private ProgressBar mPbProgressDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_image_async_task);
        initView();
        initListener();
    }

    private void initView() {
        mIvImage = findViewById(R.id.ivImageAsync);
        mBtnDownload = findViewById(R.id.btnDownLoadAsyncTask);
        mPbProgressDownload = findViewById(R.id.pbProgressDownload);
    }

    private void initListener() {
        mBtnDownload.setOnClickListener(view -> {
            AsyncTaskDowLoad asyncTaskDowLoad = new AsyncTaskDowLoad(mIvImage, mPbProgressDownload);
            asyncTaskDowLoad.execute("https://haycafe.vn/wp-content/uploads/2022/01/hinh-anh-galaxy-vu-tru-dep.jpg");
        });

    }
}

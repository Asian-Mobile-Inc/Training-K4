package com.example.asian.issueeight;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.asian.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IssueEightActivity extends AppCompatActivity {
    private static final String IMAGE_URL = "https://haycafe.vn/wp-content/uploads/2022/01/hinh-anh-galaxy-vu-tru-dep.jpg";
    private Button mBtnDownloadThread;
    private Button mBtnDownloadAsyncTask;
    private ImageView mImgDownload;
    private String mNameFile;
    private DownloadManager mDownloadManager;
    private ProgressDialog mProgressDialog;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 11;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, android.content.Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            handleStatusDownload(referenceId, context);
        }
    };

    private class DownloadImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
                mHttpURLConnection.connect();
                int fileLength = mHttpURLConnection.getContentLength();
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
                File file = new File(path);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] data = new byte[1024];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress((int) (total * 100 / fileLength));
                    outputStream.write(data, 0, count);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return BitmapFactory.decodeFile(path);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mProgressDialog.dismiss();
            if (bitmap != null) {
                mImgDownload.setImageBitmap(bitmap);
                Toast.makeText(IssueEightActivity.this, getString(R.string.download_finished), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(IssueEightActivity.this, getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(IssueEightActivity.this);
            mProgressDialog.setTitle("Downloading");
            mProgressDialog.setMessage("Downloading, Please Wait!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_eight);
        initUI();
        initListener();
        askPermission();
        registerBroadcastReceiver();
    }

    private void initUI() {
        mBtnDownloadThread = findViewById(R.id.btnDownloadThread);
        mBtnDownloadAsyncTask = findViewById(R.id.btnDownloadAsyncTask);
        mImgDownload = findViewById(R.id.imgDownload);
    }

    private void initListener() {
        mBtnDownloadThread.setOnClickListener(v -> downloadUsingThread());
        mBtnDownloadAsyncTask.setOnClickListener(v -> downloadUsingAsyncTask());
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void downloadUsingThread() {
        new Thread(() -> downloadImage()).start();
    }

    private void downloadUsingAsyncTask() {
        new DownloadImageAsyncTask().execute(IMAGE_URL);
    }

    private void downloadImage() {
        mNameFile = String.valueOf(System.currentTimeMillis()) + ".jpg";
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(android.net.Uri.parse(IMAGE_URL));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Image Download");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, mNameFile);
        mDownloadManager.enqueue(request);
    }

    private void handleStatusDownload(long referenceId, Context context) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(referenceId);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                Toast.makeText(context, getString(R.string.download_finished), Toast.LENGTH_SHORT).show();
                viewImage();
            } else if (DownloadManager.STATUS_FAILED == cursor.getInt(columnIndex)) {
                Toast.makeText(context, getString(R.string.download_failed), Toast.LENGTH_SHORT).show();
            } else if (DownloadManager.STATUS_RUNNING == cursor.getInt(columnIndex)) {
                Toast.makeText(context, getString(R.string.download_started), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void viewImage() {
        String path = android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + this.mNameFile;
        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(path);
        mImgDownload.setImageBitmap(bitmap);
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}

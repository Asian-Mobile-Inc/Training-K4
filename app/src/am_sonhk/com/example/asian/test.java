//package com.example.asian;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MainActivity extends AppCompatActivity {
//    private static final int REQUEST_PERMISSION_CODE = 1;
//    private Button mBtnRequest;
//    private Button mBtnOpenPMS;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        initUI();
//        initListener();
//    }
//
//    private void initUI() {
//        mBtnRequest = findViewById(R.id.btnRequest);
//        mBtnOpenPMS = findViewById(R.id.btnOpenPms);
//    }
//
//    private void initListener() {
//        mBtnRequest.setOnClickListener(view -> clickRequestPermision());
//        mBtnOpenPMS.setOnClickListener(view -> openSettingPermission());
//    }
//
//    private void clickRequestPermision() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permision Granted123", Toast.LENGTH_SHORT).show();
//            } else {
//                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
//                requestPermissions(permissions, REQUEST_PERMISSION_CODE);
//            }
//        }
//    }
//
//    private void openSettingPermission() {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted456", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}
//

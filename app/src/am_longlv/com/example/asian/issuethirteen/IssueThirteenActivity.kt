package com.example.asian.issuethirteen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.asian.databinding.ActivityIssueThirteenBinding
import com.example.asian.issuethirteen.adapter.TabName
import com.example.asian.issuethirteen.adapter.ViewPagerStorageAdapter
import com.example.asian.issuethirteen.viewmodel.StorageViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class IssueThirteenActivity : AppCompatActivity() {
    private val mBinding: ActivityIssueThirteenBinding by lazy {
        ActivityIssueThirteenBinding.inflate(layoutInflater)
    }
    private val mStorageViewModel: StorageViewModel by viewModels()
    private val mPermissionNameList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mutableListOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
    private val mRequestCodePermissions: Int = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        askPermissions()
    }

    private fun setupTabLayout() {
        val viewPagerAdapter = ViewPagerStorageAdapter(this)
        mBinding.vpStorage.offscreenPageLimit = 1
        mBinding.vpStorage.adapter = viewPagerAdapter
        TabLayoutMediator(
            mBinding.tlStorage, mBinding.vpStorage
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = TabName.getName(position)
        }.attach()
    }

    private fun askPermissions() {
        if (hasPermissions()) {
            setupTabLayout()
        } else {
            ActivityCompat.requestPermissions(
                this,
                mPermissionNameList.toTypedArray(),
                mRequestCodePermissions
            )
        }
    }

    private fun hasPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == mRequestCodePermissions) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupTabLayout()
            } else {
                finish()
            }
        }
    }
}
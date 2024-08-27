package com.example.asian.issuethirteen

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.asian.R
import com.example.asian.databinding.ActivityIssueThirteenBinding
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.databinding.DialogShowProgressBinding
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
    private var isShowMenu: Boolean = false
    private val progressDialog: Dialog by lazy {
        Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_show_progress)
            setCancelable(false)
            window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
    private val dialogProgressBinding: DialogShowProgressBinding by lazy {
        DialogShowProgressBinding.inflate(progressDialog.layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        askPermissions()
        initObserver()
        setupDialog()
    }

    private fun setupDialog() {
        progressDialog.setContentView(dialogProgressBinding.root)
    }

    private fun initObserver() {
        mStorageViewModel.permissionNeededForRename.observe(this) {
            it?.let { intent ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(intent).build()
                    intentResult.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
        mStorageViewModel.listStorageSelected.observe(this) {
            if (it.size == 0) {
                isShowMenu = false
                invalidateMenu()
            } else {
                isShowMenu = true
                invalidateMenu()
            }
        }
        mStorageViewModel.permissionNeededForDelete.observe(this) {
            it?.let { intent ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(intent).build()
                    intentResultDelete.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
        mStorageViewModel.isShowProgressBar.observe(this) {
            it?.let {
                val listsCount = mStorageViewModel.getListItemSelected()?.size
                if (listsCount != null && it < listsCount.minus(2) && it != -1) {
                    if (!progressDialog.isShowing) {
                        progressDialog.show()
                    }
                    dialogProgressBinding.tvProgressLoading.text =
                        getString(R.string.progress_dialog_two_int).format(it + 1, listsCount)
                } else {
                    if (progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }
            }
        }
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

    private val intentResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                mStorageViewModel.renameImageAcp(this)
            }
        }
    private val intentResultDelete =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                mStorageViewModel.deleteAllFile(this)
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_select_more, menu)
        if (isShowMenu) {
            supportActionBar?.title =
                getString(R.string.count_selected_one_int).format(mStorageViewModel.getListItemSelected()?.size)
            menu?.let {
                for (i in 0 until it.size()) {
                    it.getItem(i).setVisible(true)
                }
                if (mStorageViewModel.checkSelected()) {
                    it.getItem(0).title = getString(R.string.un_select_all)
                } else {
                    it.getItem(0).title = getString(R.string.select_all)
                }
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.title = getString(R.string.app_name)
            menu?.let {
                for (i in 0 until it.size()) {
                    it.getItem(i).setVisible(false)
                }
            }
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            (R.id.actionSelectAll) -> {
                if (mStorageViewModel.checkSelected()) {
                    mStorageViewModel.unSelectAll()
                } else {
                    mStorageViewModel.selectAll()
                }
                return true
            }

            (R.id.actionDelete) -> {
                showDialogConfirmDeleteAll(this)
                true
            }

            (android.R.id.home) -> {
                mStorageViewModel.unSelectAll()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showDialogConfirmDeleteAll(context: Context) {
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_confirm)
            setCancelable(false)
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val dialogBinding: DialogConfirmBinding =
            DialogConfirmBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)
        with(dialogBinding) {
            tvDeleteThisItem.text = getString(R.string.delete_all_images)
            btnCancelDelete.setOnClickListener {
                dialog.dismiss()
            }
            btnConfirmDelete.setOnClickListener {
                dialog.dismiss()
                mStorageViewModel.deleteAllFile(context)
            }
        }
        dialog.show()
    }
}

package com.example.asian.issuethirteen.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.asian.R
import com.example.asian.databinding.DialogEditNameBinding
import com.example.asian.databinding.DialogShowImageBinding
import com.example.asian.databinding.FragmentStorageBinding
import com.example.asian.issuethirteen.adapter.StorageAdapter
import com.example.asian.issuethirteen.model.StorageModel
import com.example.asian.issuethirteen.viewmodel.StorageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File


class StorageFragment : Fragment(), StorageAdapter.ItemClickListener {
    private var mTab: Int = 0
    private lateinit var mStorageAdapter: StorageAdapter
    private val mBinding: FragmentStorageBinding by lazy {
        FragmentStorageBinding.inflate(layoutInflater)
    }
    private val mStorageViewModel: StorageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTab = it.getInt("tab", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initObserver()
        setupRecyclerView()
        initData()
        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(tab: Int) =
            StorageFragment().apply {
                arguments = Bundle().apply {
                    putInt("tab", tab)
                }
            }
    }

    private fun setupRecyclerView() {
        mBinding.rvStorage.layoutManager = GridLayoutManager(context, 3)
        mStorageAdapter = StorageAdapter(this)
        mBinding.rvStorage.adapter = mStorageAdapter
    }

    private fun initData() {
        if (mTab == 1) {
            context?.let { mStorageViewModel.getImageFromGallery(it) }
        } else {
            mStorageViewModel.getImageFromRoom()
        }
    }

    private fun initObserver() {
        if (mTab == 1) {
            mStorageViewModel.listStorage.observe(this) {
                mStorageAdapter.submitList(it.toMutableList())
            }
        } else {
            mStorageViewModel.listFavourite.observe(this) {
                mStorageAdapter.submitList(it.toMutableList())
            }
        }
    }

    override fun onItemClick(storage: StorageModel) {
        showDiaLogImage(storage)
    }

    private fun showDiaLogImage(storage: StorageModel) {
        activity?.let {
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_show_image)
            dialog.setCancelable(false)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            var isFav = mStorageViewModel.checkImageInRoom(storage)
            val dialogBinding: DialogShowImageBinding =
                DialogShowImageBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialogBinding.tvDes.text = storage.storageName
            if (isFav) {
                dialogBinding.btnFavourite.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
            } else {
                dialogBinding.btnFavourite.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
            }
            context?.let { it1 ->
                Glide
                    .with(it1)
                    .load(storage.storageUri)
                    .into(dialogBinding.ivStorage)
            }
            dialogBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialogBinding.btnConfirm.setOnClickListener {
                if (isFav && !mStorageViewModel.checkImageInRoom(storage)) {
                    mStorageViewModel.insertImageIntoRoom(storage)
                }
                if (!isFav) {
                    mStorageViewModel.removeImageToRoom(storage)
                    Log.d("androidruntime", storage.storageUri)
                }
                dialog.dismiss()
            }
            dialogBinding.btnEdit.setOnClickListener {
                showDialogEditName(storage,dialog)
            }
            dialogBinding.btnFavourite.setOnClickListener {
                isFav = !isFav
                if (isFav) {
                    dialogBinding.btnFavourite.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                } else {
                    dialogBinding.btnFavourite.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
                }
            }
            dialog.show()
        }
    }

    private fun showDialogEditName(storage: StorageModel,parentDialog: Dialog) {
        activity?.let {
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_edit_name)
            dialog.setCancelable(false)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val dialogBinding: DialogEditNameBinding =
                DialogEditNameBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialogBinding.edtNewName.setText(storage.storageName)
            dialogBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialogBinding.btnConfirm.setOnClickListener {
                if (dialogBinding.edtNewName.text.isEmpty()) {
                    dialogBinding.edtNewName.error = getString(R.string.name_invalid)
                } else {
                    context?.let { it1 ->
                        mStorageViewModel.renameFile(
                            it1,
                            storage,
                            dialogBinding.edtNewName.text.toString()
                        )
                    }
                    parentDialog.dismiss()
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }
}

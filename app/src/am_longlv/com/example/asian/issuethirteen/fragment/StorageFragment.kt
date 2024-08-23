package com.example.asian.issuethirteen.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
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

class StorageFragment : Fragment(), StorageAdapter.ItemClickListener {
    private var tab = 0
    private lateinit var mStorageAdapter: StorageAdapter
    private val mBinding: FragmentStorageBinding by lazy {
        FragmentStorageBinding.inflate(layoutInflater)
    }
    private val mStorageViewModel: StorageViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = it.getInt("tab", 0)
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
        if (tab == 1) {
            context?.let { mStorageViewModel.getImageFromGallery(it) }
        } else {
            mStorageViewModel.getImageFromRoom()
        }
    }

    private fun initObserver() {
        if (tab == 1) {
            mStorageViewModel.listStorage.observe(viewLifecycleOwner) {
                mStorageAdapter.submitList(it.toMutableList())
            }
        } else {
            mStorageViewModel.listFavourite.observe(viewLifecycleOwner) {
                mStorageAdapter.submitList(it.toMutableList())
            }
        }
    }

    private fun showDiaLogImage(storage: StorageModel) {
        activity?.let { activity ->
            val dialog = Dialog(activity).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.dialog_show_image)
                setCancelable(false)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            var isFav = mStorageViewModel.checkImageInRoom(storage)
            val dialogBinding: DialogShowImageBinding =
                DialogShowImageBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(dialogBinding.root)
            with(dialogBinding) {
                tvDes.text = storage.storageName
                if (isFav) {
                    btnFavourite.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
                } else {
                    btnFavourite.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
                }
                context?.let { ct ->
                    Glide
                        .with(ct)
                        .load(storage.storageUri)
                        .into(ivStorage)
                }
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                btnConfirm.setOnClickListener {
                    if (isFav && !mStorageViewModel.checkImageInRoom(storage)) {
                        mStorageViewModel.insertImageIntoRoom(storage)
                    }
                    if (!isFav) {
                        mStorageViewModel.removeImageToRoom(storage)
                    }
                    dialog.dismiss()
                }
                btnEdit.setOnClickListener {
                    showDialogEditName(storage, dialog)
                }
                btnFavourite.setOnClickListener {
                    isFav = !isFav
                    if (isFav) {
                        btnFavourite.setColorFilter(
                            Color.RED,
                            PorterDuff.Mode.MULTIPLY
                        )
                    } else {
                        btnFavourite.setColorFilter(
                            Color.BLACK,
                            PorterDuff.Mode.MULTIPLY
                        )
                    }
                }
            }
            dialog.show()
        }
    }

    private fun showDialogEditName(storage: StorageModel, parentDialog: Dialog) {
        activity?.let { activity ->
            val dialog = Dialog(activity).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.dialog_show_image)
                setCancelable(false)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            val dialogBinding: DialogEditNameBinding =
                DialogEditNameBinding.inflate(dialog.layoutInflater)
            dialog.setContentView(dialogBinding.root)
            with(dialogBinding) {
                edtNewName.setText(storage.storageName.substringBeforeLast("."))
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                btnConfirm.setOnClickListener {
                    if (edtNewName.text.isEmpty()) {
                        edtNewName.error = getString(R.string.name_invalid)
                    } else if (edtNewName.text.toString().contains(".")) {
                        edtNewName.error = getString(R.string.name_invalid)
                    } else {
                        context?.let { ct ->
                            mStorageViewModel.renameFile(
                                ct,
                                storage,
                                edtNewName.text.toString()
                            )
                        }
                        parentDialog.dismiss()
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }

    override fun onItemClick(storage: StorageModel) {
        if (mStorageViewModel.getListItemSelected()?.size != 0) {
            mStorageViewModel.updateListChange(storage)
        } else {
            showDiaLogImage(storage)
        }
    }

    override fun onItemLongClick(storage: StorageModel) {
        mStorageViewModel.updateListChange(storage)
    }
}

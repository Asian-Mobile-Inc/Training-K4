package com.example.asian.retrofit.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.databinding.FragmentImageRetrofitBinding
import com.example.asian.retrofit.adapter.ImageAdapter
import com.example.asian.retrofit.model.ImageModel
import com.example.asian.retrofit.viewmodel.RetrofitViewModel

private const val KEY_BUNDLE = "tab"

class ImageRetrofitFragment : Fragment(), ImageAdapter.ItemClickListener {
    private var tab = 0
    private val mBinding: FragmentImageRetrofitBinding by lazy {
        FragmentImageRetrofitBinding.inflate(layoutInflater)
    }
    private val mImageAdapter: ImageAdapter by lazy {
        ImageAdapter(this)
    }
    private val mViewModel: RetrofitViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = it.getInt(KEY_BUNDLE, 0)
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
            ImageRetrofitFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_BUNDLE, tab)
                }
            }
    }

    private fun setupRecyclerView() {
        mBinding.rvImageRetrofit.layoutManager = GridLayoutManager(context, 3)
        mBinding.rvImageRetrofit.adapter = mImageAdapter
    }

    private fun initData() {
        if (tab == 0) {
            context?.let { mViewModel.fetchImages() }
        } else {
            mViewModel.getImageFromRoom()
        }
    }

    private fun initObserver() {
        if (tab == 0) {
            mViewModel.listImage.observe(viewLifecycleOwner) {
                mImageAdapter.submitList(it.toMutableList())
            }
        } else {
            mViewModel.listFavourite.observe(viewLifecycleOwner) {
                mImageAdapter.submitList(it.toMutableList())
            }
        }
    }

    private fun showDialogConfirmDelete(imageModel: ImageModel) {
        this.context?.let {
            val dialogBinding: DialogConfirmBinding =
                DialogConfirmBinding.inflate(layoutInflater)
            val dialog = Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setContentView(dialogBinding.root)
            }
            with(dialogBinding) {
                btnConfirmDelete.setOnClickListener {
                    mViewModel.deleteImage(imageModel.imageId)
                    dialog.dismiss()
                }
                btnCancelDelete.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    override fun onItemClick(imageModel: ImageModel) {
        showDialogConfirmDelete(imageModel)
    }

    override fun onItemLongClick(imageModel: ImageModel) {
    }
}
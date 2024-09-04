package com.example.asian.retrofit.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.asian.R
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.databinding.FragmentImageRetrofitBinding
import com.example.asian.retrofit.adapter.ImageAdapter
import com.example.asian.retrofit.model.ImageModel
import com.example.asian.retrofit.utils.Constant
import com.example.asian.retrofit.viewmodel.RetrofitViewModel

private const val KEY_BUNDLE = "tab"

class ImageRetrofitFragment : Fragment(), ImageAdapter.ItemClickListener {
    private var mTab = 0
    private var mIsLoading = false
    private val mBinding: FragmentImageRetrofitBinding by lazy {
        FragmentImageRetrofitBinding.inflate(layoutInflater)
    }
    private val mImageAdapter: ImageAdapter by lazy {
        ImageAdapter(this, mTab)
    }
    private val mViewModel: RetrofitViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTab = it.getInt(KEY_BUNDLE, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        initObserver()
        setupRecyclerView()
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
        if (mTab == 0) {
            lazyLoad()
        }
    }

    private fun initObserver() {
        when (mTab) {
            0 -> {
                mViewModel.listImage.observe(viewLifecycleOwner) {
                    mImageAdapter.submitList(it.toMutableList())
                }
                mViewModel.statusRetrofitCallback.observe(this) {
                    it?.let { sub ->
                        if (sub == Constant.STATUS_CODE_OK) {
                            mIsLoading = false
                        }
                    }
                }
            }

            1 -> {
                mViewModel.listLocal.observe(viewLifecycleOwner) {
                    mImageAdapter.submitList(it.toMutableList())
                }
            }

            else -> {
                mViewModel.listFavourite.observe(viewLifecycleOwner) {
                    mImageAdapter.submitList(it.toMutableList())
                }
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

    private fun lazyLoad() {
        mBinding.rvImageRetrofit.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
                if (!mIsLoading) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition()
                        == (mViewModel.getListItemRetrofit().size - 1)
                    ) {
                        mViewModel.loadMore()
                        mIsLoading = true
                    }
                }
            }
        })
    }

    override fun onItemClick(imageModel: ImageModel) {
        showDialogConfirmDelete(imageModel)
    }

    override fun onItemLongClick(imageModel: ImageModel) {
    }

    override fun onBtnFavouriteClick(imageModel: ImageModel) {
        mViewModel.handlerClickFavourite(imageModel)
    }

    override fun onBtnDownloadClick(imageModel: ImageModel) {
        if (!imageModel.isDownloaded) {
            context?.let { mViewModel.downloadFile(imageModel, it) }
        } else {
            Toast.makeText(
                context,
                getString(R.string.image_exists),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

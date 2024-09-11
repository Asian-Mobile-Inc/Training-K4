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
import com.bumptech.glide.Glide
import com.example.asian.R
import com.example.asian.databinding.DialogConfirmBinding
import com.example.asian.databinding.DialogShowImageRetrofitBinding
import com.example.asian.databinding.FragmentImageRetrofitBinding
import com.example.asian.retrofit.adapter.ImageAdapter
import com.example.asian.retrofit.adapter.TabName
import com.example.asian.retrofit.model.ImageModel
import com.example.asian.retrofit.utils.Constant
import com.example.asian.retrofit.viewmodel.RetrofitViewModel

private const val KEY_BUNDLE = "tab"

class ImageRetrofitFragment : Fragment(), ImageAdapter.ItemClickListener {
    private var mTab = TabName.RETROFIT.position
    private val binding: FragmentImageRetrofitBinding by lazy {
        FragmentImageRetrofitBinding.inflate(layoutInflater)
    }
    private val adapter: ImageAdapter by lazy {
        ImageAdapter(this, mTab)
    }
    private val viewModel: RetrofitViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTab = it.getInt(KEY_BUNDLE, TabName.RETROFIT.position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setupRecyclerView()
        initObserver()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(tab: Int) = ImageRetrofitFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_BUNDLE, tab)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvImageRetrofit.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = this@ImageRetrofitFragment.adapter
        }
        if (mTab == TabName.RETROFIT.position) {
            loadMore()
        }
    }

    private fun initObserver() {
        when (mTab) {
            TabName.RETROFIT.position -> {
                viewModel.listImage.observe(viewLifecycleOwner) {
                    adapter.submitList(it.toMutableList())
                }
                viewModel.statusRetrofitCallback.observe(viewLifecycleOwner) {
                    when (it) {
                        Constant.STATUS_CODE_HIDE_DIALOG_REFRESH -> {
                            viewModel.isRefresh = true
                            binding.rvImageRetrofit.scrollToPosition(0)
                        }

                        Constant.STATUS_CODE_UPLOAD_SUCCESS -> {
                            with(binding.rvImageRetrofit) {
                                viewModel.isRefresh = true
                                val gridLayoutManager = layoutManager as GridLayoutManager
                                scrollToPosition(gridLayoutManager.findFirstVisibleItemPosition())
                            }
                        }
                    }
                }
            }

            TabName.LOCAL.position -> {
                viewModel.listLocal.observe(viewLifecycleOwner) {
                    adapter.submitList(it.toMutableList())
                }
            }

            else -> {
                viewModel.listFavourite.observe(viewLifecycleOwner) {
                    adapter.submitList(it.toMutableList())
                }
            }
        }
    }

    private fun showDialogConfirmDelete(imageModel: ImageModel) {
        this.context?.let {
            val dialogBinding = DialogConfirmBinding.inflate(layoutInflater)
            val dialog = Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setContentView(dialogBinding.root)
            }
            with(dialogBinding) {
                btnConfirmDelete.setOnClickListener {
                    viewModel.deleteImage(imageModel.imageId)
                    dialog.dismiss()
                }
                btnCancelDelete.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.show()
        }
    }

    private fun loadMore() {
        binding.rvImageRetrofit.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
                val listItem = viewModel.getListItemRetrofit()
                if (!viewModel.isLoading) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == (listItem.size - 1)) {
                        viewModel.loadMore()
                    }
                }
                if (!viewModel.isRefresh) {
                    if (gridLayoutManager != null && gridLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 && listItem.size > 0) {
                        viewModel.refresh()
                    }
                } else {
                    viewModel.isRefresh = false
                }
            }
        })
    }

    override fun onItemClick(imageModel: ImageModel) {
        showDiaLogImage(imageModel)
    }

    override fun onItemLongClick(imageModel: ImageModel) {
        showDialogConfirmDelete(imageModel)
    }

    override fun onBtnFavouriteClick(imageModel: ImageModel) {
        viewModel.handleClickFavourite(imageModel)
    }

    override fun onBtnDownloadClick(imageModel: ImageModel) {
        if (!imageModel.isDownloaded) {
            context?.let { viewModel.downloadImage(imageModel, it) }
        } else {
            Toast.makeText(
                context, getString(R.string.image_exists), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showDiaLogImage(imageModel: ImageModel) {
        activity?.let { activity ->
            val dialogBinding: DialogShowImageRetrofitBinding =
                DialogShowImageRetrofitBinding.inflate(layoutInflater)
            val dialog = Dialog(activity).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(dialogBinding.root)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            with(dialogBinding) {
                context?.let { ct ->
                    Glide.with(ct).load(imageModel.url).into(ivStorage)
                }
            }
            dialog.show()
        }
    }
}

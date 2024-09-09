package com.example.asian.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.asian.R
import com.example.asian.adapter.GridSpacingItemDecoration
import com.example.asian.adapter.PicturesAdapter
import com.example.asian.databinding.DialogShowImageBinding
import com.example.asian.databinding.FragmentImagesBinding
import com.example.asian.listeners.ScrollLoadMoreListener
import com.example.asian.model.Picture
import com.example.asian.viewmodel.ImagesViewModel

class ImagesFragment(private val position: Int, onDownLoad: (Picture) -> Unit) : Fragment() {
    private val binding: FragmentImagesBinding by lazy {
        FragmentImagesBinding.inflate(layoutInflater)
    }

    private val viewModel: ImagesViewModel by activityViewModels()

    private val picturesAdapter by lazy {
        when (position) {
            0 -> PicturesAdapter(onItemClick, onFavorite, onDownLoad, true)
            else -> PicturesAdapter(onItemClick, onFavorite, onDownLoad, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        initControls()
        initObserver()
        initRefreshListener()
        return binding.root
    }

    private fun initControls() {
        binding.rvPictures.apply {
            adapter = picturesAdapter
            val gridLayoutManager = GridLayoutManager(context, 3)
            layoutManager = gridLayoutManager
            addItemDecoration(GridSpacingItemDecoration(14))
            itemAnimator = null
            addOnScrollListener(object : ScrollLoadMoreListener(gridLayoutManager) {
                override fun loadMoreItem() {
                    viewModel.onLoadMore()
                }

                override val isLoading: Boolean
                    get() = viewModel.isLoadMore.value ?: false
                override val isLastPage: Boolean
                    get() = viewModel.isLastPage
            })
        }
    }

    private fun initRefreshListener() {
        binding.srlRefreshPictures.apply {
            when (position) {
                0 -> setOnRefreshListener {
                    viewModel.getAllPicture()
                    isRefreshing = false
                }
                1 -> isEnabled = false
                2 -> isEnabled = false
            }
        }
    }

    private fun initObserver() {
        when (position) {
            0 -> {
                viewModel.isLoadingNetwork.observe(viewLifecycleOwner) {
                    binding.pbProgressNetwork.isVisible = it
                    binding.rvPictures.isVisible = !it
                }
                viewModel.pictures.observe(viewLifecycleOwner) {
                    picturesAdapter.setData(it)
                }
            }
            1 -> {
                viewModel.localPictures.observe(viewLifecycleOwner) {
                    picturesAdapter.setData(it)
                }
            }
            2 -> {
                viewModel.favoritePictures.observe(viewLifecycleOwner) {
                    picturesAdapter.setData(it)
                }
            }
        }

        viewModel.isLoadMore.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.pbLoadMore.visibility = View.VISIBLE
                false -> binding.pbLoadMore.visibility = View.GONE
            }
        }
    }

    private val onItemClick: (Picture) -> Unit = {
        val dialogBinding = DialogShowImageBinding.inflate(LayoutInflater.from(context))
        AlertDialog.Builder(context).create().apply {
            setView(dialogBinding.root)
            Glide.with(context).load(it.url).into(dialogBinding.ivPictureDialog)
            dialogBinding.btnDelete.isVisible = (position == 0)
            dialogBinding.btnDelete.setOnClickListener { _ ->
                dismiss()
                viewModel.dialogLoading.startLoadingDialog(context)
                viewModel.deleteImage(it)
            }
        }.show()
    }

    private val onFavorite: (Picture) -> Unit = {
        when (position) {
            0 -> viewModel.favoriteNetworkPicture(it)
            1 -> viewModel.favoriteLocalPicture(it)
            2 -> viewModel.unFavoritePicture(it)
        }
    }

    private val onItemDelete: (Picture) -> Unit = {
        context?.let { context ->
            val dialogBuilder = AlertDialog.Builder(context)
            with(dialogBuilder) {
                setMessage(resources.getText(R.string.do_you_want_delete_image))
                setPositiveButton(resources.getText(R.string.yes)) { _, _ ->
                    viewModel.deleteImage(it)
                }
                setNegativeButton(resources.getText(R.string.no)) { _, _ -> }
            }.create().show()
        }
    }
}

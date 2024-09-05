package com.example.asian.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.asian.R
import com.example.asian.adapter.GridSpacingItemDecoration
import com.example.asian.adapter.PicturesAdapter
import com.example.asian.databinding.FragmentImagesBinding
import com.example.asian.model.Picture
import com.example.asian.viewmodel.ImagesViewModel

class ImagesFragment(private val position: Int) : Fragment() {
    private val binding: FragmentImagesBinding by lazy {
        FragmentImagesBinding.inflate(layoutInflater)
    }

    private val viewModel: ImagesViewModel by activityViewModels()

    private val picturesAdapter by lazy {
        when (position) {
            0 -> PicturesAdapter(onItemClick, onFavorite, true)
            else -> PicturesAdapter(onItemClick, onFavorite, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        initControls()
        initObserver()
        return binding.root
    }

    private fun initControls() {
        binding.rvPictures.apply {
            adapter = picturesAdapter
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(GridSpacingItemDecoration(14))
            itemAnimator = null
        }
    }

    private fun initObserver() {
        when (position) {
            0 -> {
                viewModel.getAllPicture()
                viewModel.isLoadingNetwork.observe(viewLifecycleOwner) {
                    binding.pbProgressNetwork.isVisible = it
                }
                viewModel.pictures.observe(viewLifecycleOwner) {
                    picturesAdapter.setData(it)
                    binding.rvPictures.scrollToPosition(0)
                }
            }
            1 -> {
                viewModel.getLocalPictures()
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
    }

    private val onItemClick: (Picture) -> Unit = {}

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

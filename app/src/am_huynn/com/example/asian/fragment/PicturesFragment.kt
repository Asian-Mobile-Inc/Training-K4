package com.example.asian.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.asian.R
import com.example.asian.adapter.PicturesAdapter
import com.example.asian.databinding.DialogShowImageBinding
import com.example.asian.databinding.FragmentPicturesBinding
import com.example.asian.model.Picture
import com.example.asian.viewmodel.StorageViewModel


class PicturesFragment(private val position: Int) : Fragment() {
    private val binding: FragmentPicturesBinding by lazy {
        FragmentPicturesBinding.inflate(layoutInflater)
    }

    private val picturesAdapter: PicturesAdapter by lazy {
        PicturesAdapter(onClick)
    }

    private val viewModel: StorageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        initControl()
        initObserver()
        return binding.root
    }

    private fun initControl() {
        binding.rvPictures.layoutManager = GridLayoutManager(context, 3)
        binding.rvPictures.adapter = picturesAdapter
        binding.rvPictures.itemAnimator = null
    }

    private fun initObserver() {
        viewModel.pictures.observe(viewLifecycleOwner) {
            if (position == 0) {
                picturesAdapter.setData(it)
            } else {
                val list = mutableListOf<Picture>()
                for (i in it) {
                    if (i.favorite) {
                        list.add(i)
                    }
                }
                picturesAdapter.setData(list)
            }
        }
    }

    private val onClick: (Picture) -> Unit = {
        val dialog = AlertDialog.Builder(context).create()
        val dialogBinding = DialogShowImageBinding.inflate(LayoutInflater.from(context))

        dialog.apply {
            setView(dialogBinding.root)
            var favorite: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
                value = it.favorite
            }
            with(dialogBinding) {
                favorite.observe(viewLifecycleOwner) { favorite ->
                    if (favorite) {
                        btnFavorite.setImageResource(R.drawable.ic_favorite)
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_un_favorite)
                    }
                }
                Glide.with(context).load(it.uri).into(dialogBinding.ivPictureDialog)
                tvNamePicture.text = it.name
                btnFavorite.setOnClickListener {
                    favorite.value = !(favorite.value ?: false)
                }
                btnCancel.setOnClickListener { dismiss() }
                btnSave.setOnClickListener { _ ->
                    it.favorite = favorite.value ?: false
                    viewModel.savePicture(it)
                    dismiss()
                }
            }
        }.show()
    }
}
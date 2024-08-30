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
import com.example.asian.adapter.PictureDecoration
import com.example.asian.adapter.PicturesAdapter
import com.example.asian.databinding.DialogEditNameBinding
import com.example.asian.databinding.DialogShowImageBinding
import com.example.asian.databinding.FragmentPicturesBinding
import com.example.asian.model.Picture
import com.example.asian.viewmodel.StorageViewModel

class PicturesFragment(
    private val position: Int, private val onEdit: (Picture, String) -> Unit
) : Fragment() {
    private val binding: FragmentPicturesBinding by lazy {
        FragmentPicturesBinding.inflate(layoutInflater)
    }

    private val picturesAdapter: PicturesAdapter by lazy {
        PicturesAdapter(onClickPicture, onLongClickPicture)
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
        binding.rvPictures.addItemDecoration(PictureDecoration(10))
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

    private val onLongClickPicture: (Picture) -> Unit = {
        viewModel.selectedPicture(it)
    }

    private val onClickPicture: (Picture) -> Unit = {
        if ((viewModel.listSelected.value?.size ?: 0) > 0) {
            onLongClickPicture(it)
        } else {
            showDialogDetailPicture(it)
        }
    }

    private fun showDialogDetailPicture(picture: Picture) {
        val dialog = AlertDialog.Builder(context).create()
        val dialogBinding = DialogShowImageBinding.inflate(LayoutInflater.from(context))

        dialog.apply {
            setView(dialogBinding.root)
            val favorite: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
                value = picture.favorite
            }
            with(dialogBinding) {
                favorite.observe(viewLifecycleOwner) { favorite ->
                    if (favorite) {
                        btnFavorite.setImageResource(R.drawable.ic_favorite)
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_un_favorite)
                    }
                }
                Glide.with(context).load(picture.path).into(dialogBinding.ivPictureDialog)
                tvNamePicture.text = picture.name
                btnFavorite.setOnClickListener {
                    favorite.value = !(favorite.value ?: false)
                }
                btnCancel.setOnClickListener { dismiss() }
                btnSave.setOnClickListener {
                    picture.favorite = favorite.value ?: false
                    viewModel.savePicture(picture)
                    dismiss()
                }
                btnEdit.setOnClickListener {
                    showDialogEdit(picture)
                    dismiss()
                }
            }
        }.show()
    }

    private fun showDialogEdit(picture: Picture) {
        val dialog = AlertDialog.Builder(context).create()
        val dialogBinding = DialogEditNameBinding.inflate(LayoutInflater.from(context))
        dialog.apply {
            setView(dialogBinding.root)
            with(dialogBinding) {
                edtNameImage.setText(picture.name.substring(0, picture.name.indexOf(".")))
                btnCancel.setOnClickListener { dismiss() }
                btnSave.setOnClickListener {
                    if (edtNameImage.text.isEmpty()) {
                        edtNameImage.error = getString(R.string.please_not_empty)
                    } else {
                        dismiss()
                        onEdit(picture, edtNameImage.text.toString())
                    }
                }
            }
        }.show()
    }
}

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
                Glide.with(context).load(it.path).into(dialogBinding.ivPictureDialog)
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
                btnEdit.setOnClickListener { _ ->
                    showDialogEdit(it)
                    dismiss()
//                    if (activity is StorageActivity) {
//                        (activity as StorageActivity).edit(it)
//                    }

//                    val cv = ContentValues()
//                    cv.put(MediaStore.Files.FileColumns.DISPLAY_NAME, "abc")
//                    context.contentResolver.update(
//                        Uri.parse(it.uri), cv, "${MediaStore.Video.Media._ID}=${it.id}", null
//                    )

//                    val file = File(it.path)
//                    val onlyPath = file.parentFile.absolutePath
//
//                    var ext = (file.absolutePath)
//                    ext = ext.substring(ext.lastIndexOf("."))
//                    val newPath = "$onlyPath/abc${ext}"
//                    val newFile = File(newPath)
//                    Log.e("TAG", file.renameTo(newFile).toString() )
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
//                        if (activity is StorageActivity) {
//                            picture.name = edtNameImage.text.toString()
//                            (activity as StorageActivity).edit(picture)
//                        }
                    }
                }
            }
        }.show()
    }
}
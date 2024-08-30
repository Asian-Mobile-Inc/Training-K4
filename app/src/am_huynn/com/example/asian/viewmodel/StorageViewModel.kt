package com.example.asian.viewmodel

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.model.Picture
import com.example.asian.model.PictureDataSource
import com.example.asian.repository.PictureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StorageViewModel(private val app: Application) : AndroidViewModel(app) {
    private val pictureRepository: PictureRepository = PictureRepository(app)
    private var _pictures = MutableLiveData<MutableList<Picture>>().apply {
        value = mutableListOf()
    }
    private var _listSelected = MutableLiveData<MutableList<Picture>>().apply {
        value = mutableListOf()
    }
    private var _listFavorite = mutableListOf<Picture>()
    val listSelected = _listSelected
    val pictures: LiveData<MutableList<Picture>> = _pictures
    private var pictureEdit: Picture? = null
    private var newName: String? = null


    fun setPictureEdit(picture: Picture) {
        pictureEdit = picture
    }

    fun setNewName(newName: String) {
        this.newName = newName
    }

    fun loadAllImage() {
        viewModelScope.launch(Dispatchers.IO) {
            _pictures.postValue(pictureRepository.loadAllImage())
            _listFavorite = pictureRepository.getAllFavorite()
        }
    }

    fun confirmEditName() {
        pictureEdit?.let {
            val cv = ContentValues()
            val ext = it.name.substring(it.name.indexOf("."), it.name.length)
            newName += ext
            cv.put(MediaStore.Files.FileColumns.DISPLAY_NAME, newName)
            app.contentResolver.update(
                Uri.parse(it.uri), cv, "${MediaStore.Video.Media._ID}=${it.id}", null
            )
            updateUiPicture(it.id, newName, null, null)
        }
    }

    fun deletePictures() {
        viewModelScope.launch(Dispatchers.IO) {
            listSelected.value?.let {
                repeat(it.size) { e ->
                    deleteUiPicture(it[e])
                    if (_listFavorite.map { element -> element.id }.contains(it[e].id)) {
                        _listFavorite.remove(it[e])
                        pictureRepository.deleteRoomPicture(it[e])
                    }
                }
            }
            _listSelected.postValue(mutableListOf())
        }
    }

    fun deleteExternalStorage() {
        _listSelected.value?.let {
            repeat(it.size) { index ->
                app.contentResolver.delete(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "${MediaStore.Images.Media._ID} = ?",
                    arrayOf(it[index].id.toString())
                )
            }
        }
    }

    fun cancelSelected() {
        _listSelected.value?.let {
            selectedUiPictures(it.map { e -> e.id }.toMutableList(), false)
            _listSelected.value = mutableListOf()
        }
    }

    fun selectAll() {
        _pictures.value?.let {
            val a = mutableListOf<Picture>()
            a.addAll(it)
            _listSelected.postValue(a)
            selectedUiPictures(a.map { e -> e.id }.toMutableList(), true)
        }
    }

    private fun updateUiPicture(id: Long, name: String?, favorite: Boolean?, isSelected: Boolean?) {
        _pictures.value?.let {
            for (i in it) {
                if (i.id == id) {
                    if (name != null) i.name = name
                    if (favorite != null) i.favorite = favorite
                    if (isSelected != null) i.isSelected = isSelected
                    break
                }
            }
            _pictures.postValue(it)
        }
    }

    private fun selectedUiPictures(listId: MutableList<Long>, isSelected: Boolean) {
        _pictures.value?.let {
            for (i in it) {
                if (listId.contains(i.id)) {
                    i.isSelected = isSelected
                }
            }
            _pictures.postValue(it)
        }
    }

    private fun deleteUiPicture(picture: Picture) {
        _pictures.value?.let {
            it.remove(picture)
            _pictures.postValue(it)
        }
    }

    fun savePicture(picture: Picture) {
        viewModelScope.launch(Dispatchers.IO) {
            if (picture.favorite) {
                _pictures.value?.let {
                    pictureRepository.insertRoomPicture(picture,
                        it,
                        object : PictureDataSource.InsertDataCallback {
                            override fun insert() {
                                it.add(picture)
                                _pictures.postValue(it)
                            }

                            override fun update() {
                                for (i in it) {
                                    if (i.id == picture.id) {
                                        i.favorite = picture.favorite
                                        break
                                    }
                                }
                                _pictures.postValue(it)
                                _listFavorite.add(picture)
                            }
                        })
                }
            } else {
                pictureRepository.deleteRoomPicture(picture)
                updateUiPicture(picture.id, null, false, null)
                _listFavorite.remove(picture)
            }
        }
    }

    fun selectedPicture(picture: Picture) {
        if (picture.isSelected) {
            println("aa")
            updateUiPicture(picture.id, null, null, false)
            val result = _listSelected.value
            result?.remove(picture)
            _listSelected.postValue(result)
        } else {
            updateUiPicture(picture.id, null, null, true)
            val result = _listSelected.value
            result?.add(picture)
            _listSelected.value = result
        }
    }
}

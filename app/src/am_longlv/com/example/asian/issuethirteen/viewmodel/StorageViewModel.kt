package com.example.asian.issuethirteen.viewmodel

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.issuethirteen.database.repository.StorageRepository
import com.example.asian.issuethirteen.model.StorageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class StorageViewModel(application: Application) : AndroidViewModel(application) {
    private val storageRepository: StorageRepository = StorageRepository(application)
    private var mListStorage = MutableLiveData<MutableList<StorageModel>>()
    internal var listStorage: LiveData<MutableList<StorageModel>> = mListStorage
    private var mListFavourite = MutableLiveData<MutableList<StorageModel>>()
    internal var listFavourite: LiveData<MutableList<StorageModel>> = mListFavourite

    init {
        mListStorage.value = mutableListOf()
        mListFavourite.value = mutableListOf()
    }

    internal fun getImageFromRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            mListFavourite.postValue(storageRepository.getAllStorage())
        }
    }

    internal fun getImageFromGallery(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            fetchImages(context)
        }
    }

    internal fun insertImageIntoRoom(storageModel: StorageModel) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepository.insertStorage(storageModel)
        }
        mListFavourite.value?.let {
            it.add(storageModel)
            mListFavourite.value = it
        }
    }

    internal fun removeImageToRoom(storageModel: StorageModel) {
        mListFavourite.value?.let {
            val model = it.find { it1 ->
                it1.storageUri.trim() == storageModel.storageUri.trim()
            }
            viewModelScope.launch(Dispatchers.IO) {
                model?.let { it1 -> storageRepository.deleteStorage(it1) }
            }
            it.remove(storageModel)
            mListFavourite.value = it
        }
    }

    internal fun checkImageInRoom(storageModel: StorageModel): Boolean {
        mListFavourite.value?.let {
            val index = it.firstOrNull { itChild ->
                itChild.storageUri == storageModel.storageUri
            }
            if (index != null) {
                return true
            }
        }
        return false
    }

    fun saveImageToApp(context: Context, storageModel: StorageModel): File {
        val baseDir = context.filesDir.absolutePath
        val fileName: String = storageModel.storageName
        val filePath = baseDir + File.separator + fileName
        lateinit var bitmap: Bitmap
        val file = File(filePath)
        try {
            if (!file.exists()) {
                val out = FileOutputStream(filePath)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.close()
            }
        } catch (_: Exception) {
        }
        return file
    }

    internal fun renameFile(context: Context, storage: StorageModel, newName: String): Boolean {
        val dirs = storage.storageUri.split(File.separator)
        var filePath = ""
        for (dir in 0 until dirs.size - 1) {
            filePath += "${dirs[dir]}${File.separator}"
        }
        val from = File(storage.storageUri)
        var extension: String = from.absolutePath
        extension = extension.substring(extension.lastIndexOf("."))
        val values = ContentValues(2)
        values.put(MediaStore.Images.Media.TITLE, newName)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, newName + extension)
        val to = File("$filePath$newName$extension")
        if (from.exists()) {
            if (from.renameTo(to)) {
                MediaScannerConnection.scanFile(
                    context, arrayOf(to.absolutePath, from.absolutePath),
                    null, MediaScannerConnection.OnScanCompletedListener { path, uri ->
                        Log.d("androidruntime", "mediaConnection $path $uri")
                    }
                )
                mListStorage.value?.let {
                    val index = it.indexOfFirst { it1 ->
                        it1.storageUri.trim() == storage.storageUri
                    }
                    if (index != -1) {
                        val storageModel = StorageModel("$newName.jpg", to.absolutePath)
                        storageModel.storageId = storage.storageId
                        it[index] = storageModel
                    }
                    mListStorage.value = it
                }
                mListFavourite.value?.let {
                    val index = it.indexOfFirst { it1 ->
                        it1.storageUri.trim() == storage.storageUri
                    }
                    if (index != -1) {
                        val storageModel = StorageModel(newName, to.absolutePath)
                        storageModel.storageId = storage.storageId
                        it[index] = storageModel
                        updateNameStorage(storage, storageModel)
                    }
                    mListFavourite.value = it
                }
            } else {
                MediaScannerConnection.scanFile(
                    context, arrayOf(storage.storageUri),
                    null, null
                )
            }
        }
        return true
    }

    private fun updateNameStorage(storage: StorageModel, newStorage: StorageModel) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepository.updateNameStorage(storage, newStorage)
        }
    }

    private fun renameImage(title: String, file: File, context: Context): Boolean {
        try {
            var extension: String = file.absolutePath
            extension = extension.substring(extension.lastIndexOf("."))
            val values = ContentValues(2)
            values.put(MediaStore.Images.Media.TITLE, title)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, title + extension)
            context.contentResolver.update(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values,
                MediaStore.MediaColumns.DATA + "=?", arrayOf<String>(file.absolutePath)
            )
            Log.d("androidruntime", "===========")
            return true
        } catch (e: Exception) {
            Log.d("androidruntime", "+++++++++$e")
        }
        return false
    }

    private fun fetchImages(context: Context): MutableList<StorageModel> {
        val imageList: MutableList<StorageModel> = mutableListOf()
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
        )
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            columns,
            null,
            null,
            MediaStore.Images.Media.DEFAULT_SORT_ORDER
        )
        cursor?.let {
            for (i in 0 until cursor.count) {
                cursor.moveToPosition(i)
                val dataColumnIndex =
                    cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val name = cursor.getString(dataColumnIndex).toString().substringAfterLast("/")
                val uri = Uri.parse(cursor.getString(dataColumnIndex).toString())
                val storageModel = StorageModel(name, uri.toString())
                imageList.add(storageModel)
                imageList.let {
                    mListStorage.postValue(it)
                }
            }
        }
        cursor?.close()
        return imageList
    }
}

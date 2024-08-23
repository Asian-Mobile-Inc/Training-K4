package com.example.asian.issuethirteen.viewmodel

import android.app.Application
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.issuethirteen.database.repository.StorageRepository
import com.example.asian.issuethirteen.model.StorageModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class StorageViewModel(application: Application) : AndroidViewModel(application) {
    private val storageRepository: StorageRepository = StorageRepository(application)
    private var mListStorage = MutableLiveData<MutableList<StorageModel>>()
    internal var listStorage: LiveData<MutableList<StorageModel>> = mListStorage
    private var mListFavourite = MutableLiveData<MutableList<StorageModel>>()
    internal var listFavourite: LiveData<MutableList<StorageModel>> = mListFavourite
    private val mPermissionNeededForRename = MutableLiveData<IntentSender?>()
    internal val permissionNeededForRename: LiveData<IntentSender?> = mPermissionNeededForRename
    private val mPermissionNeededForDelete = MutableLiveData<IntentSender>()
    internal val permissionNeededForDelete: LiveData<IntentSender> =
        mPermissionNeededForDelete
    private val mListStorageSelected = MutableLiveData<MutableList<StorageModel>>()
    internal val listStorageSelected: LiveData<MutableList<StorageModel>> = mListStorageSelected
    private lateinit var pendingDeleteImage: StorageModel
    private lateinit var mUri: Uri
    private lateinit var mNewName: String
    private lateinit var mNewFile: File

    init {
        mListStorage.value = mutableListOf()
        mListFavourite.value = mutableListOf()
        mListStorageSelected.value = mutableListOf()
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
            val model = it.find { sub ->
                sub.storageUri.trim() == storageModel.storageUri.trim()
            }
            it.remove(model)
            mListFavourite.value = it
            viewModelScope.launch(Dispatchers.IO) {
                model?.let { m -> storageRepository.deleteStorage(m) }
            }
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

    private fun updateNameStorage(storage: StorageModel, newStorage: StorageModel) {
        viewModelScope.launch(Dispatchers.IO) {
            storageRepository.updateNameStorage(storage, newStorage)
        }
    }

    internal fun renameFile(context: Context, storage: StorageModel, newName: String) {
        val dirs = storage.storageUri.split(File.separator)
        var filePath = ""
        for (dir in 0 until dirs.size - 1) {
            filePath += "${dirs[dir]}${File.separator}"
        }
        val from = File(storage.storageUri)
        var extension: String = from.absolutePath
        extension = extension.substring(extension.lastIndexOf("."))
        val to = File("$filePath$newName$extension")
        mNewName = newName
        mNewFile = to
        pendingDeleteImage = storage
        viewModelScope.launch {
            if (from.exists()) {
                if (renameImage(from, to, context)) {
                    MediaScannerConnection.scanFile(
                        context, arrayOf(to.absolutePath, from.absolutePath),
                        null
                    ) { _, _ -> }
                } else {
                    MediaScannerConnection.scanFile(
                        context, arrayOf(storage.storageUri),
                        null, null
                    )
                }
            }
        }
    }

    private fun renameImage(file: File, to: File, context: Context): Boolean {
        mUri = ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            getRealIDFromURI(Uri.parse(file.absolutePath), context)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val values = ContentValues()
                values.clear()
                values.put(MediaStore.Images.Media.DISPLAY_NAME, mNewName)
                context.contentResolver.update(
                    mUri,
                    values, null, null
                )
                notifyListRenameChange()
            } catch (_: Exception) {
                val pi = MediaStore.createWriteRequest(context.contentResolver, mutableListOf(mUri))
                mPermissionNeededForRename.value = pi.intentSender
            }
        } else {
            file.renameTo(to)
            notifyListRenameChange()
        }
        return true
    }

    internal fun renameImageAcp(context: Context) {
        val values = ContentValues()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            values.clear()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, mNewName)
            viewModelScope.launch {
                context.contentResolver.update(
                    mUri,
                    values, null, null
                )
            }
            notifyListRenameChange()
        }
    }

    private fun fetchImages(context: Context): MutableList<StorageModel> {
        val imageList: MutableList<StorageModel> = mutableListOf()
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE
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
                val dataRealId = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val name = cursor.getString(dataColumnIndex).toString().substringAfterLast("/")
                val uri = Uri.parse(cursor.getString(dataColumnIndex).toString())
                val storageModel = StorageModel(name, uri.toString(), cursor.getLong(dataRealId))
                imageList.add(storageModel)
                imageList.let {
                    mListStorage.postValue(it)
                }
            }
        }
        cursor?.close()
        return imageList
    }

    private fun getRealIDFromURI(contentUri: Uri, context: Context): Long {
        var path: Long = 0
        val columns = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA
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
                val dataID = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val uri = Uri.parse(cursor.getString(dataColumnIndex).toString())
                if (uri.equals(contentUri)) {
                    path = cursor.getLong(dataID)
                }
            }
        }
        cursor?.close()
        return path
    }

    private fun notifyListRenameChange() {
        var extension: String = pendingDeleteImage.storageUri
        extension = extension.substring(extension.lastIndexOf("."))
        mListStorage.value?.let {
            val index = it.indexOfFirst { sub ->
                sub.storageUri.trim() == pendingDeleteImage.storageUri
            }
            if (index != -1) {
                val storageModel = StorageModel(
                    "$mNewName$extension",
                    mNewFile.absolutePath,
                    pendingDeleteImage.realId
                )
                storageModel.storageId = pendingDeleteImage.storageId
                it[index] = storageModel
            }
            mListStorage.value = it
        }
        mListFavourite.value?.let {
            val index = it.indexOfFirst { sub ->
                sub.storageUri.trim() == pendingDeleteImage.storageUri
            }
            if (index != -1) {
                val storageModel =
                    StorageModel(
                        "$mNewName$extension",
                        mNewFile.absolutePath,
                        pendingDeleteImage.realId
                    )
                storageModel.storageId = pendingDeleteImage.storageId
                it[index] = storageModel
                updateNameStorage(pendingDeleteImage, storageModel)
            }
            mListFavourite.value = it
        }
    }

    internal fun updateListChange(storageModel: StorageModel) {
        mListStorage.value?.let {
            val index = it.indexOfFirst { sub ->
                sub.storageUri == storageModel.storageUri
            }
            if (index != -1) {
                val storage = storageModel.copy()
                storage.isSelected = !storageModel.isSelected
                it[index] = storage
            }
            mListStorage.value = it
        }
        mListFavourite.value?.let {
            val index = it.indexOfFirst { sub ->
                sub.storageUri == storageModel.storageUri
            }
            if (index != -1) {
                val storage = storageModel.copy()
                storage.isSelected = !storageModel.isSelected
                it[index] = storage
            }
            mListFavourite.value = it
        }
        mListStorageSelected.value?.let {
            val storage: StorageModel? = it.firstOrNull { sub ->
                sub.storageUri == storageModel.storageUri
            }
            if (storage != null) {
                it.remove(storage)
            } else {
                it.add(storageModel)
            }
            mListStorageSelected.value = it
        }
    }

    internal fun selectAll() {
        mListStorageSelected.value?.let {
            it.clear()
            mListStorage.value?.let { sub -> it.addAll(sub) }
            mListStorageSelected.value = it
        }
        invalidateSelectAll(true)
    }

    internal fun unSelectAll() {
        mListStorageSelected.value?.let {
            it.clear()
            mListStorageSelected.value = it
        }
        invalidateSelectAll(false)
    }

    private fun invalidateSelectAll(checkSelected: Boolean) {
        mListStorage.value?.let {
            val newList: MutableList<StorageModel> = mutableListOf()
            it.forEach { sub ->
                newList.add(sub.copy())
                newList[newList.size - 1].isSelected = checkSelected
            }
            it.clear()
            it.addAll(newList)
            mListStorage.value = it
        }
        mListFavourite.value?.let {
            val newList: MutableList<StorageModel> = mutableListOf()
            it.forEach { sub ->
                newList.add(sub.copy())
                newList[newList.size - 1].isSelected = checkSelected
            }
            it.clear()
            it.addAll(newList)
            mListFavourite.value = it
        }
    }

    internal fun checkSelected(): Boolean {
        return mListStorageSelected.value?.size == mListStorage.value?.size
    }

    internal fun getListItemSelected(): MutableList<StorageModel>? {
        return mListStorageSelected.value
    }

    internal fun deleteAllFile(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            var file: File
            mListStorageSelected.value?.let {
                for (i in it) {
                    file = File(i.storageUri)
                    if (file.exists()) {
                        file.delete()
                        MediaScannerConnection.scanFile(
                            context, arrayOf(i.storageUri),
                            null, null
                        )
                    }
                }
                it.clear()
                mListStorageSelected.value = it
            }
            validateDeleteAll()
        } else {
            try {
                mListStorageSelected.value?.let {
                    for (i in it) {
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            getRealIDFromURI(Uri.parse(i.storageUri), context)
                        )
                        context.contentResolver.delete(
                            uri, null, null
                        )
                    }
                    it.clear()
                    mListStorageSelected.value = it
                }
                validateDeleteAll()
            } catch (_: Exception) {
                mListStorageSelected.value?.let {
                    val listUri: MutableList<Uri> = mutableListOf()
                    listUri.clear()
                    for (i in it) {
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            getRealIDFromURI(Uri.parse(i.storageUri), context)
                        )
                        listUri.add(uri)
                    }
                    val pi = MediaStore.createWriteRequest(
                        context.contentResolver,
                        listUri
                    )
                    mPermissionNeededForDelete.value = pi.intentSender
                }
            }
        }
    }

    private fun validateDeleteAll() {
        mListFavourite.value?.let {
            val newList: MutableList<StorageModel> = mutableListOf()
            it.forEach { sub ->
                if (!sub.isSelected) {
                    newList.add(sub.copy())
                } else {
                    viewModelScope.launch(Dispatchers.IO) {
                        storageRepository.deleteStorage(sub)
                    }
                }
            }
            it.clear()
            it.addAll(newList)
            mListFavourite.value = it
        }
        mListStorage.value?.let {
            val newList: MutableList<StorageModel> = mutableListOf()
            it.forEach { sub ->
                if (!sub.isSelected) {
                    newList.add(sub.copy())
                }
            }
            it.clear()
            it.addAll(newList)
            mListStorage.value = it
        }
    }
}

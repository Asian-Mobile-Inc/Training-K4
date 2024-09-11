package com.example.asian.retrofit.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.R
import com.example.asian.retrofit.api.ApiHelper
import com.example.asian.retrofit.api.RetrofitBuilder
import com.example.asian.retrofit.database.repository.RetrofitRoomRepository
import com.example.asian.retrofit.model.ImageModel
import com.example.asian.retrofit.utils.Constant
import com.example.asian.retrofit.utils.RealPathUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class RetrofitViewModel(application: Application) : AndroidViewModel(application) {
    private val roomRepository: RetrofitRoomRepository = RetrofitRoomRepository(application)
    private val apiHelper = ApiHelper(RetrofitBuilder.apiService)
    private val apiUploadHelper = ApiHelper(RetrofitBuilder.apiServiceUpload)

    private val _listImage = MutableLiveData<MutableList<ImageModel>>()
    internal val listImage: LiveData<MutableList<ImageModel>> = _listImage

    private var _listFavourite = MutableLiveData<MutableList<ImageModel>>()
    internal var listFavourite: LiveData<MutableList<ImageModel>> = _listFavourite

    private var _listLocal = MutableLiveData<MutableList<ImageModel>>()
    internal var listLocal: LiveData<MutableList<ImageModel>> = _listLocal

    private var _statusRetrofitCallback = MutableLiveData<Int>()
    internal var statusRetrofitCallback: LiveData<Int> = _statusRetrofitCallback

    private var _progressDownload = MutableLiveData<Int>()
    internal var progressDownload: LiveData<Int> = _progressDownload

    private var page = 2
    private var itemQuantityChange = 0
    internal var isLoading = false
    internal var isRefresh = true
    private var _listDownload: MutableList<ImageModel> = mutableListOf()

    init {
        _listImage.value = mutableListOf()
        _listFavourite.value = mutableListOf()
        _listLocal.value = mutableListOf()
        _statusRetrofitCallback.value = Constant.STATUS_CODE_OK
        _progressDownload.value = 0
    }

    internal fun uploadImage(uri: Uri, context: Context) {
        _statusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOADING
        if (uri != Uri.parse(Constant.STRING_EMPTY)) {
            itemQuantityChange++
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val fileRealPath =
                        RealPathUtil.getRealPathFromURI(context, uri) ?: Constant.STRING_EMPTY
                    val file = File(fileRealPath)
                    if (file.exists()) {
                        val createdAt = System.currentTimeMillis()
                        val requestFile: RequestBody =
                            file.asRequestBody(Constant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData(
                            Constant.KEY_IMG_DATA, createdAt.toString(), requestFile
                        )
                        val sdf = SimpleDateFormat(
                            Constant.CONVERT_TIME_TO_CREATED_AT, Locale.ENGLISH
                        )
                        sdf.timeZone = TimeZone.getTimeZone(Constant.KEY_GMT_DEFAULT)
                        val time = sdf.format((createdAt - Constant.TIME_DELAY_UPLOAD))
                        val img = apiUploadHelper.uploadImage(body)
                        if (img.isSuccessful) {
                            _listImage.value?.let {
                                img.body()?.let { r ->
                                    if (time <= r.createdAt) {
                                        it.add(0, r)
                                    } else {
                                        itemQuantityChange--
                                        withContext(Dispatchers.Main) {
                                            _statusRetrofitCallback.postValue(Constant.STATUS_CODE_EXISTS_IMAGE_API)
                                        }
                                    }
                                }
                                _listImage.postValue(it)
                            }
                        }
                        _statusRetrofitCallback.postValue(img.code())
                        _statusRetrofitCallback.postValue(Constant.STATUS_CODE_UPLOAD_SUCCESS)
                    }
                } catch (e: IOException) {
                    _statusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
                }
            }
        } else {
            _statusRetrofitCallback.value = Constant.STATUS_CODE_NO_PICK_IMAGE
        }
    }

    internal fun deleteImage(imgId: String) {
        _statusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val im = apiHelper.deleteImage(imgId)
                if (im.isSuccessful) {
                    itemQuantityChange--
                    _listImage.value?.let {
                        val img = it.firstOrNull { sub ->
                            im.body()?.imageId == sub.imageId
                        }
                        if (img != null) {
                            it.remove(img)
                        }
                        _listImage.postValue(it)
                    }
                }
                _statusRetrofitCallback.postValue(im.code())
            } catch (e: IOException) {
                _statusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
            }
        }
    }

    private suspend fun fetchImagesFromRoom(): MutableList<ImageModel> {
        _listFavourite.postValue(roomRepository.getAllStorage())
        return roomRepository.getAllStorage()
    }

    private suspend fun fetchImagesFromApi(): MutableList<ImageModel> {
        try {
            val rs = apiHelper.getAllImages(Constant.ITEM_PER_PAGE)
            rs.body()?.let {
                _listImage.postValue(rs.body())
            }
            _statusRetrofitCallback.postValue(rs.code())
            rs.body()?.let {
                return it
            }
        } catch (e: IOException) {
            _statusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
        }
        return mutableListOf()
    }

    internal fun fetchImagesFromLocal(context: Context): MutableList<ImageModel> {
        val imageList: MutableList<ImageModel> = mutableListOf()
        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
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
                val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val dataId = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val storageId = cursor.getLong(dataId).toString()
                val name =
                    cursor.getString(dataColumnIndex).toString().substringAfterLast(File.separator)
                        .substringBeforeLast(".")
                val type = cursor.getString(dataColumnIndex).toString().substringAfterLast(".")
                val uri = Uri.parse(cursor.getString(dataColumnIndex).toString())
                val storageModel = ImageModel(name, uri.toString(), type, Constant.STRING_EMPTY)
                storageModel.storageId = storageId
                imageList.add(storageModel)
            }
            imageList.let {
                _listLocal.postValue(it)
            }
        }
        cursor?.close()
        return imageList
    }

    internal fun fetchAllImages(context: Context) {
        if (_listImage.value?.size == 0) {
            _statusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOADING
            viewModelScope.launch {
                val imgRoom: MutableList<ImageModel> = async {
                    fetchImagesFromRoom()
                }.await()
                val imgApi: MutableList<ImageModel> = async {
                    fetchImagesFromApi()
                }.await()
                var imgLocal: MutableList<ImageModel> = mutableListOf()
                if (hasPermissions(context)) {
                    imgLocal = async {
                        fetchImagesFromLocal(context)
                    }.await()
                }
                viewModelScope.launch(Dispatchers.Default) {
                    getStatus(imgApi, imgRoom, imgLocal)
                }
            }
        }
    }

    private fun getStatus(
        imgApi: MutableList<ImageModel>,
        imgRoom: MutableList<ImageModel>,
        imgLocal: MutableList<ImageModel>,
    ) {
        imgApi.forEach {
            val indexFavourite = imgRoom.indexOfFirst { sub ->
                sub.imageId == it.imageId
            }
            val indexDownloaded = imgLocal.indexOfFirst { sub ->
                sub.imageId == it.imageId
            }
            if (indexFavourite != -1) {
                it.isFavourite = true
            }
            if (indexDownloaded != -1) {
                it.isDownloaded = true
            }
        }
        _listImage.postValue(imgApi)
        imgLocal.forEach {
            val indexFavourite = imgRoom.indexOfFirst { sub ->
                sub.imageId == it.imageId
            }
            if (indexFavourite != -1) {
                it.isFavourite = true
            }
        }
        _listLocal.postValue(imgLocal)
        imgRoom.forEach {
            it.isFavourite = true
        }
        _listFavourite.postValue(imgRoom)
    }

    internal fun handleClickFavourite(imageModel: ImageModel) {
        if (imageModel.isFavourite) {
            viewModelScope.launch(Dispatchers.IO) {
                roomRepository.deleteStorage(imageModel)
            }
            handleFavouriteList(imageModel)
            _listFavourite.value?.let {
                val item = it.firstOrNull { sub ->
                    sub.imageId == imageModel.imageId
                }
                it.remove(item)
                _listFavourite.postValue(it)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                roomRepository.insertStorage(imageModel)
            }
            _listFavourite.value?.let {
                val newItem = imageModel.copy()
                newItem.isFavourite = !imageModel.isFavourite
                newItem.isDownloaded = imageModel.isDownloaded
                it.add(newItem)
                _listFavourite.value = it
            }
            handleFavouriteList(imageModel)
        }
    }

    private fun handleFavouriteList(imageModel: ImageModel) {
        _listImage.value?.let {
            val index = it.indexOfFirst { sub ->
                sub.imageId == imageModel.imageId
            }
            if (index != -1) {
                val newItem = it[index].copy()
                newItem.isFavourite = !imageModel.isFavourite
                newItem.isDownloaded = imageModel.isDownloaded
                it[index] = newItem
            }
            _listImage.postValue(it)
        }
        _listLocal.value?.let {
            val index = it.indexOfFirst { sub ->
                sub.imageId == imageModel.imageId
            }
            if (index != -1) {
                val newItem = it[index].copy()
                newItem.isFavourite = !imageModel.isFavourite
                newItem.isDownloaded = imageModel.isDownloaded
                it[index] = newItem
            }
            _listLocal.postValue(it)
        }
    }

    internal fun reDownloadImage(context: Context) {
        _listDownload.forEach {
            downloadImage(it, context)
        }
        _listDownload.clear()
    }

    internal fun downloadImage(imageModel: ImageModel, context: Context) {
        _statusRetrofitCallback.value = Constant.STATUS_CODE_START_DOWNLOAD
        lateinit var file: File
        var progress = Constant.PROGRESS_MIN
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator + imageModel.imageId + "." + imageModel.type
                file = File(path)
                val url = URL(imageModel.url)
                val mHttpURLConnection = url.openConnection() as HttpURLConnection
                mHttpURLConnection.connect()
                val fileLength = mHttpURLConnection.getContentLength()
                val inputStream: InputStream = BufferedInputStream(url.openStream())
                if (file.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context, context.getString(R.string.image_exists), Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val outputStream = FileOutputStream(file)
                    val data = ByteArray(1024)
                    var total: Long = 0
                    var count: Int
                    while ((inputStream.read(data).also { count = it }) != -1) {
                        total += count.toLong()
                        outputStream.write(data, 0, count)
                        if (progress != ((total * Constant.PROGRESS_MAX / fileLength).toInt())) {
                            progress = ((total * Constant.PROGRESS_MAX / fileLength).toInt())
                            _progressDownload.postValue(progress)
                        }
                    }
                    outputStream.flush()
                    outputStream.close()
                    _listImage.value?.let {
                        val index = it.indexOfFirst { sub ->
                            sub.imageId == imageModel.imageId
                        }
                        if (index != -1) {
                            val newItem = it[index].copy()
                            newItem.isDownloaded = !it[index].isDownloaded
                            newItem.isFavourite = it[index].isFavourite
                            it[index] = newItem
                        }
                        _listImage.postValue(it)
                    }
                    _listLocal.value?.let {
                        it.add(imageModel)
                        _listLocal.postValue(it)
                    }
                    _progressDownload.postValue(Constant.PROGRESS_MAX)
                }
                inputStream.close()
                _statusRetrofitCallback.postValue(Constant.STATUS_CODE_OK)
                MediaScannerConnection.scanFile(
                    context, arrayOf(path), null, null
                )
            } catch (e: IOException) {
                _listDownload.add(imageModel)
                if (file.exists()) {
                    file.delete()
                }
                _statusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
            }
        }
    }

    internal fun getListItemRetrofit(): MutableList<ImageModel> {
        _listImage.value?.let {
            return it
        }
        return mutableListOf()
    }

    private suspend fun getListLoadMore(): MutableList<ImageModel> {
        try {
            if (itemQuantityChange < 0) {
                page += (itemQuantityChange / Constant.ITEM_PER_PAGE) - 1
                itemQuantityChange %= Constant.ITEM_PER_PAGE
            }
            if (itemQuantityChange > 0) {
                page += itemQuantityChange / Constant.ITEM_PER_PAGE
                itemQuantityChange %= Constant.ITEM_PER_PAGE
            }
            val rs = apiHelper.loadMoreImage(page, Constant.ITEM_PER_PAGE)
            _statusRetrofitCallback.postValue(rs.code())
            page++
            rs.body()?.let {
                isLoading = false
                if (it.size == 0) {
                    page--
                    _statusRetrofitCallback.postValue(Constant.STATUS_CODE_NO_ITEM_MORE)
                }
                return it
            }
        } catch (e: IOException) {
            _statusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
        }
        return mutableListOf()
    }

    internal fun loadMore() {
        isLoading = true
        _statusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOAD_MORE
        viewModelScope.launch(Dispatchers.IO) {
            val imgApi: MutableList<ImageModel> = async {
                getListLoadMore()
            }.await()
            _statusRetrofitCallback.postValue(Constant.STATUS_CODE_HIDE_DIALOG_LOAD_MORE)
            viewModelScope.launch(Dispatchers.Default) {
                _listImage.value?.let {
                    if (itemQuantityChange == 0) {
                        it.addAll(imgApi)
                    } else {
                        if (itemQuantityChange < 0) {
                            for (i in imgApi.size + itemQuantityChange until imgApi.size) {
                                it.add(imgApi[i])
                            }
                            itemQuantityChange = 0
                        } else {
                            for (i in itemQuantityChange until imgApi.size) {
                                it.add(imgApi[i])
                            }
                            itemQuantityChange = 0
                        }
                    }
                    _listImage.postValue(it)
                    _listFavourite.value?.let { itFav ->
                        _listLocal.value?.let { itLocal ->
                            getStatus(it, itFav, itLocal)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getListRefresh(): MutableList<ImageModel> {
        val newList: MutableList<ImageModel> = mutableListOf()
        try {
            val rs = apiHelper.loadMoreImage(1, Constant.ITEM_PER_PAGE)
            _statusRetrofitCallback.postValue(rs.code())
            rs.body()?.let {
                newList.addAll(it)
            }
        } catch (e: IOException) {
            _statusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
        }
        return newList
    }

    internal fun refresh() {
        _statusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_REFRESH
        viewModelScope.launch(Dispatchers.IO) {
            val imgApi: MutableList<ImageModel> = async {
                getListRefresh()
            }.await()
            _statusRetrofitCallback.postValue(Constant.STATUS_CODE_HIDE_DIALOG_REFRESH)
            viewModelScope.launch(Dispatchers.Default) {
                _listImage.value?.let {
                    if (imgApi.size != 0) {
                        it.clear()
                        it.addAll(imgApi)
                        _listImage.postValue(it)
                        _listFavourite.value?.let { itFav ->
                            _listLocal.value?.let { itLocal ->
                                getStatus(it, itFav, itLocal)
                            }
                        }
                    }
                }
            }
        }
    }

    private val listPermissionsName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        mutableListOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.POST_NOTIFICATIONS)
    } else {
        mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun hasPermissions(context: Context) = listPermissionsName.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

package com.example.asian.retrofit.viewmodel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
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
import retrofit2.HttpException
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
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
    private val mListImage = MutableLiveData<MutableList<ImageModel>>()
    internal val listImage: LiveData<MutableList<ImageModel>> = mListImage
    private var mListFavourite = MutableLiveData<MutableList<ImageModel>>()
    internal var listFavourite: LiveData<MutableList<ImageModel>> = mListFavourite
    private var mListLocal = MutableLiveData<MutableList<ImageModel>>()
    internal var listLocal: LiveData<MutableList<ImageModel>> = mListLocal
    private var mStatusRetrofitCallback = MutableLiveData<Int>()
    internal var statusRetrofitCallback: LiveData<Int> = mStatusRetrofitCallback
    private var mPage = 2
    private var mItemQuantityChange = 0
    internal var mIsLoading = false

    init {
        mListImage.value = mutableListOf()
        mListFavourite.value = mutableListOf()
        mListLocal.value = mutableListOf()
        mStatusRetrofitCallback.value = Constant.STATUS_CODE_OK
    }

    internal fun uploadImage(uri: Uri, context: Context) {
        mStatusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOADING
        if (uri != Uri.parse("")) {
            mItemQuantityChange++
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val fileRealPath = RealPathUtil.getRealPathFromURI(context, uri) ?: ""
                    val file = File(fileRealPath)
                    if (file.exists()) {
                        val createdAt = System.currentTimeMillis()
                        val requestFile: RequestBody =
                            file.asRequestBody(Constant.MULTIPART_FORM_DATA.toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData(
                            Constant.KEY_IMG_DATA,
                            createdAt.toString(),
                            requestFile
                        )
                        val sdf =
                            SimpleDateFormat(
                                Constant.CONVERT_TIME_TO_CREATED_AT,
                                Locale.ENGLISH
                            )
                        sdf.timeZone = TimeZone.getTimeZone(Constant.KEY_GMT_DEFAULT)
                        val time = sdf.format(createdAt)
                        val img = apiUploadHelper.uploadImage(body)
                        if (img.isSuccessful) {
                            mListImage.value?.let {
                                img.body()?.let { r ->
                                    if (time <= r.createdAt) {
                                        it.add(0, r)
                                    } else {
                                        mItemQuantityChange--
                                        withContext(Dispatchers.Main) {
                                            mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_EXISTS_IMAGE_API)
                                        }
                                    }
                                }
                                mListImage.postValue(it)
                            }
                        }
                        mStatusRetrofitCallback.postValue(img.code())
                    }
                } catch (e: HttpException) {
                    mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_NO_INTERNET)
                } catch (e: Exception) {
                    mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
                    e.printStackTrace()
                }
            }
        } else {
            mStatusRetrofitCallback.value = Constant.STATUS_CODE_NO_PICK_IMAGE
        }
    }

    internal fun deleteImage(imgId: String) {
        mStatusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOADING
        viewModelScope.launch {
            try {
                val im = apiHelper.deleteImage(imgId)
                if (im.isSuccessful) {
                    mItemQuantityChange--
                    mListImage.value?.let {
                        val img = it.firstOrNull { sub ->
                            im.body()?.imageId == sub.imageId
                        }
                        if (img != null) {
                            it.remove(img)
                        }
                        mListImage.postValue(it)
                    }
                }
                mStatusRetrofitCallback.postValue(im.code())
            } catch (e: HttpException) {
                mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_NO_INTERNET)
            } catch (e: Exception) {
                mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
                e.printStackTrace()
            }
        }
    }

    private suspend fun fetchImagesFromRoom(): MutableList<ImageModel> {
        mListFavourite.postValue(roomRepository.getAllStorage())
        return roomRepository.getAllStorage()
    }

    private suspend fun fetchImagesFromApi(): MutableList<ImageModel> {
        try {
            val rs = apiHelper.getAllImages(Constant.ITEM_PER_PAGE)
            rs.body()?.let {
                mListImage.postValue(rs.body())
            }
            mStatusRetrofitCallback.postValue(rs.code())
            rs.body()?.let {
                return it
            }
        } catch (e: HttpException) {
            mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_NO_INTERNET)
        } catch (e: Exception) {
            mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
            e.printStackTrace()
        }
        return mutableListOf()
    }

    private fun fetchImagesFromLocal(context: Context): MutableList<ImageModel> {
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
                val dataColumnIndex =
                    cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val dataId = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val storageId = cursor.getLong(dataId).toString()
                val name = cursor.getString(dataColumnIndex).toString().substringAfterLast("/")
                    .substringBeforeLast(".")
                val type = cursor.getString(dataColumnIndex).toString().substringAfterLast(".")
                val uri = Uri.parse(cursor.getString(dataColumnIndex).toString())
                val storageModel = ImageModel(name, uri.toString(), type, "")
                storageModel.storageId = storageId
                imageList.add(storageModel)
            }
            imageList.let {
                mListLocal.postValue(it)
            }
        }
        cursor?.close()
        return imageList
    }

    internal fun fetchAllImages(context: Context) {
        if (mListImage.value?.size == 0) {
            mStatusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOADING
            viewModelScope.launch {
                val imgRoom: MutableList<ImageModel> = async {
                    fetchImagesFromRoom()
                }.await()
                val imgApi: MutableList<ImageModel> = async {
                    fetchImagesFromApi()
                }.await()
                val imgLocal: MutableList<ImageModel> = async {
                    fetchImagesFromLocal(context)
                }.await()
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
        mListImage.postValue(imgApi)
        imgLocal.forEach {
            val indexFavourite = imgRoom.indexOfFirst { sub ->
                sub.imageId == it.imageId
            }
            if (indexFavourite != -1) {
                it.isFavourite = true
            }
        }
        mListLocal.postValue(imgLocal)
        imgRoom.forEach {
            it.isFavourite = true
        }
        mListFavourite.postValue(imgRoom)
    }

    internal fun handlerClickFavourite(imageModel: ImageModel) {
        if (imageModel.isFavourite) {
            viewModelScope.launch(Dispatchers.IO) {
                roomRepository.deleteStorage(imageModel)
            }
            handlerFavouriteList(imageModel)
            mListFavourite.value?.let {
                val item = it.firstOrNull { sub ->
                    sub.imageId == imageModel.imageId
                }
                it.remove(item)
                mListFavourite.postValue(it)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                roomRepository.insertStorage(imageModel)
            }
            mListFavourite.value?.let {
                val newItem = imageModel.copy()
                newItem.isFavourite = !imageModel.isFavourite
                newItem.isDownloaded = imageModel.isDownloaded
                it.add(newItem)
                mListFavourite.value = it
            }
            handlerFavouriteList(imageModel)
        }
    }

    private fun handlerFavouriteList(imageModel: ImageModel) {
        mListImage.value?.let {
            val index = it.indexOfFirst { sub ->
                sub.imageId == imageModel.imageId
            }
            if (index != -1) {
                val newItem = it[index].copy()
                newItem.isFavourite = !it[index].isFavourite
                newItem.isDownloaded = it[index].isDownloaded
                it[index] = newItem
            }
            mListImage.postValue(it)
        }
        mListLocal.value?.let {
            val index = it.indexOfFirst { sub ->
                sub.imageId == imageModel.imageId
            }
            if (index != -1) {
                val newItem = it[index].copy()
                newItem.isFavourite = !it[index].isFavourite
                newItem.isDownloaded = it[index].isDownloaded
                it[index] = newItem
            }
            mListLocal.postValue(it)
        }
    }

    fun downloadImage(imageModel: ImageModel, context: Context) {
        mStatusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(imageModel.url)
                val mHttpURLConnection = url.openConnection() as HttpURLConnection
                mHttpURLConnection.connect()
                val inputStream: InputStream = BufferedInputStream(url.openStream())
                val path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .absolutePath + File.separator + imageModel.imageId + "." + imageModel.type
                val file = File(path)
                if (file.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.image_exists),
                            Toast.LENGTH_SHORT
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
                    }
                    outputStream.flush()
                    outputStream.close()
                    mListImage.value?.let {
                        val index = it.indexOfFirst { sub ->
                            sub.imageId == imageModel.imageId
                        }
                        if (index != -1) {
                            val newItem = it[index].copy()
                            newItem.isDownloaded = !it[index].isDownloaded
                            newItem.isFavourite = it[index].isFavourite
                            it[index] = newItem
                        }
                        mListImage.postValue(it)
                    }
                    mListLocal.value?.let {
                        it.add(imageModel)
                        mListLocal.postValue(it)
                    }
                }
                inputStream.close()
                mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_OK)
                MediaScannerConnection.scanFile(
                    context, arrayOf(path),
                    null, null
                )
            } catch (e: HttpException) {
                mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_NO_INTERNET)
            } catch (e: Exception) {
                mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
                e.printStackTrace()
            }
        }
    }

    internal fun getListItemRetrofit(): MutableList<ImageModel> {
        mListImage.value?.let {
            return it
        }
        return mutableListOf()
    }

    private suspend fun getListLoadMore(): MutableList<ImageModel> {
        try {
            if (mItemQuantityChange < 0) {
                mPage += (mItemQuantityChange / Constant.ITEM_PER_PAGE) - 1
                mItemQuantityChange %= Constant.ITEM_PER_PAGE
            }
            if (mItemQuantityChange > 0) {
                mPage += mItemQuantityChange / Constant.ITEM_PER_PAGE
                mItemQuantityChange %= Constant.ITEM_PER_PAGE
            }
            val rs = apiHelper.loadMoreImage(mPage, Constant.ITEM_PER_PAGE)
            mStatusRetrofitCallback.postValue(rs.code())
            mPage++
            rs.body()?.let {
                mIsLoading = false
                if (it.size == 0) {
                    mPage--
                    mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_NO_ITEM_MORE)
                }
                return it
            }
        } catch (e: HttpException) {
            mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_NO_INTERNET)
        } catch (e: Exception) {
            mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_OTHER_EXCEPTION)
            e.printStackTrace()
        }
        return mutableListOf()
    }

    internal fun loadMore() {
        mStatusRetrofitCallback.value = Constant.STATUS_CODE_SHOW_DIALOG_LOAD_MORE
        mIsLoading = true
        viewModelScope.launch(Dispatchers.IO) {
            val imgApi: MutableList<ImageModel> = async {
                getListLoadMore()
            }.await()
            mStatusRetrofitCallback.postValue(Constant.STATUS_CODE_HIDE_DIALOG_LOAD_MORE)
            viewModelScope.launch(Dispatchers.Default) {
                mListImage.value?.let {
                    if (mItemQuantityChange == 0) {
                        it.addAll(imgApi)
                    } else {
                        if (mItemQuantityChange < 0) {
                            for (i in imgApi.size + mItemQuantityChange until imgApi.size) {
                                it.add(imgApi[i])
                            }
                            mItemQuantityChange = 0
                        } else {
                            for (i in mItemQuantityChange until imgApi.size) {
                                it.add(imgApi[i])
                            }
                            mItemQuantityChange = 0
                        }
                    }
                    mListImage.postValue(it)
                    mListFavourite.value?.let { itFav ->
                        mListLocal.value?.let { itLocal ->
                            getStatus(it, itFav, itLocal)
                        }
                    }
                }
            }
        }
    }
}

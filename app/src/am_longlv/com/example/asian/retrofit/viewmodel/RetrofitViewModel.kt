package com.example.asian.retrofit.viewmodel

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asian.retrofit.api.ApiHelper
import com.example.asian.retrofit.api.RetrofitBuilder
import com.example.asian.retrofit.database.repository.RetrofitRoomRepository
import com.example.asian.retrofit.model.ImageModel
import com.example.asian.retrofit.utils.RealPathUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

private const val KEY_IMG_DATA = "imagedata"
private const val MULTIPART_FORM_DATA = "multipart/form-data"

class RetrofitViewModel(application: Application) : AndroidViewModel(application) {
    private val roomRepository: RetrofitRoomRepository = RetrofitRoomRepository(application)
    private val apiHelper = ApiHelper(RetrofitBuilder.apiService)
    private val apiUploadHelper = ApiHelper(RetrofitBuilder.apiServiceUpload)
    private val mListImage = MutableLiveData<MutableList<ImageModel>>()
    internal val listImage: LiveData<MutableList<ImageModel>> = mListImage
    private var mListFavourite = MutableLiveData<MutableList<ImageModel>>()
    internal var listFavourite: LiveData<MutableList<ImageModel>> = mListFavourite
    private var mStatusRetrofitCallback = MutableLiveData<Int>()
    internal var statusRetrofitCallback: LiveData<Int> = mStatusRetrofitCallback

    init {
        mListImage.value = mutableListOf()
        mListFavourite.value = mutableListOf()
        mStatusRetrofitCallback.value = -1
        fetchImages()
    }

    internal fun fetchImages() {
        mStatusRetrofitCallback.value = 0
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rs = apiHelper.getAllImages()
                if (rs.isSuccessful) {
                    mListImage.postValue(rs.body())
                }
                mStatusRetrofitCallback.postValue(rs.code())
            } catch (e: Exception) {
                mStatusRetrofitCallback.postValue(-1)
                e.printStackTrace()
            }
        }
    }

    internal fun uploadImage(uri: Uri, context: Context) {
        mStatusRetrofitCallback.value = 0
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fileRealPath = RealPathUtil.getRealPathFromURI(context, uri) ?: ""
                val file = File(fileRealPath)
                if (file.exists()) {
                    val requestFile: RequestBody =
                        file.asRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
                    val body = MultipartBody.Part.createFormData(
                        KEY_IMG_DATA,
                        System.currentTimeMillis().toString(),
                        requestFile
                    )
                    val img = apiUploadHelper.uploadImage(body)
                    if (img.isSuccessful) {
                        mListImage.value?.let {
                            img.body()?.let { r -> it.add(0, r) }
                            mListImage.postValue(it)
                        }
                    }
                    mStatusRetrofitCallback.postValue(img.code())
                }
            } catch (e: HttpException) {
                mStatusRetrofitCallback.postValue(-1)
            } catch (e: Exception) {
                mStatusRetrofitCallback.postValue(-2)
                e.printStackTrace()
            }
        }
    }

    internal fun deleteImage(imgId: String) {
        mStatusRetrofitCallback.value = 0
        viewModelScope.launch {
            try {
                val im = apiHelper.deleteImage(imgId)
                if (im.isSuccessful) {
                    mListImage.value?.let {
                        val img = it.firstOrNull { sub ->
                            im.body()?.id == sub.id
                        }
                        if (img != null) {
                            it.remove(img)
                        }
                        mListImage.postValue(it)
                    }
                }
                mStatusRetrofitCallback.postValue(im.code())
            } catch (e: Exception) {
                mStatusRetrofitCallback.postValue(-1)
                e.printStackTrace()
            }
        }
    }

    internal fun getImageFromRoom() {
        viewModelScope.launch(Dispatchers.IO) {
            mListFavourite.postValue(roomRepository.getAllStorage())
        }
    }

    private fun fetchImages(context: Context): MutableList<ImageModel> {
        val imageList: MutableList<ImageModel> = mutableListOf()
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
//                val storageModel = ImageModel(name, uri.toString(), cursor.getLong(dataRealId))
//                imageList.add(storageModel)
//                imageList.let {
//                    mListStorage.postValue(it)
//                }
            }
        }
        cursor?.close()
        return imageList
    }
}
package com.example.asian.retrofit.utils

object Constant {
    internal const val KEY_IMG_DATA = "imagedata"
    internal const val MULTIPART_FORM_DATA = "multipart/form-data"
    internal const val ITEM_PER_PAGE = 18
    internal const val REQUEST_PERMISSION_CODE = 123
    internal const val STATUS_CODE_OK = 200
    internal const val STATUS_CODE_SHOW_DIALOG_LOADING = 0
    internal const val STATUS_CODE_SHOW_DIALOG_LOAD_MORE = -10
    internal const val STATUS_CODE_NO_INTERNET = -1
    internal const val STATUS_CODE_OTHER_EXCEPTION = -2
    internal const val STATUS_CODE_NO_PICK_IMAGE = -3
    internal const val STATUS_CODE_NO_ITEM_MORE = -4
    internal const val STATUS_CODE_EXISTS_IMAGE_API = -5
    internal const val CONVERT_TIME_TO_CREATED_AT = "yyyy-MM-dd'T'HH:mm:ss'+0000'"
    internal const val KEY_GMT_DEFAULT = "GMT"
    internal const val KEY_INTERNET_CHANGE = "isInternetChange"
    internal const val ACTION_INTERNET_CHANGE =
        "com.example.asian.retrofit.broadcast.InternetBroadcast"
}

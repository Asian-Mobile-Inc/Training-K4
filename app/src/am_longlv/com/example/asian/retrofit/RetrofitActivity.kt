package com.example.asian.retrofit

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.asian.R
import com.example.asian.databinding.ActivityRetrofitBinding
import com.example.asian.retrofit.viewmodel.RetrofitViewModel

class RetrofitActivity : AppCompatActivity() {
    private val mBinding: ActivityRetrofitBinding by lazy {
        ActivityRetrofitBinding.inflate(layoutInflater)
    }
    private val mRetrofitViewModel: RetrofitViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mRetrofitViewModel.fetchImages()
    }
}

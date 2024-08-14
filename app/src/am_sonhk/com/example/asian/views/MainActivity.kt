package com.example.asian.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.asian.DatabaseEx
import com.example.asian.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivityBinding

    private fun initListioner() {
        with(binding) {
            btnDatabase.setOnClickListener {
                startActivity(Intent(this@MainActivity, DatabaseEx::class.java))
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListioner()
    }
}
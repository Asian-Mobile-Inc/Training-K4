package com.example.asian.utils

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import com.example.asian.databinding.DialogLoadingBinding

class LoadingDialog(app: Application?) {
    private var dialogLoading: AlertDialog = AlertDialog.Builder(app).create()

    fun startLoadingDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        dialogLoading = builder.create()
        dialogLoading.setCancelable(false)
        dialogLoading.setCanceledOnTouchOutside(false)
        dialogLoading.show()
    }

    fun dismissDialog() {
        dialogLoading.dismiss()
    }
}
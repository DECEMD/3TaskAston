package com.example.thirdtaskaston.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.thirdtaskaston.databinding.ProgressDialogLayoutBinding

object ProgressDialog {
    fun createProgressDialog(act: Activity): AlertDialog {
        val builder = AlertDialog.Builder(act)
        val binding = ProgressDialogLayoutBinding.inflate(act.layoutInflater)
        val view = binding.root
        builder.setView(view)

        val dialog = builder.create()
        dialog.setCancelable(false)
        return dialog
    }
}
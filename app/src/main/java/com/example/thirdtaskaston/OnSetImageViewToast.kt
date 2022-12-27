package com.example.thirdtaskaston

import android.content.Context

interface OnSetImageViewToast {
    fun setImageByResId(withEx: Boolean, context: Context, resId: Int, e: Exception?)
}
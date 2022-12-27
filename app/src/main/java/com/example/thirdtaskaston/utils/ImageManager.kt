package com.example.thirdtaskaston.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object ImageManager {
    private const val MAX_IMAGE_SIZE = 1280

    private fun getImageSize(uri: String): List<Int> {
        val image = Picasso.get().load(uri).get()
        return listOf(image.width, image.height)
    }

    suspend fun imageResize(url: String): Bitmap = withContext(
        Dispatchers.IO) {
        val tempList = mutableListOf<List<Int>>()
        val size = getImageSize(url)
        val imageRatio = size[0].toFloat() / size[1].toFloat()
        if (imageRatio > 1) {
            if (size[0] > MAX_IMAGE_SIZE) {
                tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))
            } else {
                tempList.add(listOf(size[0], size[1]))
            }
        } else {
            if (size[1] > MAX_IMAGE_SIZE) {
                tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))
            } else {
                tempList.add(listOf(size[0], size[1]))
            }
        }
        return@withContext Picasso.get().load(url).resize(tempList[0][0], tempList[0][1]).get()
    }

    fun imageResizeAdditionalTask(url: String): Bitmap {
        val tempList = mutableListOf<List<Int>>()
        val bitmap = getBitmapFromURL(url)
        val size = listOf(bitmap!!.width, bitmap.height)
        val imageRatio = size[0].toFloat() / size[1].toFloat()
        if (imageRatio > 1) {
            if (size[0] > MAX_IMAGE_SIZE) {
                tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))
            } else {
                tempList.add(listOf(size[0], size[1]))
            }
        } else {
            if (size[1] > MAX_IMAGE_SIZE) {
                tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))
            } else {
                tempList.add(listOf(size[0], size[1]))
            }
        }
        return Bitmap.createScaledBitmap(bitmap, tempList[0][0], tempList[0][1], false)
    }

    private fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val connection = URL(src).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("MyLog", e.toString())
            null
        }
    }
}
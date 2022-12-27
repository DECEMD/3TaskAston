package com.example.thirdtaskaston

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thirdtaskaston.databinding.ActivityAdditionalTaskBinding
import com.example.thirdtaskaston.utils.ImageManager.imageResizeAdditionalTask
import com.example.thirdtaskaston.utils.ProgressDialog
import java.io.ByteArrayOutputStream

class AdditionalTask : AppCompatActivity(), OnSetImageViewToast {
    private lateinit var binding: ActivityAdditionalTaskBinding
    private var byteArray: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdditionalTaskBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.apply {
            imageViewMini.setOnClickListener {
                if (edTextSecondTask.text.isNotEmpty()) {
                    if (!edTextSecondTask.text.startsWith("http")) {
                        imageViewSecondTask.setImageResource(R.drawable.ic_baseline_error_outline_24)
                        Toast.makeText(this@AdditionalTask,
                            getString(R.string.not_url_in_edText),
                            Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        try {
                            val dialog = ProgressDialog.createProgressDialog(this@AdditionalTask)
                            dialog.show()
                            Thread {
                                val bitmap = imageResizeAdditionalTask(binding.edTextSecondTask.text.toString())
                                runOnUiThread {
                                    byteArray = bitmap.extToByteArray()
                                    softCloseKeyBoard()
                                    binding.imageViewSecondTask.setImageBitmap(bitmap)
                                }
                                dialog.dismiss()
                            }.start()
                        } catch (e: Exception) {
                            Log.d("MyLog", "$e")
                            setImageByResId(true,
                                this@AdditionalTask,
                                R.drawable.ic_baseline_error_outline_24,
                                e)
                        }
                    }
                } else if (edTextSecondTask.text.isEmpty()) {
                    setImageByResId(false, this@AdditionalTask, R.drawable.ic_menu_gallery, null)
                }
            }

            backToActivityButton.setOnClickListener {
                finish()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putByteArray("byteArray", byteArray)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        byteArray = savedInstanceState.getByteArray("byteArray")
        if (byteArray != null) {
            val toByteArray = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            softCloseKeyBoard()
            binding.imageViewSecondTask.setImageBitmap(toByteArray)
        }
    }

    private fun softCloseKeyBoard() {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.secondPlaceholder.windowToken, 0)
    }

    override fun setImageByResId(withEx: Boolean, context: Context, resId: Int, e: Exception?) {
        if (withEx) {
            binding.imageViewSecondTask.setImageResource(resId)
            Toast.makeText(context, getString(R.string.exception_from_url, e), Toast.LENGTH_SHORT)
                .show()
        } else {
            binding.imageViewSecondTask.setImageResource(resId)
            Toast.makeText(context, getString(R.string.edText_is_empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun Bitmap.extToByteArray(): ByteArray {
        val streamOut = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, streamOut)
        return streamOut.toByteArray()
    }
}
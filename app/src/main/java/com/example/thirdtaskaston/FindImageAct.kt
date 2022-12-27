package com.example.thirdtaskaston

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.thirdtaskaston.databinding.ActivityFindImageBinding
import com.example.thirdtaskaston.utils.ImageManager
import com.example.thirdtaskaston.utils.ProgressDialog
import kotlinx.coroutines.*

class FindImageAct : AppCompatActivity(), OnSetImageViewToast {
    private lateinit var binding: ActivityFindImageBinding
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindImageBinding.inflate(layoutInflater).also { setContentView(it.root) }
        if (savedInstanceState != null) {
            setBitmapToImageView()
        }

        binding.imageViewMini.setOnClickListener {
            if (binding.editText.text.isNotEmpty() && binding.editText.text.startsWith("http")) {
                try {
                    setBitmapToImageView()
                } catch (e: Exception) {
                    setImageByResId(true, this, R.drawable.ic_baseline_error_outline_24, e)
                }
            } else if (binding.editText.text.isEmpty()) {
                setImageByResId(false, this, R.drawable.ic_menu_gallery, null)
            } else {
                Toast.makeText(this,
                    getString(R.string.not_url_in_edText),
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.onNextTaskButton.setOnClickListener {
            startActivity(Intent(this, AdditionalTask::class.java))
        }
    }

    override fun setImageByResId(withEx: Boolean, context: Context, resId: Int, e: Exception?) {
        if (withEx) {
            binding.imageView.setImageResource(resId)
            Toast.makeText(context, getString(R.string.exception_from_url, e), Toast.LENGTH_SHORT)
                .show()
        } else {
            binding.imageView.setImageResource(resId)
            Toast.makeText(context, getString(R.string.edText_is_empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = getSystemService(
            INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.placeHolder.windowToken, 0)
    }

    private fun setBitmapToImageView() {
        val dialog = ProgressDialog.createProgressDialog(this)
        job = CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            binding.imageView.setImageBitmap(ImageManager.imageResize(binding.editText.text.toString()))
            hideKeyboard()
            dialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
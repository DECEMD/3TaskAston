package com.example.thirdtaskaston

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var onSecondTask: FloatingActionButton
    private lateinit var nestedScrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onSecondTask = findViewById(R.id.floating_button_to_second_task)
        nestedScrollView = findViewById(R.id.nested_scroll_view)

        onSecondTask.setOnClickListener {
            val intent = Intent(this, FindImageAct::class.java)
            startActivity(intent)
        }
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY && onSecondTask.isShown) {
                onSecondTask.hide()
            }
            if (scrollY < oldScrollY && !onSecondTask.isShown) {
                onSecondTask.show()
            }
            if (scrollY == 0) {
                onSecondTask.show()
            }
        })
    }
}
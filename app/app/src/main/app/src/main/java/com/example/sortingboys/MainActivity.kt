package com.example.sortingboys

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView
import android.widget.LinearLayout

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setBackgroundColor(Color.WHITE)

        val title = TextView(this)
        title.text = "Sorting Boys"
        title.textSize = 28f
        title.setTextColor(Color.BLACK)
        title.gravity = Gravity.CENTER

        layout.addView(title)

        val message = TextView(this)
        message.text = "Welcome to Sorting Boys App"
        message.textSize = 18f
        message.setTextColor(Color.DKGRAY)
        message.gravity = Gravity.CENTER

        layout.addView(message)

        setContentView(layout)
    }
}

package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        // Get values from intent
        val fileName = intent?.getStringExtra(applicationContext.getString(R.string.file_name_key))
        val status = intent?.getStringExtra(applicationContext.getString(R.string.status_key))

        file_name_text.text = fileName
        status_text.text = status

        // Cancel notification
        notificationManager = this.getSystemService(NotificationManager::class.java)
        notificationManager.cancelNotification()

        button.setOnClickListener {
            finish()
        }
    }

}

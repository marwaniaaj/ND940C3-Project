package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var fileName = ""
    private var status = ""
    private var downloadStatus: Int? = null

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createChannel(getString(R.string.notification_channel_id), getString(R.string.notification_channel_name))

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loading_button.setOnClickListener {

            when (url_radio_group.checkedRadioButtonId) {
                R.id.glide_radio -> {
                    Log.i("Radio", "Glide")
                    loading_button.onButtonStateChanged(ButtonState.Loading)
                    fileName = applicationContext.getString(R.string.glide)
                    download(applicationContext.getString(R.string.glide_repo))
                }
                R.id.load_radio -> {
                    Log.i("Radio", "Udacity")
                    loading_button.onButtonStateChanged(ButtonState.Loading)
                    fileName = applicationContext.getString(R.string.loadapp)
                    download(applicationContext.getString(R.string.udacity_repo))
                }
                R.id.retrofit_radio -> {
                    Log.i("Radio", "Retrofit")
                    loading_button.onButtonStateChanged(ButtonState.Loading)
                    fileName = applicationContext.getString(R.string.retrofit)
                    download(applicationContext.getString(R.string.retrofit_repo))
                }
                else -> {
                    Log.i("Radio", "None")
                    loading_button.onButtonStateChanged(ButtonState.Clicked)
                    Toast.makeText(applicationContext, applicationContext.getText(R.string.select_file_to_download), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i("Receiver", "received")
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadID == id) {

                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val query = id?.let {
                    DownloadManager.Query().setFilterById(it)
                }
                val cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                }

                when (downloadStatus) {
                    DownloadManager.STATUS_SUCCESSFUL ->
                        status = applicationContext.getString(R.string.status_success)
                    DownloadManager.STATUS_FAILED ->
                        status = applicationContext.getString(R.string.status_fail)
                }

                val contentIntent = Intent(applicationContext, DetailActivity::class.java)
                contentIntent.putExtra(applicationContext.getString(R.string.file_name_key), fileName)
                contentIntent.putExtra(applicationContext.getString(R.string.status_key), status)

                // Create pending intent
                val pendingIntent = PendingIntent.getActivity(
                        applicationContext, downloadID.toInt(), contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)


                notificationManager.sendNotification(
                        applicationContext.getString(R.string.notification_description),
                        applicationContext,
                        pendingIntent)

                loading_button.onButtonStateChanged(ButtonState.Completed)
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = applicationContext.getString(R.string.notification_description)

            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun download(url: String) {
        Log.i("Download:", "downloading.....")
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}

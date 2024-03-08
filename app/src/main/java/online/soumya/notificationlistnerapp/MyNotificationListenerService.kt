package online.soumya.notificationlistnerapp

import android.content.Context
import android.app.Notification
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.PowerManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MyNotificationListenerService : NotificationListenerService() {

    private lateinit var context: Context
    private var titleData = ""
    private var textData = ""

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras

        titleData = extras.getString("android.title") ?: ""
        textData = extras.getCharSequence("android.text")?.toString() ?: ""

        Log.d("Package", packageName)
        Log.d("Title", titleData)
        Log.d("Text", textData)

        val msgrcv = Intent("Msg")
        msgrcv.putExtra("package", packageName)
        msgrcv.putExtra("title", titleData)
        msgrcv.putExtra("text", textData)
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("Msg", "Notification Removed")
    }
}
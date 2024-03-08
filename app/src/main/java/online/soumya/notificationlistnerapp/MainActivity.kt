package online.soumya.notificationlistnerapp

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rec)

        val notifications = mutableListOf<NotificationModel>()
        notificationAdapter = NotificationAdapter(this, notifications)
        recyclerView.adapter = notificationAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val n = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!n.isNotificationPolicyAccessGranted) {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(onNotice, IntentFilter("Msg"))
    }

    private val onNotice = object : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            val packageName = intent.getStringExtra("package")
            val titleData = intent.getStringExtra("title")
            val textData = intent.getStringExtra("text")

            Toast.makeText(this@MainActivity, textData, Toast.LENGTH_SHORT).show()

            if (packageName == "com.whatsapp" || packageName == "com.instagram") {
                // Play sound
                playNotificationSound(applicationContext)
                // Speak out notification
                speakNotification(titleData, textData, applicationContext)
                // Add received notification to the list
                val newNotification = NotificationModel(packageName, titleData!!, textData!!)
                notificationAdapter.notifications.add(newNotification)
                notificationAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun playNotificationSound(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.notifications_sound)
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
        mediaPlayer.start()
    }

    private lateinit var tts: TextToSpeech
    private fun speakNotification(title: String?, text: String?, context: Context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.speak("New notification from $title: $text", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }
}
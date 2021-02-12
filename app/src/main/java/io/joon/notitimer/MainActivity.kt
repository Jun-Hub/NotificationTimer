package io.joon.notitimer

import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import io.joon.notificationtimer.NotificationTimer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var notiTimer: NotificationTimer.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pendingIntent = Intent(this, MainActivity::class.java).let{
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        build_btn.setOnClickListener {
            notiTimer = NotificationTimer.Builder(this)
                    .setSmallIcon(R.drawable.ic_timer)
                    .setPlayButtonIcon(R.drawable.ic_play_noti)
                    .setPauseButtonIcon(R.drawable.ic_pause_noti)
                    .setStopButtonIcon(R.drawable.ic_stop_noti)
                    .setControlMode(true)
                    .setColor(R.color.sexy_blue)
                    .setShowWhen(false)
                    .setAutoCancel(false)
                    .setOnlyAlertOnce(true)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(pendingIntent)
                    .setOnTickListener { time_until_finish_text.text = it.toString() }
                    .setOnFinishListener { Toast.makeText(this, "timer finished", Toast.LENGTH_SHORT).show() }
                    .setContentTitle("Timer :)")
        }

        play_btn.setOnClickListener {
            notiTimer.play(time_editText.text.toString().toLong())
        }

        pause_btn.setOnClickListener {
            notiTimer.pause()
        }

        stop_btn.setOnClickListener {
            notiTimer.stop()
            time_until_finish_text.text = null
        }

        terminate_btn.setOnClickListener {
            notiTimer.terminate()
        }
    }
}
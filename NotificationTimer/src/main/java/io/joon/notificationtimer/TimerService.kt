package io.joon.notificationtimer

import android.app.Service
import android.content.Intent
import android.os.*

enum class TimerState { STOPPED, PAUSED, RUNNING, TERMINATED }

class TimerService: Service() {

    companion object {
        var state = TimerState.TERMINATED
    }

    private lateinit var timer: CountDownTimer

    private val foreGroundId = 55
    private var secondsRemaining: Long = 0
    private var setTime:Long = 0
    private lateinit var showTime: String

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            when (intent.action) {
                "PLAY" -> {
                    playTimer(
                        intent.getLongExtra("setTime", 0L),
                        intent.getBooleanExtra("forReplay", false))
                }
                "PAUSE" -> pauseTimer()
                "STOP" -> stopTimer()
                "TERMINATE" -> terminateTimer()
            }
        }
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        if (::timer.isInitialized) {
            timer.cancel()
            state = TimerState.TERMINATED
        }
        NotificationTimer.removeNotification()
        stopSelf()
    }

    private fun playTimer(setTime: Long, isReplay: Boolean) {

        if (!isReplay) {
            this.setTime = setTime
            secondsRemaining = setTime
            startForeground(foreGroundId, NotificationTimer.createNotification(this, setTime))
        }

        timer = object : CountDownTimer(secondsRemaining, 1000) {
            override fun onFinish() {
                state = TimerState.STOPPED
                //초기 세팅됬었던 카운트다운 시간값을 노티에 재세팅
                val minutesUntilFinished = setTime/1000 / 60
                val secondsInMinuteUntilFinished = ((setTime/1000) - minutesUntilFinished * 60)
                val secondsStr = secondsInMinuteUntilFinished.toString()
                val showTime = "$minutesUntilFinished : ${if (secondsStr.length == 2) secondsStr else "0$secondsStr"}"
                NotificationTimer.updateStopState(this@TimerService, showTime, true)
            }

            override fun onTick(millisUntilFinished: Long) {
                NotificationTimer.updateUntilFinished(millisUntilFinished + (1000-(millisUntilFinished%1000)) - 1000)
                secondsRemaining = millisUntilFinished
                updateCountdownUI()
            }
        }.start()

        state = TimerState.RUNNING
    }

    private fun pauseTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
            state = TimerState.PAUSED
            NotificationTimer.updatePauseState(this, showTime)
        }
    }

    private fun stopTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
            state = TimerState.STOPPED
            val minutesUntilFinished = setTime/1000 / 60
            val secondsInMinuteUntilFinished = ((setTime/1000) - minutesUntilFinished * 60)
            val secondsStr = secondsInMinuteUntilFinished.toString()
            val showTime = "$minutesUntilFinished : ${if (secondsStr.length == 2) secondsStr else "0$secondsStr"}"
            NotificationTimer.updateStopState(this@TimerService, showTime)
        }
    }

    private fun terminateTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
            state = TimerState.TERMINATED
            NotificationTimer.removeNotification()
            stopSelf()
        }
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = (secondsRemaining/1000) / 60
        val secondsInMinuteUntilFinished = ((secondsRemaining/1000) - minutesUntilFinished * 60)
        val secondsStr = secondsInMinuteUntilFinished.toString()
        showTime = "$minutesUntilFinished : ${if (secondsStr.length == 2) secondsStr else "0$secondsStr"}"

        NotificationTimer.updateTimeLeft(this, showTime)
    }

}
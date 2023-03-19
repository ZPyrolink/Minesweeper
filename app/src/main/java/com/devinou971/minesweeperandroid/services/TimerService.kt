package com.devinou971.minesweeperandroid.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.devinou971.minesweeperandroid.utils.ExtraUtils
import com.devinou971.minesweeperandroid.utils.getExtra
import com.devinou971.minesweeperandroid.utils.putExtra
import java.util.*

class TimerService : Service(){
    override fun onBind(intent: Intent?): IBinder? = null

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getExtra(ExtraUtils.TIME_EXTRA, .0)
        timer.scheduleAtFixedRate(TimeTask(time), 0 ,1_000)
        return START_NOT_STICKY
    }

    private inner class TimeTask(private var time: Double): TimerTask(){
        override fun run() {
            val intent = Intent(ExtraUtils.TIMER_UPDATED.name)
            time++
            intent.putExtra(ExtraUtils.TIME_EXTRA, time)
            sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}
package com.rahdeva.bencanaapp.utils.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rahdeva.bencanaapp.R
import com.rahdeva.bencanaapp.api.ApiConfig
import com.rahdeva.bencanaapp.data.DisasterRepository
import com.rahdeva.bencanaapp.data.model.DisasterItems
import com.rahdeva.bencanaapp.data.model.DisasterResponse
import com.rahdeva.bencanaapp.utils.DataResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val NOTIFICATION_ID_WARNING = 1
        const val CHANNEL_ID = "channel_bencana_app"
        const val CHANNEL_NAME = "bencana_notification"
    }

    private var job: Job? = null
    private val disasterRepository: DisasterRepository = DisasterRepository()
    private var disasterReports = arrayListOf<DisasterItems?>()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        job = launch {
            try {
                disasterRepository.getDisaster().apply {
                    when (this) {
                        is DataResults.Success -> {
                            this.data?.let { disasterReports.addAll(it) }
                        }
                        is DataResults.Error -> { }
                    }
                }
            } catch (e: Exception){
                Log.e("NotificationWorker", e.toString())
            }
        }
        job?.join()
        while (!isStopped) {
            showNotification()
            Thread.sleep(TimeUnit.MINUTES.toMillis(15))
        }

        Result.success()
    }

    private fun showNotification() {
//        val recent = disasterReports.firstOrNull { (it?.disasterProperty?.state ?: 0) >= 3 }
//        // Use below code to test the notification since the recent reports with state >= 3 is often return null
////         val allRecent = disasterReports
//        recent?.let {
//            showDisasterWarningNotification(
//                title = "Bencana Terdeteksi!",
//                description = "Lokasi bencana: ${recent.disasterProperty?.cityName}"
//            )
//        }
        val allRecent = disasterReports
        Log.d("HELLOW", allRecent.toString())
        allRecent?.let {
            showDisasterWarningNotification(
                title = "Bencana Terdeteksi!",
                description = "Lokasi bencana: ${allRecent.first()?.disasterProperty?.cityName}}"
            )
        }
    }


    private fun showDisasterWarningNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_important_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID_WARNING, notification.build())
    }

}
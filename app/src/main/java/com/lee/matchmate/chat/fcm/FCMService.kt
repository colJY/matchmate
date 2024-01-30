package com.lee.matchmate.chat.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lee.matchmate.MainActivity
import com.lee.matchmate.R
import com.lee.matchmate.common.Constants

class FCMService : FirebaseMessagingService() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(pushMessage: RemoteMessage) {
        super.onMessageReceived(pushMessage)

        val roomId = pushMessage.data[Constants.ROOM_ID] ?: Constants.BLANK
        val target = Intent(this@FCMService, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Constants.ROOM_ID, roomId)
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            target,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val name = getString(R.string.notification_channel_name)
        val descriptionText = getString(R.string.notification_channel_description)

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(getString(R.string.app_name), name, importance)

        mChannel.description = descriptionText

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)


        val body = pushMessage.data[Constants.MESSAGE] ?: Constants.BLANK
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_launcher_matchmate)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(applicationContext).notify(0, notificationBuilder.build())
    }


}
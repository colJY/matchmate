package com.lee.matchmate.chat.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lee.matchmate.MainActivity
import com.lee.matchmate.R

const val CHANNEL_ID = "FCM_CHANNEL_ID"
const val CHANNEL_NAME = "PUSH_NAME"
const val TAG = "FCM_TAG"
class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("asd",token)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(pushMessage: RemoteMessage) {
        super.onMessageReceived(pushMessage)

        val name = "채팅 알림"
        val descriptionText = "채팅 알림입니다."

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(getString(R.string.app_name),name,importance)

        mChannel.description = descriptionText

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)





        val body = pushMessage.data["message"] ?: ""
        val notificationBuilder = NotificationCompat.Builder(applicationContext, getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_launcher_matchmate)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(body)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(applicationContext).notify(0, notificationBuilder.build())
    }


}
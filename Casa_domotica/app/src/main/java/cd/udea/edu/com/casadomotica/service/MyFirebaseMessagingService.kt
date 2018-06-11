package cd.udea.edu.com.casadomotica.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import cd.udea.edu.com.casadomotica.MainActivity
import cd.udea.edu.com.casadomotica.R
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val chanelID = "firebaseNotifications"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val firebaseNotification = remoteMessage.data
         if (firebaseNotification != null) {
            saveNotification(firebaseNotification)
        }
    }

    private fun saveNotification(firebaseNotification: MutableMap<String, String>) {
        val id = firebaseNotification["ID"]
        when (id?.get(0)) {
            'B' -> {
                if (firebaseNotification["Porcentaje"] != null) {
                    val sharedPref = getSharedPreferences("Casa_domotica", Context.MODE_PRIVATE) ?: return
                    with(sharedPref.edit()) {
                        putString(id.plus("T"), firebaseNotification["Porcentaje"])
                        apply()
                    }
                    if (firebaseNotification["Porcentaje"]!!.toInt() <= 10) {
                        showNotification(firebaseNotification, "Nivel de agua bajo. (".plus(firebaseNotification["Porcentaje"]).plus("%)"), Color.RED)
                    }
                }
            }
            'C' -> {
                if (firebaseNotification["Porcentaje"] != null) {
                    val sharedPref = getSharedPreferences("Casa_domotica", Context.MODE_PRIVATE) ?: return
                    with(sharedPref.edit()) {
                        putString(id.plus("T"), firebaseNotification["Porcentaje"])
                        apply()
                    }
                    if (firebaseNotification["Porcentaje"]!!.toInt() <= 10) {
                        showNotification(firebaseNotification, "Nivel de cuido bajo. (".plus(firebaseNotification["Porcentaje"]).plus("%)"), Color.RED)
                    }
                }
            }
        }
        if (firebaseNotification["Messaje"] != "null") {
            showNotification(firebaseNotification, firebaseNotification["Messaje"].toString(), Color.BLACK)
        }
    }

    private fun showNotification(firebaseNotification: MutableMap<String, String>, mensaje: String, color: Int) {
        val notificationBuilder = NotificationCompat.Builder(this, chanelID)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.priority = Notification.PRIORITY_HIGH
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND)
        notificationBuilder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
        val backgroundColor = color
        val sharedPref = getSharedPreferences("Casa_domotica", Context.MODE_PRIVATE)
        val nombreDispositivo = sharedPref.getString(firebaseNotification["ID"], "")
        notificationBuilder
                .setColor(backgroundColor)
                .setContentTitle("Alerta para $nombreDispositivo")
                .setContentText(mensaje)

        val notification = notificationBuilder.build()
        notification.contentIntent =
                PendingIntent.getActivity(this@MyFirebaseMessagingService.applicationContext,
                        0,
                        Intent(this@MyFirebaseMessagingService, MainActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(chanelID, "Alertas", NotificationManager.IMPORTANCE_HIGH)
            } else {

            }
            notificationManager.createNotificationChannel(mChannel as NotificationChannel?)
        }
        notificationManager.notify(0, notification)
    }
}
package cd.udea.edu.com.casadomotica.service

import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val chanelID = "firebaseNotifications"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val firebaseNotification = remoteMessage.data
         if (firebaseNotification != null) {
            showNotification(firebaseNotification)
        }
    }

    private fun showNotification(firebaseNotification: MutableMap<String, String>) {

    }
}
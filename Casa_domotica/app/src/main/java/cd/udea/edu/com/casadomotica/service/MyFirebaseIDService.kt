package cd.udea.edu.com.casadomotica.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by claramonsalve on 9/10/17.
 */

class MyFirebaseIDService : FirebaseInstanceIdService() {

    private val TAG = "MyFirebaseIDService"

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token

    }
}

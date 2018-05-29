package cd.udea.edu.com.casadomotica

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Created by pedro on 6/11/2017.
 */
class CameraPermissionsDelegate(private val activity: Activity) {
    private val REQUEST_CODE = 10

    fun hasCameraPermission(): Boolean {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
        )
        return permissionCheckResult == PackageManager.PERMISSION_GRANTED
    }

    fun resultGranted(requestCode: Int,
                      permissions: Array<String>,
                      grantResults: IntArray): Boolean {

        if (requestCode != REQUEST_CODE) {
            return false
        }

        if (grantResults.size < 1) {
            return false
        }
        if (permissions[0] != Manifest.permission.CAMERA) {
            return false
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE
        )
    }
}
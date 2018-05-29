package cd.udea.edu.com.casadomotica

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import com.firebase.ui.auth.AuthUI
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.firebase.ui.auth.IdpResponse
import android.widget.EditText
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123
    var providers = Arrays.asList(
            AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (FirebaseAuth.getInstance().currentUser == null)
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN)
        else{
            setSupportActionBar(toolbar)
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

            // Set up the ViewPager with the sections adapter.
            container.adapter = mSectionsPagerAdapter

            container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
            tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

            fab.setOnClickListener { view ->
                addDispositivo()
            }
        }

    }

    private fun addDispositivo() {
        val cameraPermissionsDelegate = CameraPermissionsDelegate(this@MainActivity)
            if (cameraPermissionsDelegate.hasCameraPermission()) {
                val mView = layoutInflater.inflate(R.layout.qr_scanner, null)
                val scannerView = mView.findViewById<CodeScannerView>(R.id.scanner_view)
                val codeScanner = CodeScanner(this, scannerView)
                val alertDialog = AlertDialog
                        .Builder(this@MainActivity)
                        .setCancelable(false)
                        .setTitle("Escanea el codigo QR del dispositivo")
                        .setPositiveButton("Cancelar", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                            }

                        })
                        .setView(mView)
                        .create()
                codeScanner.decodeCallback = DecodeCallback {
                    runOnUiThread{
                        val id = it.text
                        alertDialog.cancel()
                        val mEditText = EditText(this@MainActivity)
                        val tipoDispositivo = if (id[0].equals('B')) "Bebedero" else "Comedero"
                        AlertDialog
                                .Builder(this@MainActivity)
                                .setTitle("Nuevo $tipoDispositivo")
                                .setMessage("Agrega un nombre")
                                .setCancelable(false)
                                .setPositiveButton("Aceptar") { _, _ ->
                                    FirebaseDatabase
                                            .getInstance()
                                            .reference
                                            .child("Usuarios")
                                            .child(FirebaseAuth.getInstance().uid)
                                            .child("Dispositivos")
                                            .child(id)
                                            .setValue(mEditText.text.toString())
                                    FirebaseDatabase
                                            .getInstance()
                                            .reference
                                            .child("Dispositivos")
                                            .child(id)
                                            .child(FirebaseAuth.getInstance().uid)
                                            .setValue(true)
                                    getSharedPreferences("house", MODE_PRIVATE)
                                            .edit()
                                            .putString(id,mEditText.text.toString())
                                            .apply()
                                }
                                .setNegativeButton("Cancelar") { _, _ ->
                                    FirebaseDatabase
                                            .getInstance()
                                            .reference
                                            .child("Dispositivos")
                                            .child(id)
                                            .child(FirebaseAuth.getInstance().uid)
                                            .removeValue()
                                }
                                .setView(mEditText)
                                .create().show()
                        Toast.makeText(this@MainActivity, "Codigo leido", Toast.LENGTH_LONG).show()
                    }

                }
                codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                    runOnUiThread {
                        Toast.makeText(this, "Camera initialization error: " + it.message,
                                Toast.LENGTH_LONG).show()
                    }
                }
                alertDialog.show()
                codeScanner.startPreview()
            } else {
                AlertDialog
                        .Builder(this@MainActivity)
                        .setTitle("No tenemos permisos para la camara")
                        .setCancelable(false)
                        .setMessage("No puedes escanear el codigo")
                        .setPositiveButton("Aceptar", { dialogInterface: DialogInterface, i: Int -> cameraPermissionsDelegate.requestLocationPermission()})
                        .show()
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode === Activity.RESULT_OK) {
                setSupportActionBar(toolbar)
                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

                // Set up the ViewPager with the sections adapter.
                container.adapter = mSectionsPagerAdapter

                container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
                tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

                fab.setOnClickListener { view ->
                    addDispositivo()
                }
                FirebaseDatabase.getInstance().reference.child("Token").child(FirebaseAuth.getInstance().uid).setValue(FirebaseInstanceId.getInstance().token)
                Toast.makeText(this@MainActivity, "Sesion iniciada correctamente", Toast.LENGTH_LONG).show()
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.cerrar_sesion) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        this@MainActivity.finish()
                    }
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> DispositivosFragment()

                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }
    }
}

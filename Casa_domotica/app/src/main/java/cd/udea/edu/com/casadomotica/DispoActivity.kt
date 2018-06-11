package cd.udea.edu.com.casadomotica

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import cd.udea.edu.com.casadomotica.adapter.TimeAdapter
import cd.udea.edu.com.casadomotica.modelo.Dispositivo
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dispo.*
import kotlinx.android.synthetic.main.add_time.view.*
import kotlinx.android.synthetic.main.fragment_dispositivos.*

class DispoActivity : AppCompatActivity(), ValueEventListener {

    lateinit var idDispositivo: String
    lateinit var dispositivos: MutableList<Dispositivo>
    lateinit var dispoAdapter: TimeAdapter
    lateinit var dispoString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispo)
        idDispositivo = intent.getStringExtra("ID")
        activity_dispo_fab.setOnClickListener {
            addNewTime()
        }
        FirebaseDatabase.getInstance().reference.child("Dispositivos").child(idDispositivo).addValueEventListener(this)
        dispositivos = mutableListOf()
        dispoAdapter = TimeAdapter(dispositivos, R.layout.app_compact_time_list, this@DispoActivity)
        activity_dispo_recyclerview.layoutManager = LinearLayoutManager(this@DispoActivity)
        activity_dispo_recyclerview.adapter = dispoAdapter
    }

    private fun addNewTime() {
        val mView = layoutInflater.inflate(R.layout.add_time, null)
        mView.add_time_timePicker.setIs24HourView(false)
        AlertDialog
                .Builder(this@DispoActivity)
                .setCancelable(false)
                .setTitle("Selecciona la hora y la cantidad de comida")
                .setPositiveButton("Aceptar", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        saveNewTime(mView.add_time_timePicker.hour, mView.add_time_timePicker.minute, mView.add_time_editText.text.toString())
                    }
                })
                .setNegativeButton("Cancelar", null)
                .setView(mView)
                .create()
                .show()
    }

    private fun saveNewTime(hour: Int, minute: Int, food: String) {
        if (food.isNotEmpty()) {
            val time = (hour * 	3600000) + (minute * 60000)
            println(hour)

            FirebaseDatabase.getInstance()
                    .reference
                    .child("Dispositivos")
                    .child(idDispositivo)
                    .setValue(if(dispoString.equals("null")) "$time,$food" else "$dispoString,$time,$food")
        }
    }

    override fun onCancelled(p0: DatabaseError?) {
    }

    override fun onDataChange(p0: DataSnapshot) {
        dispositivos.clear()
        dispoString = p0.value.toString()
        val values = dispoString.split(",")
        var time = ""
        for ((indice, item) in values.withIndex()) {
            if (indice % 2 == 0) {
                time = item
            }else {
                dispositivos.add(Dispositivo(time, item))
            }
        }
        dispoAdapter.notifyDataSetChanged()
    }
}

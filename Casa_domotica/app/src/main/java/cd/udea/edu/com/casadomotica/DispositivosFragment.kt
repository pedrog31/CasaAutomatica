package cd.udea.edu.com.casadomotica

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.udea.edu.com.casadomotica.adapter.DispoAdapter
import cd.udea.edu.com.casadomotica.modelo.Dispositivo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dispositivos.*


/**
 * A simple [Fragment] subclass.
 *
 */
class DispositivosFragment : Fragment(), ChildEventListener, SwipeRefreshLayout.OnRefreshListener {
    lateinit var dispositivos:MutableList<Dispositivo>
    lateinit var dispoAdapter: DispoAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dispositivos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            fragment_dispositivos_swipeRefreshLayout.setOnRefreshListener (this)
            FirebaseDatabase.getInstance().reference.child("Usuarios").child(FirebaseAuth.getInstance().uid).child("Dispositivos").addChildEventListener(this)
            dispositivos = mutableListOf()
            dispoAdapter = DispoAdapter(dispositivos, R.layout.app_compact_dispo_list, this@DispositivosFragment)
            fragment_dispositivos_recyclerview.layoutManager = LinearLayoutManager(this@DispositivosFragment.context)
            fragment_dispositivos_recyclerview.adapter = dispoAdapter
        }
    }


    override fun onCancelled(p0: DatabaseError?) {

    }

    override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
    }

    override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val dispositivo = Dispositivo(p0.key,p0.child("Nombre").value.toString())
        dispositivos.add(dispositivo)
        dispoAdapter.notifyItemInserted(dispoAdapter.itemCount)
        val sharedPref = activity?.getSharedPreferences("Casa_domotica", Context.MODE_PRIVATE)
        if (sharedPref != null) {
            with(sharedPref.edit()) {
                putString(dispositivo.id, dispositivo.nombre)
                apply()
            }
        }
    }

    override fun onChildRemoved(p0: DataSnapshot?) {
    }

    override fun onRefresh() {
        dispoAdapter.notifyDataSetChanged()
        fragment_dispositivos_swipeRefreshLayout.isRefreshing = false
    }

}

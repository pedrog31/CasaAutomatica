package cd.udea.edu.com.casadomotica

import android.os.Bundle
import android.support.v4.app.Fragment
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
class DispositivosFragment : Fragment(), ChildEventListener {

    lateinit var dispositivos:MutableList<Dispositivo>
    lateinit var dispoAdapter: DispoAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dispositivos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (FirebaseAuth.getInstance().currentUser != null) {
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
        val dispositivo = Dispositivo(p0.key,p0.value.toString())
        dispositivos.add(dispositivo)
        dispoAdapter.notifyItemInserted(dispoAdapter.itemCount)
    }

    override fun onChildRemoved(p0: DataSnapshot?) {
    }

}

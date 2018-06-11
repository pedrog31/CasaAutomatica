package cd.udea.edu.com.casadomotica.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.udea.edu.com.casadomotica.DispoActivity
import cd.udea.edu.com.casadomotica.DispositivosFragment
import cd.udea.edu.com.casadomotica.modelo.Dispositivo
import kotlinx.android.synthetic.main.app_compact_dispo_list.view.*

/**
 * Created by Pedro Gallego on 25/10/17.
 */
class DispoAdapter(private var dispositivos: MutableList<Dispositivo>,
                   private val layout: Int,
                   private val dispositivosFragment: DispositivosFragment) : RecyclerView.Adapter<DispoAdapter.GroupViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DispoAdapter.GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: DispoAdapter.GroupViewHolder, position: Int) {
        holder.bindSpace(dispositivos[position])
    }

    override fun getItemCount(): Int {
        return dispositivos.size
    }

    inner class GroupViewHolder(groupView: View) : RecyclerView.ViewHolder(groupView) {
        fun bindSpace(dispositivo: Dispositivo) {
            itemView.app_compat_dispo_list_textView_title.text = dispositivo.nombre
            itemView.app_compat_dispo_list_textView_descripcion.text = when (dispositivo.id[0]) {
                'C' -> "Comedero"
                'B' -> "Bebedero"
                else -> ""
            }
            val sharedPref = dispositivosFragment.activity?.getSharedPreferences("Casa_domotica", Context.MODE_PRIVATE)
            val tanque = sharedPref?.getString(dispositivo.id.plus("T"), null)
            if (tanque != null)
                itemView.app_compat_dispo_list_textView_tanque.text = "Tanque \n $tanque %"
            val coca = sharedPref?.getString(dispositivo.id.plus("C"), null)
            if (coca != null)
                itemView.app_compat_dispo_list_textView_coca.text = "Coca \n $coca %"
            if (dispositivo.id[0].equals('C'))
            itemView.app_compat_dispo_list_cardView_group.setOnClickListener {
                val mIntent = Intent(dispositivosFragment.context, DispoActivity::class.java)
                mIntent.putExtra("ID", dispositivo.id)
                dispositivosFragment.startActivity(mIntent)
            }
        }
     }
}
package cd.udea.edu.com.casadomotica.adapter

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
            itemView.app_compat_dispo_list_textView_description.text = if (dispositivo.id[0].equals('B')) "Bebedero" else "Comedero"
            itemView.app_compat_dispo_list_cardView_group.setOnClickListener {
                dispositivosFragment.startActivity(Intent(dispositivosFragment.context, DispoActivity::class.java))
            }
        }
     }
}
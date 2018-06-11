package cd.udea.edu.com.casadomotica.adapter

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cd.udea.edu.com.casadomotica.DispoActivity
import cd.udea.edu.com.casadomotica.modelo.Dispositivo
import kotlinx.android.synthetic.main.app_compact_time_list.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/**
 * Created by Pedro Gallego on 25/10/17.
 */
class TimeAdapter(private var dispositivos: MutableList<Dispositivo>,
                  private val layout: Int,
                  private val dispositivosFragment: DispoActivity) : RecyclerView.Adapter<TimeAdapter.GroupViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeAdapter.GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeAdapter.GroupViewHolder, position: Int) {
        holder.bindSpace(dispositivos[position])
    }

    override fun getItemCount(): Int {
        return dispositivos.size
    }

    inner class GroupViewHolder(groupView: View) : RecyclerView.ViewHolder(groupView) {
        fun bindSpace(dispositivo: Dispositivo) {
            val calendar = Date()
            calendar.time = calendar.time - ((calendar.hours * 	3600000) + (calendar.minutes * 60000)) + dispositivo.id.toLong()
            val format = SimpleDateFormat("h:mm a", Locale("es", "CO"))
            itemView.app_compat_time_list_textView_time.text = format.format(calendar)
            itemView.app_compat_time_list_textView_cantidad.text = dispositivo.nombre.plus(" Gramos")
        }
     }
}
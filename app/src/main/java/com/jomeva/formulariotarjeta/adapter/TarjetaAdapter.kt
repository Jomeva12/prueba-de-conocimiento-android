package com.jomeva.formulariotarjeta.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.jomeva.formulariotarjeta.R
import com.jomeva.formulariotarjeta.model.Tarjeta
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class TarjetaAdapter(
    options: FirestoreRecyclerOptions<Tarjeta?>?,
    var context: Context?
) :
    FirestoreRecyclerAdapter<Tarjeta, TarjetaAdapter.ViewHolder>(
        options!!
    ) {

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        mTarjeta: Tarjeta
    ) {
        val titular= holder.itemView.findViewById<TextView>(R.id.titular)
        val numeroTarjeta = holder.itemView.findViewById<TextView>(R.id.numeroTarjeta)
        val imagen = holder.itemView.findViewById<CircleImageView>(R.id.imagen)
        titular.text=mTarjeta.titular
        numeroTarjeta.text=mTarjeta.numeroTarjeta
       val img=mTarjeta.imagenUrl
        if (img != "") {
            Picasso.get().load(img).into(imagen)
        } else {
            Picasso.get().load(R.drawable.camera).into(imagen)
        }
    }

    public class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_tarjeta, parent, false)
        return ViewHolder(view)

    }


}

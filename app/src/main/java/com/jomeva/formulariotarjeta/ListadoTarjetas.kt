package com.jomeva.formulariotarjeta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.jomeva.formulariotarjeta.adapter.TarjetaAdapter
import com.jomeva.formulariotarjeta.model.Tarjeta
import com.jomeva.formulariotarjeta.provider.TarjetaProvider

class ListadoTarjetas : AppCompatActivity() {
    private lateinit var reciclerViewTarjetas: RecyclerView
    lateinit var mlinearLayoutManager: LinearLayoutManager
    lateinit var mTarjetaAdapter: TarjetaAdapter
    private lateinit var mTarjetaProvider: TarjetaProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_tarjetas)
        mTarjetaProvider=TarjetaProvider()
        reciclerViewTarjetas = findViewById(R.id.reciclerViewTarjetas)
        mlinearLayoutManager = LinearLayoutManager(this)
        reciclerViewTarjetas.layoutManager = this.mlinearLayoutManager

        var query: Query =
            mTarjetaProvider.getAllTarjetas()

        var options: FirestoreRecyclerOptions<Tarjeta?> =
            FirestoreRecyclerOptions.Builder<Tarjeta>().setQuery(
                query,
                Tarjeta::class.java
            ).build()
        mTarjetaAdapter = TarjetaAdapter(options, this)
        reciclerViewTarjetas.adapter = this.mTarjetaAdapter
        mTarjetaAdapter.startListening()
        mTarjetaAdapter.notifyDataSetChanged()
    }
}

package com.jomeva.formulariotarjeta.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jomeva.formulariotarjeta.model.Tarjeta

class TarjetaProvider {
    private var mCollectionReference: CollectionReference =
        FirebaseFirestore.getInstance().collection("tarjeta")


    fun saveTarjeta(tarjeta: Tarjeta): Task<Void> {
        val id = mCollectionReference.document().id
        tarjeta.idTarjeta = id
        return mCollectionReference.document("${ tarjeta.idTarjeta}").set(tarjeta)
    }
    public fun getAllTarjetas(): Query {
        return mCollectionReference
    }
}

package com.jomeva.formulariotarjeta.provider

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.jomeva.formulariotarjeta.utils.CompressorBitmapImage


class ImageProvider  {

    var mstorage: StorageReference = FirebaseStorage.getInstance().reference


    constructor()


    public fun save(context: Context, uri: String, name: String): UploadTask? {
        Log.d("imagen12", "-> ${uri}")
        val imageByte: ByteArray? =
            CompressorBitmapImage.getImage(context, uri, 500, 500)
        Log.d("imagen13", "-> ${imageByte}")
        val storage = mstorage.child("images/").child(name)
        return imageByte?.let { storage.putBytes(it) }
    }

    public fun getStorage(): StorageReference {
        return mstorage
    }
//obtener imagen de la uri




}

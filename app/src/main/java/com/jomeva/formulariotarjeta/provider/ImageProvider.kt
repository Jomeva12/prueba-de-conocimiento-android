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
    lateinit var mContext: Context
    var mstorage: StorageReference
    var mUrl: Uri? = null
    var index: Int = 0


    constructor() {
        mstorage = FirebaseStorage.getInstance().reference
    }


    public fun save(context: Context, uri: String, name: String): UploadTask? {
        Log.d("uriaggaa", "${uri}")
        val imageByte: ByteArray? =
            CompressorBitmapImage.getImage(context, uri, 500, 500)
        val storage = mstorage.child("images/").child(name)
        return imageByte?.let { storage.putBytes(it) }
    }
    public fun saveConsignaciones(context: Context, uri: String, name: String): UploadTask? {
        Log.d("uriaggaa", "${uri}")
        val imageByte: ByteArray? =
            CompressorBitmapImage.getImage(context, uri, 500, 500)
        val storage = mstorage.child("consignaciones/").child(name)
        return imageByte?.let { storage.putBytes(it) }
    }

    public fun deleteFile(name: String): Task<Void> {
        Log.d("borandoFile2", "${name}")
        return mstorage.child("audioRecord/$name").delete()
    }

    public fun getStorage(): StorageReference {
        return mstorage
    }
//obtener imagen de la uri




}

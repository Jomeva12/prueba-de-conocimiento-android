package com.jomeva.formulariotarjeta.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

// ...




object CompressorBitmapImage {

    /*
     * Metodo que permite comprimir imagenes y transformarlas a bitmap
     */
    fun getImage(
        ctx: Context?,
        path: String?,
        width: Int,
        height: Int
    ): ByteArray? {
        val file_thumb_path = File(path)
        var thumb_bitmap: Bitmap? = null
        try {
            thumb_bitmap = id.zelory.compressor.Compressor(ctx)
                .setMaxWidth(width)
                .setMaxHeight(height)
                .setQuality(75)
                .compressToBitmap(file_thumb_path)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val baos = ByteArrayOutputStream()
        thumb_bitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        return baos.toByteArray()
    }

}



package com.jomeva.formulariotarjeta

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jomeva.formulariotarjeta.model.Tarjeta
import com.jomeva.formulariotarjeta.provider.ImageProvider
import com.jomeva.formulariotarjeta.provider.TarjetaProvider
import java.io.File
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var imagen: ImageView
    private lateinit var mTarjetaProvider: TarjetaProvider
    private lateinit var nombre: EditText
    private lateinit var numeroTarjeta: EditText
    private lateinit var guardar: Button
    private lateinit var imageSelectionLauncher: ActivityResultLauncher<String>
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var cargando: LottieAnimationView
    //imagen
    private var imagenUriTarjeta: Uri? = null
    private lateinit var mImageProvider: ImageProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTarjetaProvider = TarjetaProvider()
        nombre = findViewById(R.id.nombre)
        numeroTarjeta = findViewById(R.id.numeroTarjeta)
        guardar = findViewById(R.id.guardar)
        imagen = findViewById(R.id.imagen)
        cargando = findViewById(R.id.cargando)
        mImageProvider = ImageProvider()
        var botonMenu = findViewById<FloatingActionButton>(R.id.botonMenu)
        botonMenu.setOnClickListener {
            val intent = Intent(this, ListadoTarjetas::class.java)
            startActivity(intent)
        }
        guardar.setOnClickListener {
            validarSiGuardaImagen()
        }
        imagen.setOnClickListener {

            permisosImagenes()

        }
        //imagen
        imageSelectionLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                // Aquí puedes manejar el resultado de la selección de la imagen
                if (uri != null) {
                    imagen.setImageURI(uri)
                    imagenUriTarjeta = uri
                }
            }
    }

    private fun crearTarjeta(url: String) {
        var tarjeta = Tarjeta()
        tarjeta.numeroTarjeta = numeroTarjeta.text.toString()
        tarjeta.titular = nombre.text.toString()
        tarjeta.imagenUrl = url
        mTarjetaProvider.saveTarjeta(tarjeta).addOnSuccessListener {
            cargando.visibility = View.GONE
            Toast.makeText(
                this,
                "Tarjeta guardada con éxito",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun validarSiGuardaImagen() {

        var camposVacios = false
        val nombreText = nombre.text.toString().trim()
        val numeroTarjetaText = numeroTarjeta.text.toString().trim()
        if (nombreText.isEmpty()) {
            camposVacios = true
        }
        if (numeroTarjetaText.isEmpty()) {
            camposVacios = true
        }
        if (imagenUriTarjeta == null) {
            camposVacios = true
        }


        if (!camposVacios) {
            cargando.visibility = View.VISIBLE
            saveImage()

        } else {

            Toast.makeText(
                this,
                "debe cargar la foto de la tarjeta y los campos deben estar llenos",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun saveImage() {
        val nombre = "${Date().time}.jpg"
        val fileImage = getPathFromUri(this, imagenUriTarjeta!!)

        mImageProvider.save(this, fileImage!!, nombre)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val storageRef = mImageProvider.getStorage().child("images").child(nombre)
                    storageRef.downloadUrl.addOnSuccessListener { image ->
                        val url: String = image.toString()
                        crearTarjeta(url)
                    }.addOnFailureListener { exception ->
                        // Manejar la falla de obtener la URL de descarga
                    }
                } else {
                    // No se pudo guardar
                }
            }

    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val contentResolver = context.contentResolver

        // Check if the Uri scheme is "content"
        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            // Use DocumentFile to handle SAF (Storage Access Framework) documents
            val documentFile = DocumentFile.fromSingleUri(context, uri)
            if (documentFile != null && documentFile.exists()) {
                // Get the file name
                val fileName = getFileName(context, uri)

                // Create a temporary file and copy the content into it
                val tempFile = File(context.cacheDir, fileName)
                contentResolver.openInputStream(uri)?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                filePath = tempFile.absolutePath
            }
        }

        // If the Uri scheme is not "content" or DocumentFile failed, try to get the file path through other means
        if (filePath == null) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    filePath = it.getString(columnIndex)
                }
            }
        }

        return filePath
    }

    fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameColumn = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameColumn != -1) {
                    fileName = it.getString(displayNameColumn)
                }
            }
        }
        return fileName
    }
    fun permisosImagenes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permisio = arrayOf<String>(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                requestPermissions(permisio, CAMERA_PERMISSION_REQUEST_CODE)

            } else {
                openCamara()
            }

        } else {
            openCamara()
        }

    }
    var imagenUri: Uri? = null
    private fun openCamara() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DESCRIPTION, "camara")
        imagenUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenUri)

        camaraLauncher.launch(intent)
    }
    val camaraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {
            if (it.resultCode == RESULT_OK) {
                try {
                        imagen.setImageURI(imagenUri)
                        imagenUriTarjeta = imagenUri

                } catch (e: Exception) {

                }
            }
        }
    )
}

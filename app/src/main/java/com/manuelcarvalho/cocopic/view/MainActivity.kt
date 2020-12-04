package com.manuelcarvalho.cocopic.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.manuelcarvalho.cocopic.R
import com.manuelcarvalho.cocopic.utils.formatString
import com.manuelcarvalho.cocopic.utils.getResizedBitmap
import com.manuelcarvalho.cocopic.utils.sendEmail
import com.manuelcarvalho.cocopic.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.fragment_first.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AppViewModel

    private val STORAGE_PERMISSION_CODE = 101
    private val CAMERA_PERMISSION_CODE = 105
    private val PHOTO_PERMISSION_CODE = 106

    private val filepath = "MyFileStorage"
    internal var myExternalFile: File? = null

    private val fileName = "image.asm"
    private var bitmapW = 128
    private var bitmapH = 64

    private var isLoaded = false
    private var is4color = false

    private var workBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        readSettings()


        viewModel = ViewModelProviders.of(this)[AppViewModel::class.java]
        viewModel.seekBarProgress.value = 50


        checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_CODE
        )

        checkPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_CODE
        )


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->

            createFile()
            createUri()

            sendEmail(this, createUri()!!)
        }


        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.c64bitmap, R.id.cocobitmap, R.id.vzbitmap, R.id.vz4bit -> {
                //item.isChecked = !item.isChecked
                item.isChecked = !item.isChecked
                if (item.itemId == R.id.c64bitmap) {
                    bitmapW = 256
                    bitmapH = 192
                    is4color = true
                    saveSettings()

                }
                if (item.itemId == R.id.vzbitmap) {
                    bitmapW = 128
                    bitmapH = 64
                    is4color = false
                    saveSettings()
                }
                if (item.itemId == R.id.cocobitmap) {
                    bitmapW = 256
                    bitmapH = 192
                    is4color = false
                    saveSettings()

                }
                if (item.itemId == R.id.vz4bit) {
                    bitmapW = 128
                    bitmapH = 64
                    is4color = true
                    Log.d(TAG, "Menu ${is4color}")
                    saveSettings()

                }
                if (item.itemId == R.id.vzDraw) {
                    bitmapW = 128
                    bitmapH = 64
                    is4color = true
                    setDrawMode()

                }
                Toast.makeText(this, "$item ", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_camera -> {
                capturePhoto()
                return true
            }
            R.id.action_image -> {
                captureImage()   //decodeVZImage()

                return true
            }
            R.id.action_refresh -> {
                redoImage()   //decodeVZImage()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun readSettings() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        //val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key)
        val savedBitmapW = sharedPref.getInt("bitmapDim", 128)

        when (savedBitmapW) {
            128 -> {
                bitmapW = 128
                bitmapH = 64
                is4color = false
                this.title = "VZ200 2 color"
            }
            257 -> {
                bitmapW = 256
                bitmapH = 192
                is4color = true
                this.title = "CoCo2 4 color"
            }
            256 -> {
                bitmapW = 256
                bitmapH = 192
                is4color = false
                this.title = "CoCo2 2 color"
            }
            129 -> {
                bitmapW = 128
                bitmapH = 64
                is4color = true
                this.title = "VZ200 4 color"
            }
        }
        invalidateOptionsMenu()

    }

    private fun saveSettings() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        Log.d(TAG, "4 col =  ${is4color} ${bitmapW}")
        with(sharedPref.edit()) {
            if (is4color && bitmapW == 128) {
                putInt("bitmapDim", 129)
            } else if (is4color && bitmapW == 256) {
                putInt("bitmapDim", 257)
            } else {
                putInt("bitmapDim", bitmapW)
            }
            commit()
        }
        readSettings()
    }

    private fun redoImage() {
        if (workBitmap != null && is4color) {
            //viewModel.decodeBitmap(workBitmap!!)
            viewModel.decode4ColorsBitmapVZ(workBitmap!!)
        } else {
            workBitmap?.let { viewModel.decodeBitmapVZ(it) }
        }
    }

    private fun createFile() {
        myExternalFile = File(getExternalFilesDir(filepath), fileName)
        try {
            val fileOutPutStream = FileOutputStream(myExternalFile)
            fileOutPutStream.write(formatString.toByteArray())
            fileOutPutStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Toast.makeText(applicationContext, "data save", Toast.LENGTH_SHORT).show()
    }


    private fun createUri(): Uri? {
        val requestFile = File(getExternalFilesDir(filepath), fileName)

        // Use the FileProvider to get a content URI
        val fileUri: Uri? = try {
            FileProvider.getUriForFile(
                this@MainActivity,
                "com.manuelcarvalho.cocopic.fileprovider",
                requestFile
            )
        } catch (e: IllegalArgumentException) {
            Log.e(
                "File Selector",
                "The selected file can't be shared: $requestFile"
            )
            null
        }
        //Log.e(TAG,"Uri ${fileUri}")
        return fileUri
    }

    private fun captureImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        )

        startActivityForResult(intent, PHOTO_PERMISSION_CODE)
    }

    private fun capturePhoto() {

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PERMISSION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_PERMISSION_CODE && data != null) {
            val newPhoto = (data.extras?.get("data") as Bitmap)
            val newImage = getResizedBitmap(newPhoto, bitmapW, bitmapH)
            if (newImage != null) {
                workBitmap = newImage
            }

            if (newImage != null) {
                progressBar.isVisible = true

                if (bitmapW == 128 && is4color == false) {

                    viewModel.decodeBitmapVZ(newImage)


                }
                if (is4color) {
                    viewModel.decode4ColorsBitmapVZ(newImage)
                }
            }

        }

        if (resultCode == Activity.RESULT_OK && requestCode == PHOTO_PERMISSION_CODE && data != null) {

            val imageUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

            progressBar.isVisible = true
            val newImage1 = getResizedBitmap(bitmap, bitmapW, bitmapH)
            if (newImage1 != null) {
                workBitmap = newImage1
            }
            if (newImage1 != null) {
                progressBar.isVisible = true

                if (bitmapW == 128 && is4color == false) {

                    //viewModel.decodeBitmap(workBitmap!!)
                    viewModel.decodeBitmapVZ(newImage1)

                }
                if (is4color) {
                    viewModel.decode4ColorsBitmapVZ(newImage1)
                }
            }
        }
    }

    private fun observeViewModel() {
        Log.d(TAG, "ObserveViewModel started")
        viewModel.newImage.observe(this, Observer { image ->
            image?.let {
                imageView.setImageBitmap(image)

            }
        })

        viewModel.progress.observe(this, Observer { progress ->
            progress?.let {
                progressBar.progress = progress
            }
        })

        viewModel.menuRedo.observe(this, Observer { menu ->
            Log.d(TAG, "menu changed")
            menu?.let {
                if (menu) {
                    isLoaded = true
                    invalidateOptionsMenu()
                } else {
                    isLoaded = false
                    invalidateOptionsMenu()
                }
            }
        })


    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(6)?.isEnabled = isLoaded
        when (bitmapW) {
            128 -> {
                if (is4color) {
                    menu?.getItem(2)?.isChecked = true
                } else {
                    menu?.getItem(3)?.isChecked = true
                }
            }
            256 -> {
                if (is4color) {
                    menu?.getItem(0)?.isChecked = true
                } else {
                    menu?.getItem(1)?.isChecked = true
                }
            }
        }
        return true
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission)
            == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(permission),
                requestCode
            )
        } else {
            Toast.makeText(
                this@MainActivity,
                "Permission already granted",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    fun setDrawMode() {

    }


}
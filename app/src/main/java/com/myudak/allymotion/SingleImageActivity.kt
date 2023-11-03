package com.myudak.allymotion

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.myudak.allymotion.databinding.ActivitySingleImageBinding

class SingleImageActivity : AppCompatActivity() {

    lateinit var binding:ActivitySingleImageBinding
    private var imageUri : Uri?= null
    private var REQUESTCODE_CAMERA = 1;
    private var REQUESTCODe_GALERRY = 2;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleImageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initClicks()
    }

    private fun initClicks() {
        binding.btnCamera.setOnClickListener{
            startIntentFromCamera()
        }

        binding.btnGalerry.setOnClickListener {
            startIntentFromGallery()
        }
    }

    private fun startIntentFromGallery() {
        val intent = Intent()

        intent.apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(intent,REQUESTCODe_GALERRY)
    }

    private fun startIntentFromCamera() {
        imageUri = null
        binding.preview.setImageBitmap(null)

        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(packageManager) != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE,"NEW IMAGE")
            values.put(MediaStore.Images.Media.DESCRIPTION,"ML KIT API COK")

            imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
            startActivityForResult(pictureIntent,REQUESTCODE_CAMERA)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == REQUESTCODE_CAMERA && resultCode == Activity.RESULT_OK ) {
            binding.preview.setImageURI(imageUri)
        } else if(requestCode == REQUESTCODe_GALERRY && resultCode == Activity.RESULT_OK) {
            imageUri = data?.let {
                it.data
            }?:run{
                null
            }
            binding.preview.setImageURI(imageUri)
        }
    }
}
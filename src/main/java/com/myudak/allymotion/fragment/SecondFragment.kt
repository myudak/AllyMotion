package com.myudak.allymotion.fragment


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import com.myudak.allymotion.R
import com.myudak.allymotion.ResultsFoodActivity
import com.myudak.allymotion.util.Classifier
import java.io.ByteArrayOutputStream


class SecondFragment : Fragment() {

    var mCapture: Button? = null
    var imageView: ImageView? = null
    var image_uri: Uri? = null
    private val mInputSize = 256
    private val mModelPath = "model.tflite"
    private val mLabelPath = "labels_food.txt"
    private lateinit var classifier: Classifier

    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        viewOfLayout = inflater.inflate(R.layout.activity_food, container, false)

        imageView = viewOfLayout.findViewById(R.id.iv1)
        mCapture = viewOfLayout.findViewById(R.id.btnPushup)
        initClassifier()

//        camera = viewOfLayout.findViewById(R.id.btn)
//        gallery = viewOfLayout.findViewById(R.id.btn2)
//        result = viewOfLayout.findViewById(R.id.result)
//        imageView = viewOfLayout.findViewById(R.id.imageView)
//
//        camera.setOnClickListener {view ->
//            Toast.makeText(
//                activity,"Success.",
//                Toast.LENGTH_SHORT).show()
//            if (ContextCompat.checkSelfPermission(context!!,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(cameraIntent, 3)
//            } else {
//                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
//            }
//        }
//
//        gallery.setOnClickListener {
//            val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(cameraIntent, 1)
//        }

        //button click action
        //mCapture.setOnClickListener(buttonlistener)
        viewOfLayout.findViewById<Button>(R.id.btnPushup).setOnClickListener(buttonListener)
        viewOfLayout.findViewById<ImageView>(R.id.infoFood).setOnClickListener{
            showDialog()
        }
        //pred(imageView!!)
        return viewOfLayout
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_infofood)

        dialog.show()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.Transparent.toArgb()))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }


    private val buttonListener = View.OnClickListener {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, PERMISSION_CODE)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                // Handle permission denied case
                Toast.makeText(context,"IZIN TIDAK DITERIMA :MAD:", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri =
            context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //Camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    fun onRequestPermissionResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(context, "No Permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun initClassifier() {
        classifier = Classifier(context!!.assets, mModelPath, mLabelPath, mInputSize)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        ) //called when image was captured from camera
        if (resultCode == Activity.RESULT_OK) { //set the image captured to our ImageView
//Toast.makeText(this, "Display Pic", Toast.LENGTH_SHORT).show();
            imageView!!.setImageURI(null)
            imageView!!.setImageURI(image_uri)
            imageView!!.rotation = 90f
            //val bitmap = ((view as ImageView).drawable as BitmapDrawable).bitmap
            pred(imageView!!)

        }

    }

    fun pred(view: ImageView){
        val bitmap = ((view as ImageView).drawable as BitmapDrawable).bitmap
        val result = classifier.recognizeImage(bitmap)
        //runOnUiThread { Toast.makeText(this, result.get(0).title, Toast.LENGTH_SHORT).show() }
        var size = result.size

        val filename = "bitmap.png"
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        var stream = context!!.openFileOutput(filename, Context.MODE_PRIVATE)
        stream.write(bytes.toByteArray())
        stream.close()

        //bitmap.recycle()


        //runOnUiThread { Toast.makeText(this, size.toString(), Toast.LENGTH_SHORT).show() }
        if (size==0){
            requireActivity().runOnUiThread { Toast.makeText(context, "No item found", Toast.LENGTH_SHORT).show() }
            //val intent = Intent(this, results::class.java).putExtra("result", "chicken curry")
            //intent.putExtra("image", filename)
            //startActivity(intent)
        }
        else{
            //runOnUiThread { Toast.makeText(this, result.get(0).title, Toast.LENGTH_SHORT).show() }
            val intent = Intent(context, ResultsFoodActivity::class.java).putExtra("result", result.get(0).title)
            intent.putExtra("image", filename)
            startActivity(intent)
        }

    }
    companion object {
        private const val PERMISSION_CODE = 1000
        private const val IMAGE_CAPTURE_CODE = 1001
    }
}
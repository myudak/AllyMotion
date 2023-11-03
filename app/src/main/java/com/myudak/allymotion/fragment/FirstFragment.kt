package com.myudak.allymotion.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.myudak.allymotion.NofapActivity
import com.myudak.allymotion.R
import com.myudak.allymotion.util.mlkit.activity.CameraXLivePreviewActivity
import com.myudak.allymotion.util.mlkit.activity.LivePreviewActivity

// TODO: Rename parameter arguments, choose names that match
class FirstFragment : Fragment() {

    private lateinit var viewOfLayout: View
    private var olgaValue: String? = "pushup"
    private lateinit var sharedPreferences: SharedPreferences

    private fun launchLivePreview(exercise: String) {
        val intent = Intent(context, LivePreviewActivity::class.java)
        intent.putExtra("button", exercise)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater.inflate(R.layout.fragment_first, container, false)


        sharedPreferences =  activity!!.applicationContext.getSharedPreferences("highscore", Context.MODE_PRIVATE)
        var highscore : TextView = viewOfLayout.findViewById(R.id.txtHighscore)
        highscore.text =  "highscoree " + sharedPreferences.getString("bob", "0")

        val refreshButton= viewOfLayout.findViewById<ImageView>(R.id.REFRESH)
           .setOnClickListener {
            // Refresh the high score and update the TextView
            refreshHighscore()
        }
        var btnSquat = viewOfLayout.findViewById<AppCompatButton>(R.id.btnSquat)
        var btnPushup = viewOfLayout.findViewById<AppCompatButton>(R.id.btnPushup)

        btnSquat.setOnClickListener {
            checkAndRequestPermissions("squat")
//            launchLivePreview("squat")
        }

        btnPushup.setOnClickListener {
            checkAndRequestPermissions("pushup")
//            launchLivePreview("pushup")
        }

        return viewOfLayout
    }

    private fun refreshHighscore() {
        sharedPreferences =  activity!!.applicationContext.getSharedPreferences("highscore", Context.MODE_PRIVATE)
        var highscore : TextView = viewOfLayout.findViewById(R.id.txtHighscore)
        highscore.text =  "highscoree " + sharedPreferences.getString("bob", "0")

    }

    private fun checkAndRequestPermissions(olga : String) {
        this.olgaValue = olga

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            val permission = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permission, PERMISSION_CODE)
        } else {
            launchLivePreview(olga)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                olgaValue?.let { launchLivePreview(it) }
            } else {
                // Handle permission denied case
                Toast.makeText(context,"IZIN TIDAK DITERIMA :MAD:", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        private const val PERMISSION_CODE = 1000
    }
}
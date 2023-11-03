package com.myudak.allymotion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth


var settingString = String()
val sharedfile = "settingsfile"
var settingArray = ArrayList<Boolean>()

class SettingActivity : AppCompatActivity() {

    lateinit var weightEditText : EditText
    lateinit var heightEditText : EditText
    lateinit var calculateButton : Button
    lateinit var resultTextView : TextView

    val SWITCH = arrayListOf<String>("switch1","switch2","switch3","switch4","switch5","switch6","switch7" )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        var auth : FirebaseAuth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        var nameText : TextView = findViewById(R.id.nameSetting)
        var profilePicture : ImageView = findViewById(R.id.profileImageView)

        val parentView = profilePicture.parent as ViewGroup?

        if (user != null) {
            if (user.displayName.isNullOrEmpty()) {
//                parentView?.removeView(profilePicture)
                nameText.text = user.email
            } else {
                nameText.text = user.displayName
                val profileImageUrl = user.photoUrl.toString()

                // Load the profile image into the ImageView using Glide library
                Glide.with(this)
                    .load(profileImageUrl)
                    .circleCrop() // This will make the image circular
                    .into(profilePicture)
            }

        }



        weightEditText  = findViewById(R.id.weightEditText)
        heightEditText   = findViewById(R.id.heightEditText)
        calculateButton  = findViewById(R.id.calculateButton)
        resultTextView  = findViewById(R.id.resultTextView)

        calculateButton.setOnClickListener {
            calculateBMI()
        }


        val btnSave = findViewById<Button>(R.id.save_settings)
        btnSave.setOnClickListener(View.OnClickListener {
            saveData()
            //var intent = Intent(this, MainAct::class.java).putExtra("setting", settingArray)
            //startActivity(intent)
            loadData()
            val sharedPreferences = getSharedPreferences(sharedfile, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            settingString=""
            for (i in settingArray){
                //settingString += i.toString()
                if (i == true){
                    settingString+="1"
                }
                else{
                    settingString+="0"
                }
            }
            editor.putString("settingstring", settingString)
            editor.apply()
            editor.commit()
            Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
        })

        //loadData()
        updateViews()
    }

    private fun calculateBMI() {
        val weight = weightEditText.text.toString().toFloat()
        val height = heightEditText.text.toString().toFloat() / 100 // Convert cm to meters

        val bmi = weight / (height * height)
        val bmiText = String.format("%.2f", bmi)

        val result = "BMI: $bmiText"

        resultTextView.text = result
    }

    fun saveData(){
        val sharedPreferences = getSharedPreferences(sharedfile, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        //val sw1 = findViewById<Switch>(R.id.milk)
        //val sw2 = findViewById<Switch>(R.id.egg)
        val switches = arrayOf(findViewById<Switch>(R.id.milk), findViewById<Switch>(R.id.egg),findViewById<Switch>(R.id.peanut),findViewById<Switch>(R.id.fish),findViewById<Switch>(R.id.wheat),findViewById<Switch>(R.id.shellfish),findViewById<Switch>(R.id.nuts))
        //sw1.isChecked = sharedPreferences.getBoolean()
        //editor.putBoolean(SWITCH1, sw1.isChecked)
        //editor.putBoolean(SWITCH2, sw2.isChecked)
        var i=0
        for(sw in switches){
            editor.putBoolean(SWITCH[i], sw.isChecked)
            i+=1
        }
        //settingString=""
        //for (i in settingArray){
        //settingString += i.toString()
        //   if (i == true){
        //       settingString+="1"
        //    }
        //    else{
        //       settingString+="0"
        //   }
        //}
        // editor.putString("settingstring", settingString)
        editor.apply()
        editor.commit()
        //Toast.makeText(applicationContext, settingString, Toast.LENGTH_LONG).show()

    }
    fun loadData(): ArrayList<Boolean> {
        val arrayList = ArrayList<Boolean>()
        val sharedPreferences = getSharedPreferences(sharedfile, MODE_PRIVATE)
        for(switchstring in SWITCH){
            arrayList.add(sharedPreferences.getBoolean(switchstring, false))
        }
        //val switchOnOff1 = sharedPreferences.getBoolean(SWITCH1, false);
        //val switchOnOff2 = sharedPreferences.getBoolean(SWITCH2, false);
        //arrayList.add(switchOnOff1)
        //arrayList.add(switchOnOff2)
        settingArray = arrayList
        return arrayList
    }
    fun updateViews(){
        val array = loadData()
        val switches = arrayOf(findViewById<Switch>(R.id.milk), findViewById<Switch>(R.id.egg),findViewById<Switch>(R.id.peanut),findViewById<Switch>(R.id.fish),findViewById<Switch>(R.id.wheat),findViewById<Switch>(R.id.shellfish),findViewById<Switch>(R.id.nuts))
        //val sw1 = findViewById<Switch>(R.id.milk)
        //val sw2 = findViewById<Switch>(R.id.egg)
        //sw1.setChecked(array[0])
        //sw2.setChecked(array[1])
        var i = 0
        for(sw in switches){
            sw.isChecked = array[i]
            i += 1
        }



    }
}
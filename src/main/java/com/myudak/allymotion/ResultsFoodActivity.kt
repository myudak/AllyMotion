package com.myudak.allymotion

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

class ResultsFoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results_food)
        var textView = findViewById<TextView>(R.id.result_tv)
        var textresult = findViewById<TextView>(R.id.result_tv2)
        var imageView = findViewById<ImageView>(R.id.iv2)
        val bundle = intent.extras
        val message = bundle?.getString("result")

        val filename = bundle?.getString("image")
        val instream = this.openFileInput(filename)
        val bitmap = BitmapFactory.decodeStream(instream)

        instream.close()
        imageView.setImageBitmap(bitmap)
        imageView!!.rotation = 90f

        // ================================= KALORI

        val foodCalories = mapOf(
            "kari ayam" to 109.9,
            "ayam goreng" to 245.9,
            "kue coklat" to 370.7,
            "cupcake" to 305,
            "donat" to 421,
            "siomay" to 147,
            "kentang goren" to 311.9,
            "nasi goreng" to 163,
            "garlic bread" to 349.9,
            "burger" to 294.9,
            "hotdog" to 289.7,
            "Es Krim" to 207.5,
            "omelet" to 153.7,
            "pizza" to 266,
            "samosa" to 348,
            "roti lapis" to 518,
            "sup ayam" to 36.1,
            "lumpia" to 101,
            "sushi" to 143,
            "wafel" to 291.1
        )

        val kalori = foodCalories[message] ?: ""

        // =======================================

        textView.setText(message + "\n Kalori per 100g : " + kalori)

        val settings = getSharedPreferences(sharedfile, MODE_PRIVATE)
        val str = settings.getString("settingstring", "0000000")
        val arrayList = arrayListOf<String>("susu", "Telur","Kacang tanah", "Ikan", "Gandum","kerang","Kacang-kacangan")
        var stri = ""


        var i=0
        csvReader().open(assets.open("allergies.csv")) {
            readAllAsSequence().forEach { row ->
                if (row[0] == message){
                    if (str != null) {
                        for (allergen in str){                     // str is setting string eg. 1011001
                            if(allergen=='1' && row[i+1]=="1"){
                                stri+="Kemungkinan mengandung "+arrayList[i]+".\n"
                            }
                            i+=1
                        }
                    }
                }
            }
        }
        if(stri==""){
            stri = "Tidak ada alergi."
        }

        textresult.setText(stri)
    }

}

package com.myudak.allymotion

import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.myudak.allymotion.widget.NofapWidget
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class NofapActivity : AppCompatActivity() {

    lateinit var daysCountTextView : TextView
    private var startDate: Date? = null
    lateinit var menitCount : TextView
    lateinit var btnResetNofap : AppCompatButton
    private lateinit var stopwatchHandler: Handler
    private lateinit var sharedPreferences: SharedPreferences

    private val REQUEST_ADD_WIDGET = 123
    private lateinit var pinActivityResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        private const val PREF_CUSTOM_START_DATE = "pref_custom_start_date"
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nofap)

        supportActionBar?.hide();


        pinActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Widget pinned successfully
                Toast.makeText(this, "Widget pinned to Home Screen!", Toast.LENGTH_SHORT).show()
            } else {
                // Widget pinning failed
                Toast.makeText(this, "Widget pinning failed!", Toast.LENGTH_SHORT).show()
            }
        }

        // Add a button click listener to request widget pinning
        val btnAddToHomeScreen = findViewById<TextView>(R.id.addBtnWidgetNofap)
        btnAddToHomeScreen.setOnClickListener {
            requestWidgetPinning()
        }

        daysCountTextView = findViewById(R.id.textNofapHari)
        menitCount = findViewById(R.id.textNofapJam)
        btnResetNofap = findViewById(R.id.btnResetNofap)
        stopwatchHandler = Handler()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        var customStartDate = sharedPreferences.getString(PREF_CUSTOM_START_DATE, null)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


        if (customStartDate != null) {
            startDate = dateFormat.parse(customStartDate)
        } else {
            customStartDate = getCurrentDateTimeFormatted()
            startDate = dateFormat.parse(customStartDate)
            sharedPreferences.edit().putString(PREF_CUSTOM_START_DATE, customStartDate).apply()
        }

        btnResetNofap.setOnClickListener{
            datePickerDialog()

        }

        startStopwatch()
    }

    fun getCurrentDateTimeFormatted(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun datePickerDialog() {
        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        val initialHour = calendar.get(Calendar.HOUR_OF_DAY)
        val initialMinute = calendar.get(Calendar.MINUTE)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                // Set the date
                val newStartDate = Calendar.getInstance()
                newStartDate.set(year, month, day)

                val timePickerDialog = TimePickerDialog(
                    this,
                    { _: TimePicker, hourOfDay: Int, minute: Int ->
                        // Set the time
                        newStartDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        newStartDate.set(Calendar.MINUTE, minute)
                        newStartDate.set(Calendar.SECOND, 0)
                        startDate = newStartDate.time

                        // Save custom strat date
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val customStartDate = dateFormat.format(startDate)
                        sharedPreferences.edit().putString(PREF_CUSTOM_START_DATE, customStartDate).apply()
                        Toast.makeText(this,"SAVED", Toast.LENGTH_SHORT).show()
                    },
                    initialHour,
                    initialMinute,
                    true
                )
                timePickerDialog.show()
            },
            initialYear,
            initialMonth,
            initialDay
        )
        datePickerDialog.show()

    }

    private fun startStopwatch() {
        stopwatchHandler.post(object : Runnable {
            override fun run() {
                val currentTime = Calendar.getInstance().time
                val elapsedMilliseconds = currentTime.time - startDate!!.time

                val days = (elapsedMilliseconds / (1000 * 60 * 60 * 24)).toInt()
                val hours = (elapsedMilliseconds / (1000 * 60 * 60) % 24).toInt()
                val minutes = (elapsedMilliseconds / (1000 * 60) % 60).toInt()
                val seconds = (elapsedMilliseconds / 1000 % 60).toInt()

                val stopwatchText = String.format("%02d Jam %02d Menit %02d Detik",hours, minutes,seconds)

               daysCountTextView.text = days.toString()
                menitCount.text = stopwatchText
                stopwatchHandler.postDelayed(this, 1000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopwatchHandler.removeCallbacksAndMessages(null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestWidgetPinning() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val myProvider = ComponentName(this, NofapWidget::class.java)

        val b = Bundle()
        b.putString("ggg", "ggg")

        if (appWidgetManager.isRequestPinAppWidgetSupported) {



            val pinWidgetProviderIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)

            val successCallback = PendingIntent.getBroadcast(
                this, 0,
                pinWidgetProviderIntent, PendingIntent.FLAG_IMMUTABLE
            )
            appWidgetManager.requestPinAppWidget(myProvider, b,successCallback)
        } else {
            // Widget pinning not supported on this device
            Toast.makeText(this, "Widget pinning is not supported on this device!", Toast.LENGTH_SHORT).show()
        }
    }
}
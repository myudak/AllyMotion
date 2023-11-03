package com.myudak.allymotion.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.myudak.allymotion.MainActivity
import com.myudak.allymotion.NofapActivity
import com.myudak.allymotion.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Implementation of App Widget functionality.
 */
class NofapWidget : AppWidgetProvider() {

    companion object {
        const val PREF_CUSTOM_START_DATE = "pref_custom_start_date"
    }

    private var startDate: Date? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }



    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    var customStartDate = sharedPreferences.getString(NofapWidget.PREF_CUSTOM_START_DATE, null)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val views = RemoteViews(context.packageName, R.layout.nofap_widget)

    if (customStartDate != null) {
        val startDate = dateFormat.parse(customStartDate)

        val currentTime = Calendar.getInstance().time
        val elapsedMilliseconds = currentTime.time - startDate!!.time

        val days = (elapsedMilliseconds / (1000 * 60 * 60 * 24)).toInt()

        views.setTextViewText(R.id.appwidget_text, "$days hari")
    }

    else {
        val widgetText = context.getString(R.string.appwidget_text)
        // Construct the RemoteViews object
        views.setTextViewText(R.id.appwidget_text,widgetText)
    }



    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
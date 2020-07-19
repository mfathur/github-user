package com.mfathurz.githubuser.ui.setting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.ui.main.MainActivity
import java.util.*

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMINDER: String
    private lateinit var LANGUAGE: String

    private lateinit var reminderPreference: SwitchPreference
    private lateinit var languagePreference: Preference

    private val currentLanguage = Locale.getDefault().displayLanguage
    private var isActive = false

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    private fun init() {
        REMINDER = resources.getString(R.string.key_reminder)
        LANGUAGE = resources.getString(R.string.key_language)

        reminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
        languagePreference = findPreference<Preference>(LANGUAGE) as Preference

        languagePreference.setOnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            return@setOnPreferenceClickListener true
        }
        sendNotification()
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        languagePreference.summary = sh.getString(LANGUAGE, currentLanguage)
        reminderPreference.disableDependentsState = sh.getBoolean(REMINDER,false)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == LANGUAGE) {
            languagePreference.summary = sharedPreferences.getString(LANGUAGE, currentLanguage)

        }

        if (key == REMINDER){
            reminderPreference.disableDependentsState = sharedPreferences.getBoolean(REMINDER,false)
            isActive = sharedPreferences.getBoolean(REMINDER,false)
            Toast.makeText(activity,"$isActive",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendNotification(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(requireContext(), NOTIFICATION_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val mNotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setContentTitle(getString(R.string.reminder))
            .setContentText(getString(R.string.find_popular_user))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)

            builder.setChannelId(CHANNEL_ID)

            mNotificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = builder.build()

        mNotificationManager.notify(NOTIFICATION_ID,notification)

    }

    companion object{
        private const val CHANNEL_ID = "channel_1"
        private const val CHANNEL_NAME = "notification_get_user_back"
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_REQUEST_CODE = 200
    }

}
package com.mfathurz.githubuser.ui.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.mfathurz.githubuser.AlarmReceiver
import com.mfathurz.githubuser.R
import java.util.*

class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var REMINDER: String
    private lateinit var LANGUAGE: String

    private lateinit var reminderPreference: SwitchPreference
    private lateinit var languagePreference: Preference

    private val currentLanguage = Locale.getDefault().displayLanguage
    private var isActive = false

    private lateinit var alarmReceiver: AlarmReceiver

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
        alarmReceiver = AlarmReceiver()
        REMINDER = resources.getString(R.string.key_reminder)
        LANGUAGE = resources.getString(R.string.key_language)

        reminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
        languagePreference = findPreference<Preference>(LANGUAGE) as Preference

        languagePreference.setOnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            return@setOnPreferenceClickListener true
        }
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        languagePreference.summary = sh.getString(LANGUAGE, currentLanguage)
        reminderPreference.disableDependentsState = sh.getBoolean(REMINDER, false)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == LANGUAGE) {
            languagePreference.summary = sharedPreferences.getString(LANGUAGE, currentLanguage)

        }

        if (key == REMINDER) {
            reminderPreference.disableDependentsState =
                sharedPreferences.getBoolean(REMINDER, false)
            isActive = sharedPreferences.getBoolean(REMINDER, false)
            if (isActive) {
                alarmReceiver.setRepeatingAlarm(requireContext())
            } else {
                alarmReceiver.cancelAlarm(requireContext())
            }
        }
    }

}
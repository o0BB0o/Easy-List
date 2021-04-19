package com.yjiang45.project3.ui.main

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.yjiang45.project3.MainActivity
import com.yjiang45.project3.MainActivity.Companion.DARK_MODE
import com.yjiang45.project3.MainActivity.Companion.DELETE_ALL
import com.yjiang45.project3.MainActivity.Companion.SHOW_UNCHECKED

import com.yjiang45.project3.R
import kotlinx.android.synthetic.main.settings_fragment.*

class Settings : Fragment() {
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }
    //private lateinit var viewModel: SettingsViewModel

    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        check_switch.isChecked = prefs.getBoolean(SHOW_UNCHECKED,false)
        check_switch.setOnCheckedChangeListener{ _, isChecked ->
            with(prefs.edit()){
                putBoolean(SHOW_UNCHECKED, isChecked)
                apply()
            }
        }

        change_theme.isChecked = prefs.getBoolean(DARK_MODE,false)
        change_theme.setOnCheckedChangeListener{_, isChecked ->
            with(prefs.edit()) {
                putBoolean(DARK_MODE, isChecked)
                //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                changeTheme()
                apply()
            }
        }

        enable_deleteAll.isChecked = prefs.getBoolean(DELETE_ALL,false)
        enable_deleteAll.setOnCheckedChangeListener{_, isChecked ->
            with(prefs.edit()) {
                putBoolean(DELETE_ALL, isChecked)
                apply()
            }
        }
    }

    fun changeTheme(){
        if (prefs.getBoolean(DARK_MODE,false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }


}

package com.yjiang45.project3

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.yjiang45.project3.ui.main.MainFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.yjiang45.project3.ui.main.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private val vm: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.let {
                it.title = when (destination.id) {
                    R.id.settings -> "Settings"
                    R.id.detail -> "Details"
                    R.id.info2 -> "Info"
                    else -> getString(R.string.app_name)
                }
            }
        }
    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.navHostFragment).navigateUp()



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.info_menuItem ->{
                navHostFragment.navController.navigate(R.id.action_mainFragment_to_info2)
                true
            }

            R.id.settings_menuItem ->{
                navHostFragment.navController.navigate(R.id.action_mainFragment_to_settings)
                true
            }

            R.id.delete_All ->{
                itemDeletedAlert()
                true
            }

            else->super.onOptionsItemSelected(item)
        }

    }

    fun itemDeletedAlert() {//pop up alert that alerts user to unsure their move
        val msg = "Do you want to reset(delete) all items?"
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Warning")
            setMessage(msg)
            setNegativeButton("Cancel", null)
            setPositiveButton("OK", DialogInterface.OnClickListener{
                    dialog, id ->  vm.deleteAll()
            })
            show()
        }
    }
    companion object{
        const val SHOW_UNCHECKED = "show unchecked"
        const val DARK_MODE = "darkMode"
        const val DELETE_ALL = "delete all"
    }

}

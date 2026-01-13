package com.example.pag_baul

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pag_baul.databinding.ActivityMainBinding
import com.google.ar.core.ArCoreApk

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This function will silently check for ARCore and prevent the popup.
        // It won't stop the app from loading.
        checkArCoreAvailability()

        // Your original code to load the HomeFragment.
        // This will now run AFTER the AR check is handled.
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    /**
     * This function checks if ARCore is available. If it's not, it does nothing
     * and allows the app to continue, preventing the error popup.
     */
    private fun checkArCoreAvailability() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)

        if (availability.isSupported) {
            // ARCore is supported. Great, nothing to do.
            Log.d("AR_CHECK", "ARCore is supported and installed.")
        } else {
            // ARCore is not supported or not installed.
            // We do nothing and let the app continue. This prevents the popup.
            Log.d("AR_CHECK", "ARCore is not supported or not installed.")
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Allows user to go back to the previous fragment
            .commit()
    }

    override fun onBackPressed() {
        // This handles the back button press correctly.
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            // If there's only one fragment left (the HomeFragment), exit the app.
            finish()
        }
    }
}

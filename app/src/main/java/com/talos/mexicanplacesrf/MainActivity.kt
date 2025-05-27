package com.talos.mexicanplacesrf

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.talos.mexicanplacesrf.data.PlaceRepository
import com.talos.mexicanplacesrf.data.remote.RetrofitHelper
import com.talos.mexicanplacesrf.databinding.ActivityMainBinding
import com.talos.mexicanplacesrf.ui.adapters.fragments.PlacesListFragment
import com.talos.mexicanplacesrf.utils.Constants
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Thread.sleep(3000)
        var screenSplash = installSplashScreen()
        screenSplash.setKeepOnScreenCondition { false }

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // si es la priemra vez
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    PlacesListFragment()
                ).commit()
        }

    }
}
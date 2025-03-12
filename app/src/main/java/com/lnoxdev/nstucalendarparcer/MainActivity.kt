package com.lnoxdev.nstucalendarparcer

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.lnoxdev.data.netiSchedule.NetiScheduleLoader
import com.lnoxdev.data.toSchedule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var netiRepository: NetiScheduleLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            netiRepository.updateSchedule("АБс-223")
        }

        val main = findViewById<ConstraintLayout>(R.id.main)
        main.setOnClickListener {
            lifecycleScope.launch {
                val schedule = netiRepository.TESTFUN().toSchedule()
                Log.d("testlog", schedule.days[3].lessons.first().toString())
            }
        }
    }

}
package com.du21.mangtas_business_days_count

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.du21.mangtas_business_days_count.databinding.ActivityMainBinding
import com.du21.mangtas_business_days_count.entities.Date

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels { AppViewModelFactory }

    private lateinit var binding: ActivityMainBinding

    private var startDate: Date? = null
    private var endDate: Date? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    fun openPickStartDate(view: View) {
        DatePickerDialog(this).apply {
            setOnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.setStartDate(year, month, dayOfMonth)
            }
        }.show()
    }

    fun openPickEndDate(view: View) {
        DatePickerDialog(this).apply {
            setOnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.setEndDate(year, month, dayOfMonth)
            }
        }.show()
    }

}
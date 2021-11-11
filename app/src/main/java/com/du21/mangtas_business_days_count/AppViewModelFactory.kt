package com.du21.mangtas_business_days_count

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.du21.mangtas_business_days_count.common.DateValidatorUsingDateFormat
import com.du21.mangtas_business_days_count.utils.Constant


object AppViewModelFactory :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        ViewModelProvider.AndroidViewModelFactory
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                AppDataSource(),
                DateValidatorUsingDateFormat(Constant.DATE_FORMAT)
            ) as T
        }
        throw UnsupportedClassVersionError()
    }
}
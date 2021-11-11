package com.du21.mangtas_business_days_count.common

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

@BindingAdapter("app:textId")
fun setTextFromStringRes(view: TextView, @StringRes res: Int) {
    if (res == -1 || res == 0) {
        view.text = ""
    } else {
        view.setText(res)
    }
}
package com.example.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("textInputLayoutNameError")
fun bindTextInputLayoutNameError(textInputLayout: TextInputLayout, errorState: Boolean) {
    val message = if (errorState) {
        textInputLayout.context.getString(R.string.error_input_name)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("textInputLayoutCountError")
fun bindTextInputLayoutCountError(textInputLayout: TextInputLayout, errorState: Boolean) {
    val message = if (errorState) {
        textInputLayout.context.getString(R.string.error_input_count)
    } else {
        null
    }
    textInputLayout.error = message
}

@BindingAdapter("textDoubleToString")
fun bindDoubleToString(textInputEditText: TextInputEditText, count: Double) {
    textInputEditText.setText(count.toString())
}
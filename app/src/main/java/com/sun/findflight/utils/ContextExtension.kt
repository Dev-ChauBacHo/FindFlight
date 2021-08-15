package com.sun.findflight.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.sun.findflight.R

fun Context.showToast(message: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, length).show()
}

fun Context.showExplainDialog(intent: Intent) {
    val dialog = this.let { AlertDialog.Builder(it) }
    dialog.apply {
        setTitle(getString(R.string.text_required_permission))
        setMessage(getString(R.string.text_permission_explain))
        setPositiveButton(
            getString(R.string.text_open_setting)
        ) { _, _ ->
            startActivity(intent)
        }
        create()
        show()
    }
}

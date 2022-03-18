package com.local.growkart.util

import android.R
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class ToastUtil {
    companion object {
        fun showInfo(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun showErrorInfo(activity: Activity, error: String) {
            val snackbar = createErrorSnackBar(activity, error)
            snackbar.show()
        }

        private fun createErrorSnackBar(
            activity: Activity,
            error: String
        ) = createSnackBar(activity, error, R.color.holo_red_dark)

        private fun createSuccessSnackBar(
            activity: Activity,
            message: String
        ) = createSnackBar(activity, message, R.color.holo_green_dark)

        private fun createSnackBar(
            activity: Activity,
            error: String,
            color: Int
        ): Snackbar {
            val view = activity.findViewById<View>(R.id.content)
            val snackbar = Snackbar.make(view, error, Snackbar.LENGTH_LONG)
            val bgView = snackbar.view
            bgView.setBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    color
                )
            )
            return snackbar
        }

        fun showErrorWithAction(
            activity: Activity,
            error: String,
            action: String,
            actionEvent: View.OnClickListener
        ) {
            val snackbar = createErrorSnackBar(activity, error)
            snackbar.setAction(action, actionEvent)
            snackbar.show()
        }

        fun showSuccessInfo(activity: Activity, message: String, action: String = "Ok") {
            val snackbar = createSuccessSnackBar(activity, message)
            snackbar.setAction(action) { snackbar.dismiss() }
            snackbar.show()
        }
    }

}

interface Action {
    fun action();
}
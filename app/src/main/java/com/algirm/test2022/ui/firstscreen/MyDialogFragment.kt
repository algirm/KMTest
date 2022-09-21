package com.algirm.test2022.ui.firstscreen

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment

class MyDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val isPalindrome = arguments?.getBoolean("isPalindrome")
            val title = if (isPalindrome == true) "Is Palindrome" else "Not Palindrome"
            // Set the dialog title
            builder.setTitle(title)
                .setNeutralButton("OK") { _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyDialogFragment", "onDestroy: dialog destroyed")
    }

    companion object {
        fun newInstance(): MyDialogFragment {
            return MyDialogFragment()
        }
    }

}
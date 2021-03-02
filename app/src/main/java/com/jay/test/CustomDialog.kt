package com.jay.test

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class CustomDialog : DialogFragment() {

    var onPositiveClick: () -> Unit = { throw NotImplementedError() }
    var onNegativeClick: () -> Unit = { dismiss() }
    var title: String = "Title"
    var message: String = "Message"
    var positiveBtnText: String = "OK"
    var negativeBtnText: String = "Cancel"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireContext())

        builder.apply {
            setTitle(title)

            setMessage(message)

            setPositiveButton(positiveBtnText) { _, _ ->
                onPositiveClick()
            }
            setNegativeButton(negativeBtnText) { _, _ ->
                onNegativeClick()
            }
        }

        return builder.create()
    }
}
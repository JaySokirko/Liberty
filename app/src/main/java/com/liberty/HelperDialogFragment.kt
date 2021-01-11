package com.liberty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class HelperDialogFragment : DialogFragment() {

    var title: String = ""
    var message: String = ""
    var action: () -> Unit = { dismiss() }
    var actionButtonText = "Ok"

    private lateinit var titleTextView: TextView
    private lateinit var subtitleTextView: TextView
    private lateinit var actionBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_helper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView = view.findViewById(R.id.title_text_view)
        subtitleTextView = view.findViewById(R.id.subtitle_text_view)
        actionBtn = view.findViewById(R.id.action_button)

        titleTextView.text = title
        subtitleTextView.text = message
        actionBtn.text = actionButtonText
        actionBtn.setOnClickListener { action() }
    }

    fun show(manager: FragmentManager) {
        if (dialog?.isShowing == true) return
        super.show(manager, this::class.java.name)
    }
}
package com.jay.test

import android.Manifest
import android.Manifest.permission.READ_CONTACTS
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sokyrko.liberty.Liberty
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain

class PermissionsFragment: Fragment() {

    private lateinit var requestReadContactsBtn: Button
    private lateinit var readContactsResultTextView: TextView

    private val permissionResultReceiver = PermissionsResultReceiver()

    private val dialog: CustomDialog = CustomDialog().apply {
        title = "Dialog title"
        message = "Explain why your dialog requires permissions"
        positiveBtnText = "Settings"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_permissions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Liberty.init(fragment = this)

        requestReadContactsBtn = view.findViewById(R.id.single_permission_request_btn)
        readContactsResultTextView = view.findViewById(R.id.single_permission_request_text_view)

        requestReadContactsBtn.setOnClickListener {
            Liberty.requestPermission(permission = READ_CONTACTS, requestCode = REQUEST_READ_CONTACTS)
        }

        observeReadContactsRequestResult()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Liberty.onRequestPermissionsResult(receiver = permissionResultReceiver, requestCode, permissions, grantResults)
    }

    private fun observeReadContactsRequestResult() {
        permissionResultReceiver.readContactsRequestResult.observe(viewLifecycleOwner) { result: Liberty.RequestResult ->

            when (result) {
                Liberty.RequestResult.ALLOWED -> {
                    readContactsResultTextView.text = "Result: Allowed"
                    readContactsResultTextView.setTextColor(Color.GREEN)
                }
                Liberty.RequestResult.DENIED -> {
                    readContactsResultTextView.text = "Result: Denied"
                    readContactsResultTextView.setTextColor(requireContext().getColor(R.color.yellow))
                }
                Liberty.RequestResult.NEVER_ASK_AGAIN -> {
                    readContactsResultTextView.text = "Result: Never ask again"
                    readContactsResultTextView.setTextColor(Color.RED)
                }
            }
        }
    }
}
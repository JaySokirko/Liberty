package com.jay.test

import android.Manifest.permission.READ_CONTACTS
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sokyrko.liberty.Liberty


class PermissionsFragment: Fragment() {

    private lateinit var requestReadContactsBtn: Button
    private lateinit var requestReadStorageBtn: Button
    private lateinit var readContactsResultTextView: TextView
    private lateinit var readStorageResultTextView: TextView

    private val permissionResultReceiver = PermissionsResultReceiver()

    private var returnedFromSettings = false

    private val dialog: CustomDialog = CustomDialog().apply {
        title = "Some title"
        message = "Explain why your app requires permissions"
        positiveBtnText = "Settings"
        onPositiveClick = {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
            intent.data = uri
            startActivityForResult(intent, 300)
            returnedFromSettings = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_permissions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Liberty.init(fragment = this)

        requestReadContactsBtn = view.findViewById(R.id.single_permission_request_btn)
        requestReadStorageBtn = view.findViewById(R.id.single_permission_request_btn_2)
        readContactsResultTextView = view.findViewById(R.id.single_permission_request_text_view)
        readStorageResultTextView = view.findViewById(R.id.single_permission_request_text_view_2)

        requestReadContactsBtn.setOnClickListener {
            Liberty.requestPermission(permission = READ_CONTACTS, requestCode = REQUEST_READ_CONTACTS)
        }

        requestReadStorageBtn.setOnClickListener {
            Liberty.requestPermission(permission = READ_EXTERNAL_STORAGE, requestCode = REQUEST_READ_STORAGE)
        }

        observeReadContactsRequestResult()
        observeReadStorageRequestResult()

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
                    readContactsResultTextView.text = "Result: Don't ask again"
                    readContactsResultTextView.setTextColor(Color.RED)
                }
            }
        }
    }

    private fun observeReadStorageRequestResult() {
        permissionResultReceiver.readStorageRequestResult.observe(viewLifecycleOwner) { result: Liberty.RequestResult ->

            when (result) {
                Liberty.RequestResult.ALLOWED -> {
                    readStorageResultTextView.text = "Result: Allowed"
                    readStorageResultTextView.setTextColor(Color.GREEN)
                }
                Liberty.RequestResult.DENIED -> {
                    readStorageResultTextView.text = "Result: Denied"
                    readStorageResultTextView.setTextColor(requireContext().getColor(R.color.yellow))
                }
                Liberty.RequestResult.NEVER_ASK_AGAIN -> {
                    readStorageResultTextView.text = "Result: Don't ask again"
                    readStorageResultTextView.setTextColor(Color.RED)
                }
            }
        }
    }
}
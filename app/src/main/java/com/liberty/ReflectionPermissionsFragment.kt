package com.liberty

import android.Manifest.permission.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.liberty.Constants.REQUEST_CAMERA_AND_STORAGE_PERMISSIONS
import com.liberty.Constants.REQUEST_READ_CALENDAR
import com.liberty.Constants.REQUEST_READ_CONTACTS
import com.sokyrko.liberty.Liberty
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain
import com.sokyrko.liberty.annotation.PermissionName

class ReflectionPermissionsFragment : Fragment() {

    private lateinit var permissionsBtn: Button
    private lateinit var contactsBtn: Button
    private lateinit var calendarBtn: Button
    private lateinit var cameraPermissionHint: TextView
    private lateinit var storagePermissionHint: TextView
    private lateinit var contactsPermissionHint: TextView
    private lateinit var calendarPermissionHint: TextView

    private var isReadContactsAlreadyAllowed = false

    private var dialog = HelperDialogFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_permissions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Liberty.init(this)

        permissionsBtn = view.findViewById(R.id.permissions_button)
        contactsBtn = view.findViewById(R.id.contacts_button)
        calendarBtn = view.findViewById(R.id.calendar_button)
        cameraPermissionHint = view.findViewById(R.id.camera_hint_text_view)
        storagePermissionHint = view.findViewById(R.id.storage_hint_text_view)
        contactsPermissionHint = view.findViewById(R.id.contacts_hint_text_view)
        calendarPermissionHint = view.findViewById(R.id.calendar_hint_text_view)

        onPermissionsBtnClick()
        onReadContactsBtnClick()
        onReadCalendarsBtnClick()

        Liberty.isHavePermission(READ_EXTERNAL_STORAGE) {
            Log.d("TAG", "onViewCreated: ")
        }

        Liberty.isHavePermissions(READ_EXTERNAL_STORAGE, CAMERA) {
            Log.d("TAG", "onViewCreated: ")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Liberty.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onDestroyView() {
        Liberty.clear()
        super.onDestroyView()
    }

    private fun onPermissionsBtnClick() {
        permissionsBtn.setOnClickListener {
            Liberty.requestPermissions(
                READ_EXTERNAL_STORAGE,
                CAMERA,
                requestCode = REQUEST_CAMERA_AND_STORAGE_PERMISSIONS)
        }
    }

    private fun onReadContactsBtnClick() {
        contactsBtn.setOnClickListener {
            Liberty.requestPermission(READ_CONTACTS, requestCode = REQUEST_READ_CONTACTS)
        }
    }

    private fun onReadCalendarsBtnClick() {
        calendarBtn.setOnClickListener {
            Liberty.requestPermission(READ_CALENDAR, requestCode = REQUEST_READ_CALENDAR)
        }
    }

    @OnAllowed(REQUEST_CAMERA_AND_STORAGE_PERMISSIONS)
    fun onPermissionsAllowed(@PermissionName permission: String) {
        when(permission) {
            CAMERA -> { cameraPermissionHint.setOnAllowed() }
            READ_EXTERNAL_STORAGE -> { storagePermissionHint.setOnAllowed() }
        }
    }

    @OnDenied(REQUEST_CAMERA_AND_STORAGE_PERMISSIONS)
    fun onPermissionsDenied(@PermissionName permission: String) {
        when(permission) {
            CAMERA -> { cameraPermissionHint.setOnDenied() }
            READ_EXTERNAL_STORAGE -> { storagePermissionHint.setOnDenied() }
        }
    }

    @OnNeverAskAgain(REQUEST_CAMERA_AND_STORAGE_PERMISSIONS)
    fun onNeverAskAgain(@PermissionName permission: String) {
        when(permission) {
            CAMERA -> { cameraPermissionHint.setOnNeverAskAgain() }
            READ_EXTERNAL_STORAGE -> { storagePermissionHint.setOnNeverAskAgain() }
        }
    }

    @OnAllowed(REQUEST_READ_CONTACTS)
    fun onContactsPermissionAllowed() {
        contactsPermissionHint.setOnAllowed()

        if(isReadContactsAlreadyAllowed) {
            dialog.apply {
                title = "Permission already granted"
                message = "Do your stuff"
                action = { dismiss() }
            }

            dialog.show(requireFragmentManager())
        }

        isReadContactsAlreadyAllowed = true
    }

    @OnDenied(REQUEST_READ_CONTACTS)
    fun onContactsPermissionDenied() {
        contactsPermissionHint.setOnDenied()
    }

    @OnNeverAskAgain(REQUEST_READ_CONTACTS)
    fun onContactsPermissionNeverAskAgain() {
        contactsPermissionHint.setOnNeverAskAgain()

        dialog.apply {
            title = "The user has clicked \"never ask again\""
            message = "Explain why do you need this permission"
            action = { dismiss() }
        }

        dialog.show(requireFragmentManager())
    }

    @OnAllowed(REQUEST_READ_CALENDAR)
    fun onReadCalendarAllowed() {
        calendarPermissionHint.setOnAllowed()
    }

    @OnDenied(REQUEST_READ_CALENDAR)
    fun onReadCalendarDenied() {
        calendarPermissionHint.setOnDenied()
    }

    @OnNeverAskAgain(REQUEST_READ_CALENDAR)
    fun onReadCalendarNeverAskAgain() {
        calendarPermissionHint.setOnNeverAskAgain()
    }
}
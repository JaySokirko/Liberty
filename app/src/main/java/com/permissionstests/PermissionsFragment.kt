package com.permissionstests

import android.Manifest.permission.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.permissionstests.Constants.REQUEST_CAMERA_PERMISSION
import com.permissionstests.Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION
import com.sokyrko.liberty.Liberty
import com.sokyrko.liberty.Permission

class PermissionsFragment : Fragment() {

    private lateinit var cameraPermissionBtn: Button
    private lateinit var cameraPermissionHintTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_permissions, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraPermissionBtn = view.findViewById(R.id.permission_button)
        cameraPermissionHintTextView = view.findViewById(R.id.permisson_hint_text_view)

        onCameraPermissionBtnClick()
    }

    private fun onCameraPermissionBtnClick() {
        cameraPermissionBtn.setOnClickListener {

            val cameraPermission: Permission =
                Liberty.permission(CAMERA, REQUEST_CAMERA_PERMISSION)

            val storagePermission: Permission =
                Liberty.permission(READ_EXTERNAL_STORAGE, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION)

            Liberty.requestPermissions(cameraPermission, storagePermission)
        }
    }
}
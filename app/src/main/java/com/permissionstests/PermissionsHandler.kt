package com.permissionstests

import android.util.Log
import com.permissionstests.Constants.REQUEST_CAMERA_PERMISSION
import com.permissionstests.Constants.REQUEST_READ_EXTERNAL_STORAGE_PERMISSION
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain

class PermissionsHandler {

    private val TAG = "TAG"

    @OnAllowed(REQUEST_CAMERA_PERMISSION)
    private fun onCameraPermissionAllowed() {
        Log.d(TAG, "onCameraPermissionAllowed: ")
    }

    @OnDenied(REQUEST_CAMERA_PERMISSION)
    private fun onCameraPermissionDenied() {
        Log.d(TAG, "onCameraPermissionDenied: ")
    }

    @OnNeverAskAgain(REQUEST_CAMERA_PERMISSION)
    private fun onCameraPermissionNeverAskAgain() {
        Log.d(TAG, "don't ask again: ")
    }

    @OnAllowed(REQUEST_READ_EXTERNAL_STORAGE_PERMISSION)
    private fun onStoragePermissionAllowed() {
        Log.d(TAG, "onStoragePermissionAllowed: ")
    }

    @OnDenied(REQUEST_READ_EXTERNAL_STORAGE_PERMISSION)
    private fun onStoragePermissionDenied() {
        Log.d(TAG, "onStoragePermissionDenied: ")
    }

    @OnNeverAskAgain(REQUEST_READ_EXTERNAL_STORAGE_PERMISSION)
    private fun onStoragePermissionNeverAskAgain() {
        Log.d(TAG, "onStoragePermissionNeverAskAgain: ")
    }
}
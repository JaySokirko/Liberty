package com.jay.test

import androidx.lifecycle.MutableLiveData
import com.sokyrko.liberty.Liberty
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain

class PermissionsResultReceiver {

    val readContactsRequestResult: MutableLiveData<Liberty.RequestResult> = MutableLiveData()
    val readStorageRequestResult: MutableLiveData<Liberty.RequestResult> = MutableLiveData()

    @OnAllowed(REQUEST_READ_CONTACTS)
    fun onContactsRequestAllowed() {
        readContactsRequestResult.postValue(Liberty.RequestResult.ALLOWED)
    }

    @OnDenied(REQUEST_READ_CONTACTS)
    fun onContactsRequestDenied() {
        readContactsRequestResult.postValue(Liberty.RequestResult.DENIED)
    }

    @OnNeverAskAgain(REQUEST_READ_CONTACTS)
    fun onContactsRequestNeverAskAgain() {
        readContactsRequestResult.postValue(Liberty.RequestResult.NEVER_ASK_AGAIN)
    }

    @OnAllowed(REQUEST_READ_STORAGE)
    fun onReadStorageAllowed() {
        readStorageRequestResult.postValue(Liberty.RequestResult.ALLOWED)
    }

    @OnDenied(REQUEST_READ_STORAGE)
    fun onReadStorageDenied() {
        readStorageRequestResult.postValue(Liberty.RequestResult.DENIED)
    }

    @OnNeverAskAgain(REQUEST_READ_STORAGE)
    fun onReadStorageNeverAskAgain() {
        readStorageRequestResult.postValue(Liberty.RequestResult.NEVER_ASK_AGAIN)
    }
}
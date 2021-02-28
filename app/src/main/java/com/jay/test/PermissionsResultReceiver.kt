package com.jay.test

import androidx.lifecycle.MutableLiveData
import com.sokyrko.liberty.Liberty
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain

class PermissionsResultReceiver {

    val readContactsRequestResult: MutableLiveData<Liberty.RequestResult> = MutableLiveData()

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
}
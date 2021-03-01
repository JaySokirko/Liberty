# Liberty
 
  # Basic 2 #
 
This library helps to manage runtime permissions handling. 
  
![](record_1.gif)

Firstly, initialize the library. You can do this in your activity or fragment:
```kotlin
Liberty.init(activity = this)
Liberty.init(fragment = this)
 ```
<br/>

Call this code to request permission, for instance, android.Manifest.permission.READ_CONTACTS
```kotlin
Liberty.requestPermission(permission = android.Manifest.permission.READ_CONTACTS, requestCode = REQUEST_READ_CONTACTS)
```

<br/>

In the next step, you need to override onRequestPermissionsResult:
```kotlin
   override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        Liberty.onRequestPermissionsResult(receiver = this, requestCode, permissions, grantResults)
    }
```
Where the receiver is the object which will receive the result of the permission request. 
In the current case, it is an instance of your activity or fragment.

<br/>

And in the final step, you should define functions that will handle results:
```kotlin
    @OnAllowed(REQUEST_READ_CONTACTS)
    fun onContactsAllowed() {
        //Permission allowed
    }
    
    @OnDenied(REQUEST_READ_CONTACTS)
    fun onContactsDenied() {
        //Permission denied
    }
    
    @OnNeverAskAgain(REQUEST_READ_CONTACTS)
    fun onContactsNeverAskAgain() {
        //The user has clicked "don't ask again"
    }
```
Functions names do not matter, but they should be without any arguments.
After permission request, the library will trigger a certain method, depends on the result.
For instance, if the user has allowed the requested permission, function annotated @OnAllowed will be called.

<br/>




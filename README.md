# Liberty
This library helps to manage runtime permissions handling. 
<br/>
![](record_1.gif)
<br/>

## Basic concepts ##
   
Firstly, initialize the library. You can do this in your activity or fragment:
```kotlin
Liberty.init(activity = this)
Liberty.init(fragment = this)
 ```
<br/>

Call this code to request permission, for instance, android.Manifest.permission.READ_CONTACTS
```kotlin
Liberty.requestPermission(permission = android.Manifest.permission.READ_CONTACTS, requestCode = YOUR_REQUEST_CODE)
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
    @OnAllowed(YOUR_REQUEST_CODE)
    fun onContactsAllowed() {
        //Permission allowed
    }
    
    @OnDenied(YOUR_REQUEST_CODE)
    fun onContactsDenied() {
        //Permission denied
    }
    
    @OnNeverAskAgain(YOUR_REQUEST_CODE)
    fun onContactsNeverAskAgain() {
        //Clicked "don't ask again"
    }
```
Functions names do not matter, but they should be without any arguments.
After permission request, the library will trigger a certain method, depends on the result.
For instance, if the user has allowed the requested permission, function annotated @OnAllowed will be called.

<br/>

## Warning ##
If you've initialized the library in an activity that extends from Activity(), 
you should manually call Liberty.clear() in the onDestroy(): 
```kotlin
class MainActivity : Activity() {

    override fun onDestroy() {
        super.onDestroy()
        Liberty.clear()
    }
}
```
In case, if your activity extends AppCompatActivity() or you've initialized 
Liberty in your fragment, you can don't care about clearing resources, cuz the library handles this automatically.

<br/>

## Scoped permissions request ##
It often needs to request several permissions in a time.
<br/>
![](record_2.gif)
<br/>
To do this just call:
```kotlin
Liberty.requestPermissions(
                CAMERA,
                READ_EXTERNAL_STORAGE,
                READ_SMS,
                requestCode = YOUR_REQUEST_CODE)
```

And you need to define a function which will receive a result:
```kotlin
@OnPermissionsRequestResult(YOUR_REQUEST_CODE)
fun onContactsAndCamera(result: MutableList<Permission>) {
  //Handle permissions request result
}
```

The function name doesn't matter but should receives
only one argument with type ```MutableList<Permission>```

<br/>

## Custom receivers ## 
Let's assume you want to receive permissions request results in your ViewModel class.
To do this you just need to pass your ViewModel class instance as the receiver in onRequestPermissionsResult():
```kotlin
Liberty.onRequestPermissionsResult(receiver = yourViewModel, requestCode, permissions, grantResults)
```
And, accordingly, in the ViewModel class define functions which receive results:
```kotlin
    @OnAllowed(YOUR_REQUEST_CODE)
    fun onPermissionAllowed() {
        //Permission allowed
    }
```

<br/>

## Additionally ##

```kotlin
Permission 
```
class contains two fields:







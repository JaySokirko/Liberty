# Liberty
 
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
Liberty.requestPermission(permission = READ_CONTACTS, requestCode = 100)
```

<br/>

In the next step, you need to override onRequestPermissionsResult:
```kotlin
   override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        Liberty.onRequestPermissionsResult(receiver = permissionResultReceiver, requestCode, permissions, grantResults)
    }
```

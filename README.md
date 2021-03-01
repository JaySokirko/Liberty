# Liberty
 
This library helps to manage runtime permissions handling. 
  
![](record_1.gif)

Firstly, initialize the library. You can do this in your activity or fragment:
```kotlin
Liberty.init(activity = this)
Liberty.init(fragment = this)
 ```
<br/>

Call this code to request some permission:
```kotlin
Liberty.requestPermission(permission = yourPermissionName, requestCode = 100)
```


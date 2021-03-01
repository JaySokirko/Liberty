# Liberty
 
This library helps to manage runtime permissions handling. 
  
![](record_1.gif)

Firstly, initialize the library. You can do this in your activity or fragment:
```kotlin
Liberty.init(activity = this)
Liberty.init(fragment = this)
 ```



To request permission just call this code:
```kotlin
Liberty.requestPermission(permission = READ_CONTACTS, requestCode = REQUEST_READ_CONTACTS)
```


//[ipfsd](../../index.md)/[danbroid.ipfsd.service](../index.md)/[NotificationManager](index.md)



# NotificationManager  
 [androidJvm] class [NotificationManager](index.md)(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), **channelID**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **channelName**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **channelDescription**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **importance**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **listener**: [NotificationManager.NotificationListener](-notification-listener/index.md)?)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [Companion](-companion/index.md)| [androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>
| [NotificationListener](-notification-listener/index.md)| [androidJvm]  <br>Content  <br>interface [NotificationListener](-notification-listener/index.md)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [cancelNotification](cancel-notification.md)| [androidJvm]  <br>Content  <br>@[MainThread](https://developer.android.com/reference/kotlin/androidx/annotation/MainThread.html)()  <br>  <br>fun [cancelNotification](cancel-notification.md)()  <br><br><br>
| [equals](../-i-p-f-s-message/-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open operator override fun [equals](../-i-p-f-s-message/-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../-i-p-f-s-message/-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [hashCode](../-i-p-f-s-message/-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [showNotification](show-notification.md)| [androidJvm]  <br>Content  <br>@[MainThread](https://developer.android.com/reference/kotlin/androidx/annotation/MainThread.html)()  <br>  <br>fun [showNotification](show-notification.md)(contentText: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, title: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, builderConfig: [NotificationCompat.Builder](https://developer.android.com/reference/kotlin/androidx/core/app/NotificationCompat.Builder.html).() -> [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?)  <br><br><br>
| [toString](../-settings-activity/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [toString](../-settings-activity/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [channelID](index.md#danbroid.ipfsd.service/NotificationManager/channelID/#/PointingToDeclaration/)|  [androidJvm] val [channelID](index.md#danbroid.ipfsd.service/NotificationManager/channelID/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [context](index.md#danbroid.ipfsd.service/NotificationManager/context/#/PointingToDeclaration/)|  [androidJvm] val [context](index.md#danbroid.ipfsd.service/NotificationManager/context/#/PointingToDeclaration/): [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)   <br>
| [listener](index.md#danbroid.ipfsd.service/NotificationManager/listener/#/PointingToDeclaration/)|  [androidJvm] var [listener](index.md#danbroid.ipfsd.service/NotificationManager/listener/#/PointingToDeclaration/): [NotificationManager.NotificationListener](-notification-listener/index.md)?   <br>


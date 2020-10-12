//[ipfsd](../index.md)/[danbroid.ipfsd.service](index.md)



# Package danbroid.ipfsd.service  


## Types  
  
|  Name|  Summary| 
|---|---|
| [ApiClient](-api-client/index.md)| [androidJvm]  <br>Content  <br>class [ApiClient](-api-client/index.md)(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), **port**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **urlBase**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : OkHttpCallExecutor  <br><br><br>
| [IPFS](-i-p-f-s/index.md)| [androidJvm]  <br>Brief description  <br><br><br>IPFS is a class that wraps a go-ipfs node and its shell over UDS.<br><br>  <br>Content  <br>open class [IPFS](-i-p-f-s/index.md)  <br><br><br>
| [IPFSClient](-i-p-f-s-client/index.md)| [androidJvm]  <br>Content  <br>open class [IPFSClient](-i-p-f-s-client/index.md)(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))  <br><br><br>
| [IPFSMessage](-i-p-f-s-message/index.md)| [androidJvm]  <br>Content  <br>sealed class [IPFSMessage](-i-p-f-s-message/index.md) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)  <br><br><br>
| [IPFSService](-i-p-f-s-service/index.md)| [androidJvm]  <br>Content  <br>class [IPFSService](-i-p-f-s-service/index.md) : [Service](https://developer.android.com/reference/kotlin/android/app/Service.html)  <br><br><br>
| [NotificationManager](-notification-manager/index.md)| [androidJvm]  <br>Content  <br>class [NotificationManager](-notification-manager/index.md)(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), **channelID**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **channelName**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **channelDescription**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **importance**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **listener**: [NotificationManager.NotificationListener](-notification-manager/-notification-listener/index.md)?)  <br><br><br>
| [RequestBuilder](-request-builder/index.md)| [androidJvm]  <br>Brief description  <br><br><br>RequestBuilder is an IPFS command request builder.<br><br>  <br>Content  <br>open class [RequestBuilder](-request-builder/index.md)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [toIPFSMessage](to-i-p-f-s-message.md)| [androidJvm]  <br>Content  <br>fun [Message](https://developer.android.com/reference/kotlin/android/os/Message.html).[toIPFSMessage](to-i-p-f-s-message.md)(): [IPFSMessage](-i-p-f-s-message/index.md)  <br><br><br>


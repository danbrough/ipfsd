//[ipfsd](../../index.md)/[danbroid.ipfsd.service](../index.md)/[IPFSClient](index.md)



# IPFSClient  
 [androidJvm] open class [IPFSClient](index.md)(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))   


## Constructors  
  
|  Name|  Summary| 
|---|---|
| [IPFSClient](-i-p-f-s-client.md)|  [androidJvm] fun [IPFSClient](-i-p-f-s-client.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))   <br>


## Types  
  
|  Name|  Summary| 
|---|---|
| [Companion](-companion/index.md)| [androidJvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>
| [ConnectionState](-connection-state/index.md)| [androidJvm]  <br>Content  <br>enum [ConnectionState](-connection-state/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)<[IPFSClient.ConnectionState](-connection-state/index.md)>   <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [connect](connect.md)| [androidJvm]  <br>Content  <br>@[MainThread](https://developer.android.com/reference/kotlin/androidx/annotation/MainThread.html)()  <br>  <br>fun [connect](connect.md)()  <br><br><br>
| [decrementCallCount](decrement-call-count.md)| [androidJvm]  <br>Content  <br>@[MainThread](https://developer.android.com/reference/kotlin/androidx/annotation/MainThread.html)()  <br>  <br>fun [decrementCallCount](decrement-call-count.md)()  <br><br><br>
| [disconnect](disconnect.md)| [androidJvm]  <br>Content  <br>fun [disconnect](disconnect.md)()  <br><br><br>
| [equals](../-i-p-f-s-message/-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open operator override fun [equals](../-i-p-f-s-message/-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../-i-p-f-s-message/-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [hashCode](../-i-p-f-s-message/-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [incrementCallCount](increment-call-count.md)| [androidJvm]  <br>Content  <br>@[MainThread](https://developer.android.com/reference/kotlin/androidx/annotation/MainThread.html)()  <br>  <br>fun [incrementCallCount](increment-call-count.md)()  <br><br><br>
| [runWhenConnected](run-when-connected.md)| [androidJvm]  <br>Content  <br>suspend fun [runWhenConnected](run-when-connected.md)(job: [SuspendFunction0](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-suspend-function0/index.html)<[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)>)  <br><br><br>
| [sendMessage](send-message.md)| [androidJvm]  <br>Content  <br>suspend fun [sendMessage](send-message.md)(msg: [IPFSMessage](../-i-p-f-s-message/index.md))  <br><br><br>
| [toString](../-settings-activity/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [toString](../-settings-activity/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [connectionState](index.md#danbroid.ipfsd.service/IPFSClient/connectionState/#/PointingToDeclaration/)|  [androidJvm] val [connectionState](index.md#danbroid.ipfsd.service/IPFSClient/connectionState/#/PointingToDeclaration/): [LiveData](https://developer.android.com/reference/kotlin/androidx/lifecycle/LiveData.html)<[IPFSClient.ConnectionState](-connection-state/index.md)>   <br>
| [context](index.md#danbroid.ipfsd.service/IPFSClient/context/#/PointingToDeclaration/)|  [androidJvm] val [context](index.md#danbroid.ipfsd.service/IPFSClient/context/#/PointingToDeclaration/): [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)   <br>


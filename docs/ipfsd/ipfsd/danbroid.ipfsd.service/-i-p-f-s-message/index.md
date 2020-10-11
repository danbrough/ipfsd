//[ipfsd](../../index.md)/[danbroid.ipfsd.service](../index.md)/[IPFSMessage](index.md)



# IPFSMessage  
 [androidJvm] sealed class [IPFSMessage](index.md) : [Parcelable](https://developer.android.com/reference/kotlin/android/os/Parcelable.html)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [BANDWIDTH](-b-a-n-d-w-i-d-t-h/index.md)| [androidJvm]  <br>Content  <br>data class [BANDWIDTH](-b-a-n-d-w-i-d-t-h/index.md)(**totalIn**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **totalOut**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **rateIn**: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html), **rateOut**: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) : [IPFSMessage](index.md)  <br><br><br>
| [CLIENT_CONNECT](-c-l-i-e-n-t_-c-o-n-n-e-c-t/index.md)| [androidJvm]  <br>Content  <br>object [CLIENT_CONNECT](-c-l-i-e-n-t_-c-o-n-n-e-c-t/index.md) : [IPFSMessage](index.md)  <br><br><br>
| [CLIENT_DISCONNECT](-c-l-i-e-n-t_-d-i-s-c-o-n-n-e-c-t/index.md)| [androidJvm]  <br>Content  <br>object [CLIENT_DISCONNECT](-c-l-i-e-n-t_-d-i-s-c-o-n-n-e-c-t/index.md) : [IPFSMessage](index.md)  <br><br><br>
| [SERVICE_STARTED](-s-e-r-v-i-c-e_-s-t-a-r-t-e-d/index.md)| [androidJvm]  <br>Content  <br>object [SERVICE_STARTED](-s-e-r-v-i-c-e_-s-t-a-r-t-e-d/index.md) : [IPFSMessage](index.md)  <br><br><br>
| [SERVICE_STOPPING](-s-e-r-v-i-c-e_-s-t-o-p-p-i-n-g/index.md)| [androidJvm]  <br>Content  <br>object [SERVICE_STOPPING](-s-e-r-v-i-c-e_-s-t-o-p-p-i-n-g/index.md) : [IPFSMessage](index.md)  <br><br><br>
| [SET_CONFIGURATION](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md)| [androidJvm]  <br>Content  <br>data class [SET_CONFIGURATION](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md)(**inactivityTimeout**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **resetStats**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [IPFSMessage](index.md)  <br><br><br>
| [STATS_RESET](-s-t-a-t-s_-r-e-s-e-t/index.md)| [androidJvm]  <br>Content  <br>object [STATS_RESET](-s-t-a-t-s_-r-e-s-e-t/index.md) : [IPFSMessage](index.md)  <br><br><br>
| [TIMEOUT_DECREMENT_LOCK](-t-i-m-e-o-u-t_-d-e-c-r-e-m-e-n-t_-l-o-c-k/index.md)| [androidJvm]  <br>Content  <br>object [TIMEOUT_DECREMENT_LOCK](-t-i-m-e-o-u-t_-d-e-c-r-e-m-e-n-t_-l-o-c-k/index.md) : [IPFSMessage](index.md)  <br><br><br>
| [TIMEOUT_INCREMENT_LOCK](-t-i-m-e-o-u-t_-i-n-c-r-e-m-e-n-t_-l-o-c-k/index.md)| [androidJvm]  <br>Content  <br>object [TIMEOUT_INCREMENT_LOCK](-t-i-m-e-o-u-t_-i-n-c-r-e-m-e-n-t_-l-o-c-k/index.md) : [IPFSMessage](index.md)  <br><br><br>
| [TIMEOUT_RESET](-t-i-m-e-o-u-t_-r-e-s-e-t/index.md)| [androidJvm]  <br>Content  <br>object [TIMEOUT_RESET](-t-i-m-e-o-u-t_-r-e-s-e-t/index.md) : [IPFSMessage](index.md)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [describeContents](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#android.os/Parcelable/describeContents/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>abstract override fun [describeContents](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#android.os/Parcelable/describeContents/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [equals](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open operator override fun [equals](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [hashCode](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toMessage](to-message.md)| [androidJvm]  <br>Content  <br>fun [toMessage](to-message.md)(replyTo: [Messenger](https://developer.android.com/reference/kotlin/android/os/Messenger.html)?): [Message](https://developer.android.com/reference/kotlin/android/os/Message.html)  <br><br><br>
| [toString](to-string.md)| [androidJvm]  <br>Content  <br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [writeToParcel](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#android.os/Parcelable/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>abstract override fun [writeToParcel](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md#android.os/Parcelable/writeToParcel/#android.os.Parcel#kotlin.Int/PointingToDeclaration/)(p0: [Parcel](https://developer.android.com/reference/kotlin/android/os/Parcel.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>


## Inheritors  
  
|  Name| 
|---|
| [IPFSMessage](-c-l-i-e-n-t_-c-o-n-n-e-c-t/index.md)
| [IPFSMessage](-c-l-i-e-n-t_-d-i-s-c-o-n-n-e-c-t/index.md)
| [IPFSMessage](-s-e-r-v-i-c-e_-s-t-a-r-t-e-d/index.md)
| [IPFSMessage](-s-e-r-v-i-c-e_-s-t-o-p-p-i-n-g/index.md)
| [IPFSMessage](-t-i-m-e-o-u-t_-r-e-s-e-t/index.md)
| [IPFSMessage](-s-t-a-t-s_-r-e-s-e-t/index.md)
| [IPFSMessage](-t-i-m-e-o-u-t_-i-n-c-r-e-m-e-n-t_-l-o-c-k/index.md)
| [IPFSMessage](-t-i-m-e-o-u-t_-d-e-c-r-e-m-e-n-t_-l-o-c-k/index.md)
| [IPFSMessage](-b-a-n-d-w-i-d-t-h/index.md)
| [IPFSMessage](-s-e-t_-c-o-n-f-i-g-u-r-a-t-i-o-n/index.md)


//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Name](index.md)/[publish](publish.md)



# publish  
[jvm]  
Brief description  


Publish IPNS names



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| key| <br><br>Name of the key to be used or a valid PeerID, as listed by 'ipfs key list -l'. Default: self.<br><br>
| lifetime| <br><br>Time duration that the record will be valid for. This accepts durations such as "300s", "1.5h" or "2h45m". Valid time units are "ns", "us" (or "µs"), "ms", "s", "m", "h". Default: 24h. Required: no.<br><br>
| path| <br><br>ipfs path of the object to be published.<br><br>
  
  
Content  
@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  
  
fun [publish](publish.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), resolve: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), lifetime: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [ApiCall](../../-api-call/index.md)<[Types.NameValue](../../-types/-name-value/index.md)>  




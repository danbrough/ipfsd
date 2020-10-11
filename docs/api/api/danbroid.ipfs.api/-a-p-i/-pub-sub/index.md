//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[PubSub](index.md)



# PubSub  
 [jvm] object [PubSub](index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [Message](-message/index.md)| [jvm]  <br>Content  <br>data class [Message](-message/index.md)(**from**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **data**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **seqno**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **topicIDs**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [publish](publish.md)| [jvm]  <br>Content  <br>fun [publish](publish.md)(topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ApiCall](../../-api-call/index.md)<[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)>  <br><br><br>
| [subscribe](subscribe.md)| [jvm]  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [subscribe](subscribe.md)(topic: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), discover: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.PubSub.Message](-message/index.md)>  <br><br><br>
| [toString](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


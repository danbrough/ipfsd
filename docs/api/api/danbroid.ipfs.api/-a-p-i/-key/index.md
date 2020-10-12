//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Key](index.md)



# Key  
 [jvm] object [Key](index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [Key](-key/index.md)| [jvm]  <br>Content  <br>data class [Key](-key/index.md)(**name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **id**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br><br><br>
| [LsResponse](-ls-response/index.md)| [jvm]  <br>Content  <br>data class [LsResponse](-ls-response/index.md)(**keys**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[API.Key.Key](-key/index.md)>)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [ls](ls.md)| [jvm]  <br>Brief description  <br><br><br>List all local keypairs<br><br>  <br>Content  <br>@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)()  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [ls](ls.md)(ipnsBase: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Key.LsResponse](-ls-response/index.md)>  <br><br><br>
| [toString](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


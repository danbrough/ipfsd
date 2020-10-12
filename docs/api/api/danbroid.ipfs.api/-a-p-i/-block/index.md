//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Block](index.md)



# Block  
 [jvm] object [Block](index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [PutResponse](-put-response/index.md)| [jvm]  <br>Content  <br>data class [PutResponse](-put-response/index.md)(**key**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **size**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [get](get.md)| [jvm]  <br>Brief description  <br><br><br>/api/v0/block/get Get a raw IPFS block.<br><br>  <br>Content  <br>fun [get](get.md)(cid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), responseProcessor: [ResponseProcessor](../../index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)<[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)>): [ApiCall](../../-api-call/index.md)<[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)>  <br><br><br>
| [hashCode](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [put](put.md)| [jvm]  <br>Brief description  <br><br><br>/api/v0/block/put Store input as an IPFS block.<br><br>  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [put](put.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, mhType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, mhLen: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, pin: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Block.PutResponse](-put-response/index.md)>  <br><br><br>
| [toString](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


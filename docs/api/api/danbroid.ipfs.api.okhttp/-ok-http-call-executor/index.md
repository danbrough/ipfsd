//[api](../../index.md)/[danbroid.ipfs.api.okhttp](../index.md)/[OkHttpCallExecutor](index.md)



# OkHttpCallExecutor  
 [jvm] open class [OkHttpCallExecutor](index.md)@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()constructor(**port**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **urlBase**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [CallExecutor](../../danbroid.ipfs.api/-call-executor/index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [Companion](-companion/index.md)| [jvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [exec](exec.md)| [jvm]  <br>Content  <br>open suspend override fun <[T](exec.md)> [exec](exec.md)(call: [ApiCall](../../danbroid.ipfs.api/-api-call/index.md)<[T](exec.md)>, handler: [ResultHandler](../../danbroid.ipfs.api/index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)<[T](exec.md)>)  <br>open override fun <T> [exec](../../danbroid.ipfs.api/-call-executor/exec.md)(call: [ApiCall](../../danbroid.ipfs.api/-api-call/index.md)<T>, handler: [CallExecutor.JavaResultHandler](../../danbroid.ipfs.api/-call-executor/-java-result-handler/index.md)<T>)  <br><br><br>
| [hashCode](-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toString](-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [urlBase](index.md#danbroid.ipfs.api.okhttp/OkHttpCallExecutor/urlBase/#/PointingToDeclaration/)|  [jvm] val [urlBase](index.md#danbroid.ipfs.api.okhttp/OkHttpCallExecutor/urlBase/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>


//[api](../../index.md)/[danbroid.ipfs.api](../index.md)/[CallExecutor](index.md)



# CallExecutor  
 [jvm] interface [CallExecutor](index.md)   


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](../-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [exec](exec.md)| [jvm]  <br>Content  <br>abstract suspend fun <[T](exec.md)> [exec](exec.md)(call: [ApiCall](../-api-call/index.md)<[T](exec.md)>, handler: [ResultHandler](../index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)<[T](exec.md)>)  <br><br><br>
| [hashCode](../-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toString](../-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Inheritors  
  
|  Name| 
|---|
| [OkHttpCallExecutor](../-ok-http-call-executor/index.md)


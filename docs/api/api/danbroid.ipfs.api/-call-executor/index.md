//[api](../../index.md)/[danbroid.ipfs.api](../index.md)/[CallExecutor](index.md)



# CallExecutor  
 [jvm] interface [CallExecutor](index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [JavaResultHandler](-java-result-handler/index.md)| [jvm]  <br>Content  <br>interface [JavaResultHandler](-java-result-handler/index.md)<[T](-java-result-handler/index.md)>  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [exec](exec.md)| [jvm]  <br>Content  <br>open fun <[T](exec.md)> [exec](exec.md)(call: [ApiCall](../-api-call/index.md)<[T](exec.md)>, handler: [CallExecutor.JavaResultHandler](-java-result-handler/index.md)<[T](exec.md)>)  <br>abstract suspend fun <[T](exec.md)> [exec](exec.md)(call: [ApiCall](../-api-call/index.md)<[T](exec.md)>, handler: [ResultHandler](../index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)<[T](exec.md)>)  <br><br><br>
| [hashCode](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toString](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Inheritors  
  
|  Name| 
|---|
| [OkHttpCallExecutor](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/index.md)


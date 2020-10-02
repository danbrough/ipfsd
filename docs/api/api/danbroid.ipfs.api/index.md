//[api](../index.md)/[danbroid.ipfs.api](index.md)



# Package danbroid.ipfs.api  


## Types  
  
|  Name|  Summary| 
|---|---|
| [API](-a-p-i/index.md)| [jvm]  <br>Content  <br>class [API](-a-p-i/index.md)(**executor**: [API.CallExecutor](-a-p-i/-call-executor/index.md))  <br><br><br>
| [ApiCall](-api-call/index.md)| [jvm]  <br>Content  <br>open class [ApiCall](-api-call/index.md)<[T](-api-call/index.md)>(**executor**: [API.CallExecutor](-a-p-i/-call-executor/index.md), **path**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **responseProcessor**: [ResponseProcessor](index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)<[T](-api-call/index.md)>)  <br><br><br>
| [OkHttpCallExecutor](-ok-http-call-executor/index.md)| [jvm]  <br>Content  <br>open class [OkHttpCallExecutor](-ok-http-call-executor/index.md)(**port**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **urlBase**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [API.CallExecutor](-a-p-i/-call-executor/index.md)  <br><br><br>
| [ResponseProcessor](index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)| [jvm]  <br>Content  <br>typealias [ResponseProcessor](index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)<[T](index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)> = [SuspendFunction2](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-suspend-function2/index.html)<[Reader](https://docs.oracle.com/javase/8/docs/api/java/io/Reader.html), [ResultHandler](index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)<[T](index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)>, [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)>  <br><br><br>
| [ResultHandler](index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)| [jvm]  <br>Brief description  <br><br><br>Receives the result of an API call<br><br>  <br>Content  <br>typealias [ResultHandler](index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)<[T](index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)> = [SuspendFunction1](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-suspend-function1/index.html)<[T](index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)?, [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)>  <br><br><br>
| [Types](-types/index.md)| [jvm]  <br>Content  <br>object [Types](-types/index.md)  <br><br><br>


//[api](../../index.md)/[danbroid.ipfs.api](../index.md)/[ApiCall](index.md)



# ApiCall  
 [jvm] open class [ApiCall](index.md)<[T](index.md)>(**executor**: [API.CallExecutor](../-a-p-i/-call-executor/index.md), **path**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **responseProcessor**: [ResponseProcessor](../index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)<[T](index.md)>)   


## Constructors  
  
|  Name|  Summary| 
|---|---|
| [ApiCall](-api-call.md)|  [jvm] fun <[T](index.md)> [ApiCall](-api-call.md)(executor: [API.CallExecutor](../-a-p-i/-call-executor/index.md), path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [Class](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)<[T](index.md)>)   <br>


## Types  
  
|  Name|  Summary| 
|---|---|
| [Companion](-companion/index.md)| [jvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>
| [FilePart](-file-part/index.md)| [jvm]  <br>Content  <br>class [FilePart](-file-part/index.md)(**file**: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)) : [ApiCall.Part](-part/index.md)  <br><br><br>
| [Part](-part/index.md)| [jvm]  <br>Content  <br>interface [Part](-part/index.md)  <br><br><br>
| [StringPart](-string-part/index.md)| [jvm]  <br>Content  <br>class [StringPart](-string-part/index.md)(**data**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, **absPath**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [ApiCall.Part](-part/index.md)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [addData](add-data.md)| [jvm]  <br>Content  <br>fun [addData](add-data.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, absPath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [ApiCall](index.md)<[T](index.md)>  <br><br><br>
| [addFile](add-file.md)| [jvm]  <br>Content  <br>fun [addFile](add-file.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)): [ApiCall](index.md)<[T](index.md)>  <br><br><br>
| [asFlow](as-flow.md)| [jvm]  <br>Content  <br>fun [asFlow](as-flow.md)(): Flow<[T](index.md)?>  <br><br><br>
| [equals](../-types/-name-value/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../-types/-name-value/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [exec](exec.md)| [jvm]  <br>Content  <br>suspend fun [exec](exec.md)(handler: [ResultHandler](../index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)<[T](index.md)>?)  <br><br><br>
| [hashCode](../-types/-name-value/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../-types/-name-value/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [onError](on-error.md)| [jvm]  <br>Content  <br>fun [onError](on-error.md)(handler: ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -> [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [ApiCall](index.md)<[T](index.md)>  <br><br><br>
| [onResult](on-result.md)| [jvm]  <br>Content  <br>fun [onResult](on-result.md)(handler: [ResultHandler](../index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)<[T](index.md)>): [ApiCall](index.md)<[T](index.md)>  <br><br><br>
| [toString](to-string.md)| [jvm]  <br>Content  <br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [errorHandler](index.md#danbroid.ipfs.api/ApiCall/errorHandler/#/PointingToDeclaration/)|  [jvm] var [errorHandler](index.md#danbroid.ipfs.api/ApiCall/errorHandler/#/PointingToDeclaration/): ([Throwable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)) -> [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)   <br>
| [executor](index.md#danbroid.ipfs.api/ApiCall/executor/#/PointingToDeclaration/)|  [jvm] val [executor](index.md#danbroid.ipfs.api/ApiCall/executor/#/PointingToDeclaration/): [API.CallExecutor](../-a-p-i/-call-executor/index.md)   <br>
| [path](index.md#danbroid.ipfs.api/ApiCall/path/#/PointingToDeclaration/)|  [jvm] val [path](index.md#danbroid.ipfs.api/ApiCall/path/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [responseProcessor](index.md#danbroid.ipfs.api/ApiCall/responseProcessor/#/PointingToDeclaration/)|  [jvm] val [responseProcessor](index.md#danbroid.ipfs.api/ApiCall/responseProcessor/#/PointingToDeclaration/): [ResponseProcessor](../index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)<[T](index.md)>   <br>
| [resultHandler](index.md#danbroid.ipfs.api/ApiCall/resultHandler/#/PointingToDeclaration/)|  [jvm] var [resultHandler](index.md#danbroid.ipfs.api/ApiCall/resultHandler/#/PointingToDeclaration/): [ResultHandler](../index.md#danbroid.ipfs.api/ResultHandler///PointingToDeclaration/)<[T](index.md)>   <br>


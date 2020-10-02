//[api](../../index.md)/[danbroid.ipfs.api](../index.md)/[API](index.md)



# API  
 [jvm] class [API](index.md)(**executor**: [API.CallExecutor](-call-executor/index.md))   


## Types  
  
|  Name|  Summary| 
|---|---|
| [CallExecutor](-call-executor/index.md)| [jvm]  <br>Content  <br>interface [CallExecutor](-call-executor/index.md)  <br><br><br>
| [Companion](-companion/index.md)| [jvm]  <br>Content  <br>object [Companion](-companion/index.md)  <br><br><br>
| [Key](-key/index.md)| [jvm]  <br>Content  <br>class [Key](-key/index.md)(**api**: [API](index.md))  <br><br><br>
| [Name](-name/index.md)| [jvm]  <br>Content  <br>class [Name](-name/index.md)(**api**: [API](index.md))  <br><br><br>
| [PubSub](-pub-sub/index.md)| [jvm]  <br>Content  <br>class [PubSub](-pub-sub/index.md)(**api**: [API](index.md))  <br><br><br>
| [Stats](-stats/index.md)| [jvm]  <br>Content  <br>class [Stats](-stats/index.md)(**api**: [API](index.md))  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [add](add.md)| [jvm]  <br>Brief description  <br><br><br>Add data to ipfs.<br><br>  <br>Content  <br>fun [add](add.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)?, recurseDirectory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, wrapWithDirectory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, chunker: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, pin: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), onlyHash: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../-api-call/index.md)<[Types.File](../-types/-file/index.md)>  <br><br><br>
| [equals](../-types/-name-value/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../-types/-name-value/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../-types/-name-value/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../-types/-name-value/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [id](id.md)| [jvm]  <br>Content  <br>fun [id](id.md)(): [ApiCall](../-api-call/index.md)<[Types.ID](../-types/-i-d/index.md)>  <br><br><br>
| [ls](ls.md)| [jvm]  <br>Content  <br>fun [ls](ls.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), stream: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ApiCall](../-api-call/index.md)<[Types.Objects](../-types/-objects/index.md)>  <br><br><br>
| [toString](../-types/-name-value/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../-types/-name-value/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [version](version.md)| [jvm]  <br>Content  <br>fun [version](version.md)(): [ApiCall](../-api-call/index.md)<[Types.Version](../-types/-version/index.md)>  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [executor](index.md#danbroid.ipfs.api/API/executor/#/PointingToDeclaration/)|  [jvm] val [executor](index.md#danbroid.ipfs.api/API/executor/#/PointingToDeclaration/): [API.CallExecutor](-call-executor/index.md)   <br>
| [key](index.md#danbroid.ipfs.api/API/key/#/PointingToDeclaration/)|  [jvm] val [key](index.md#danbroid.ipfs.api/API/key/#/PointingToDeclaration/): [API.Key](-key/index.md)   <br>
| [name](index.md#danbroid.ipfs.api/API/name/#/PointingToDeclaration/)|  [jvm] val [name](index.md#danbroid.ipfs.api/API/name/#/PointingToDeclaration/): [API.Name](-name/index.md)   <br>
| [pubSub](index.md#danbroid.ipfs.api/API/pubSub/#/PointingToDeclaration/)|  [jvm] val [pubSub](index.md#danbroid.ipfs.api/API/pubSub/#/PointingToDeclaration/): [API.PubSub](-pub-sub/index.md)   <br>
| [stats](index.md#danbroid.ipfs.api/API/stats/#/PointingToDeclaration/)|  [jvm] val [stats](index.md#danbroid.ipfs.api/API/stats/#/PointingToDeclaration/): [API.Stats](-stats/index.md)   <br>


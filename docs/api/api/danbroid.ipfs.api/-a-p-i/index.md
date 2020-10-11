//[api](../../index.md)/[danbroid.ipfs.api](../index.md)/[API](index.md)



# API  
 [jvm] 

API for calls to an IPFS node

object [API](index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [Config](-config/index.md)| [jvm]  <br>Content  <br>object [Config](-config/index.md)  <br><br><br>
| [Key](-key/index.md)| [jvm]  <br>Content  <br>object [Key](-key/index.md)  <br><br><br>
| [Name](-name/index.md)| [jvm]  <br>Content  <br>object [Name](-name/index.md)  <br><br><br>
| [PubSub](-pub-sub/index.md)| [jvm]  <br>Content  <br>object [PubSub](-pub-sub/index.md)  <br><br><br>
| [Repo](-repo/index.md)| [jvm]  <br>Content  <br>object [Repo](-repo/index.md)  <br><br><br>
| [Stats](-stats/index.md)| [jvm]  <br>Content  <br>object [Stats](-stats/index.md)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [add](add.md)| [jvm]  <br>Brief description  <br><br><br>Add data to ipfs.<br><br>  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [add](add.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)?, recurseDirectory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, wrapWithDirectory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, chunker: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, pin: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), onlyHash: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../-api-call/index.md)<[Types.File](../-types/-file/index.md)>  <br><br><br>
| [equals](../-types/-config/-config-change/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../-types/-config/-config-change/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../-types/-config/-config-change/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../-types/-config/-config-change/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [id](id.md)| [jvm]  <br>Content  <br>fun [id](id.md)(): [ApiCall](../-api-call/index.md)<[Types.ID](../-types/-i-d/index.md)>  <br><br><br>
| [ls](ls.md)| [jvm]  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [ls](ls.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), stream: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ApiCall](../-api-call/index.md)<[Types.Objects](../-types/-objects/index.md)>  <br><br><br>
| [toString](../-types/-config/-config-change/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../-types/-config/-config-change/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [version](version.md)| [jvm]  <br>Content  <br>fun [version](version.md)(): [ApiCall](../-api-call/index.md)<[Types.Version](../-types/-version/index.md)>  <br><br><br>


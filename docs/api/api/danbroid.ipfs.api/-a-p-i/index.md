//[api](../../index.md)/[danbroid.ipfs.api](../index.md)/[API](index.md)



# API  
 [jvm] 

API for calls to an IPFS node

object [API](index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [Block](-block/index.md)| [jvm]  <br>Content  <br>object [Block](-block/index.md)  <br><br><br>
| [Config](-config/index.md)| [jvm]  <br>Content  <br>object [Config](-config/index.md)  <br><br><br>
| [Dag](-dag/index.md)| [jvm]  <br>Content  <br>object [Dag](-dag/index.md)  <br><br><br>
| [FileResponse](-file-response/index.md)| [jvm]  <br>Content  <br>data class [FileResponse](-file-response/index.md)(**bytes**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **hash**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **size**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))  <br><br><br>
| [Files](-files/index.md)| [jvm]  <br>Content  <br>object [Files](-files/index.md)  <br><br><br>
| [Key](-key/index.md)| [jvm]  <br>Content  <br>object [Key](-key/index.md)  <br><br><br>
| [LsResponse](-ls-response/index.md)| [jvm]  <br>Content  <br>data class [LsResponse](-ls-response/index.md)(**objects**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[API.Object](-object/index.md)>)  <br><br><br>
| [Name](-name/index.md)| [jvm]  <br>Content  <br>object [Name](-name/index.md)  <br><br><br>
| [Network](-network/index.md)| [jvm]  <br>Content  <br>object [Network](-network/index.md)  <br><br><br>
| [Object](-object/index.md)| [jvm]  <br>Content  <br>data class [Object](-object/index.md)(**hash**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **links**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[API.Object.Link](-object/-link/index.md)>)  <br><br><br>
| [PubSub](-pub-sub/index.md)| [jvm]  <br>Content  <br>object [PubSub](-pub-sub/index.md)  <br><br><br>
| [Repo](-repo/index.md)| [jvm]  <br>Content  <br>object [Repo](-repo/index.md)  <br><br><br>
| [Stats](-stats/index.md)| [jvm]  <br>Content  <br>object [Stats](-stats/index.md)  <br><br><br>
| [VersionResponse](-version-response/index.md)| [jvm]  <br>Content  <br>data class [VersionResponse](-version-response/index.md)(**commit**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **goLang**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **repo**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **version**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **system**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [add](add.md)| [jvm]  <br>Brief description  <br><br><br>Add data to ipfs.<br><br>  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [add](add.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)?, recurseDirectory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, wrapWithDirectory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, chunker: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, pin: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), onlyHash: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, trickle: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, rawLeaves: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, inline: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, inlineLimit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, fsCache: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, noCopy: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../-api-call/index.md)<[API.FileResponse](-file-response/index.md)>  <br><br><br>
| [equals](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [ls](ls.md)| [jvm]  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [ls](ls.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), stream: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ApiCall](../-api-call/index.md)<[API.LsResponse](-ls-response/index.md)>  <br><br><br>
| [toString](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [version](version.md)| [jvm]  <br>Content  <br>fun [version](version.md)(): [ApiCall](../-api-call/index.md)<[API.VersionResponse](-version-response/index.md)>  <br><br><br>


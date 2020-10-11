//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Repo](index.md)



# Repo  
 [jvm] object [Repo](index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [FsckResponse](-fsck-response/index.md)| [jvm]  <br>Brief description  <br><br><br>/api/v0/repo/fsck Remove repo lockfiles. cURL Example curl -X POST "http://127.0.0.1:5001/api/v0/repo/fsck"<br><br>  <br>Content  <br>data class [FsckResponse](-fsck-response/index.md)(**message**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br><br><br>
| [GcResponse](-gc-response/index.md)| [jvm]  <br>Content  <br>data class [GcResponse](-gc-response/index.md)(**key**: [API.Repo.GcResponse.GcError](-gc-response/-gc-error/index.md))  <br><br><br>
| [StatResponse](-stat-response/index.md)| [jvm]  <br>Content  <br>data class [StatResponse](-stat-response/index.md)(**numObjects**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **repoPath**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **version**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **repoSize**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **storageMax**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))  <br><br><br>
| [VerifyResponse](-verify-response/index.md)| [jvm]  <br>Content  <br>data class [VerifyResponse](-verify-response/index.md)(**msg**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **progress**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>
| [VersionResponse](-version-response/index.md)| [jvm]  <br>Content  <br>data class [VersionResponse](-version-response/index.md)(**version**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [fsck](fsck.md)| [jvm]  <br>Content  <br>fun [fsck](fsck.md)(): [ApiCall](../../-api-call/index.md)<[API.Repo.FsckResponse](-fsck-response/index.md)>  <br><br><br>
| [gc](gc.md)| [jvm]  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [gc](gc.md)(streamErrors: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, quiet: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Repo.GcResponse](-gc-response/index.md)>  <br><br><br>
| [hashCode](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [stat](stat.md)| [jvm]  <br>Brief description  <br><br><br>Get stats for the currently used repo.<br><br>  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [stat](stat.md)(sizeOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, human: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Repo.StatResponse](-stat-response/index.md)>  <br><br><br>
| [toString](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../../-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [verify](verify.md)| [jvm]  <br>Brief description  <br><br><br>Verify all blocks in repo are not corrupted.<br><br>  <br>Content  <br>fun [verify](verify.md)(): [ApiCall](../../-api-call/index.md)<[API.Repo.VerifyResponse](-verify-response/index.md)>  <br><br><br>
| [version](version.md)| [jvm]  <br>Brief description  <br><br><br>Show the repo version. curl -X POST "http://127.0.0.1:5001/api/v0/repo/version?quiet=<value>"<br><br>  <br>Content  <br>fun [version](version.md)(quiet: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Repo.VersionResponse](-version-response/index.md)>  <br><br><br>


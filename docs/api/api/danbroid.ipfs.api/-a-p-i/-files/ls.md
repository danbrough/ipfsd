//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Files](index.md)/[ls](ls.md)



# ls  
[jvm]  
Brief description  




api/v0/files/ls List directories in the local mutable namespace. cURL Example curl -X POST "http://127.0.0.1:5001/api/v0/files/ls?arg=<path>&long=<value>&U=<value>"



Arguments





## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| directoryOrder| <br><br>Do not sort; list entries in directory order. Required: no.<br><br>
| longListing| <br><br>Use long listing format. Required: no.<br><br>
| path| <br><br>Path to show listing for. Defaults to '/'. Required: no.<br><br>
  
  
Content  
@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  
  
fun [ls](ls.md)(path: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, longListing: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, directoryOrder: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Files.LsResponse](-ls-response/index.md)>  




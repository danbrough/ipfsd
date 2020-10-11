//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Repo](index.md)/[version](version.md)



# version  
[jvm]  
Brief description  


Show the repo version. curl -X POST "http://127.0.0.1:5001/api/v0/repo/version?quiet=<value>"



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| quiet| <br><br>Write minimal output. Required: no.<br><br>
  
  
Content  
fun [version](version.md)(quiet: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Repo.VersionResponse](-version-response/index.md)>  




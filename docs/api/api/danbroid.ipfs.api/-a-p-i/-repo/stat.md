//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Repo](index.md)/[stat](stat.md)



# stat  
[jvm]  
Brief description  


Get stats for the currently used repo.



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| human| <br><br>Print sizes in human readable format (e.g., 1K 234M 2G). Required: no.<br><br>
| sizeOnly| <br><br>Only report RepoSize and StorageMax. Required: no.<br><br>
  
  
Content  
@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  
  
fun [stat](stat.md)(sizeOnly: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, human: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Repo.StatResponse](-stat-response/index.md)>  




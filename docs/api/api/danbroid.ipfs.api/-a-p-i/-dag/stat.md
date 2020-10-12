//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Dag](index.md)/[stat](stat.md)



# stat  
[jvm]  
Brief description  


/api/v0/dag/stat Gets stats for a DAG



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| cid| <br><br>CID of a DAG root to get statistics for Required: yes.<br><br>
| progress| <br><br><br><br>Return progressive data while reading through the DAG. Default: true. Required: no.<br><br><br><br><h3>cURL Example</h3><br><br><br><br><pre>curl -X POST "http://127.0.0.1:5001/api/v0/dag/stat?arg=<root>&progress=true"</pre><br><br><br><br>
  
  
Content  
@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)()  
@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  
  
fun [stat](stat.md)(cid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), progress: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Dag.StatResponse](-stat-response/index.md)>  




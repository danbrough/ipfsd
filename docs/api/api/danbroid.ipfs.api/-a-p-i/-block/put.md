//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Block](index.md)/[put](put.md)



# put  
[jvm]  
Brief description  


/api/v0/block/put Store input as an IPFS block.



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| data| <br><br>Data to add<br><br>
| format| <br><br>cid format for blocks to be created with. Required: no.<br><br>
| mhLen| <br><br>multihash hash length. Default: -1. Required: no.<br><br>
| mhType| <br><br>multihash hash function. Default: sha2-256. Required: no.<br><br>
| pin| <br><br><br><br>pin added blocks recursively. Default: false. Required: no.<br><br><br><br><h3>cURL Example</h3> curl -X POST -F file=@myfile "http://127.0.0.1:5001/api/v0/block/put?format=<value>&mhtype=sha2-256&mhlen=-1&pin=false"<br><br><br><br>
  
  
Content  
@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  
  
fun [put](put.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, format: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, mhType: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, mhLen: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, pin: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Block.PutResponse](-put-response/index.md)>  




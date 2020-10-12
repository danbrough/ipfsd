//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Block](index.md)/[get](get.md)



# get  
[jvm]  
Brief description  


/api/v0/block/get Get a raw IPFS block.



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| cid| <br><br><br><br>The base58 multihash of an existing block to get.<br><br><br><br><h3>cURL Example</h3><br><br><br><br><pre>curl -X POST "http://127.0.0.1:5001/api/v0/block/get?arg=<key>"</pre><br><br><br><br>
  
  
Content  
fun [get](get.md)(cid: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), responseProcessor: [ResponseProcessor](../../index.md#danbroid.ipfs.api/ResponseProcessor///PointingToDeclaration/)<[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)>): [ApiCall](../../-api-call/index.md)<[Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)>  




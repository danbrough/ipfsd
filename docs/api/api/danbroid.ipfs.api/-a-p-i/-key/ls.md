//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Key](index.md)/[ls](ls.md)



# ls  
[jvm]  
Brief description  


List all local keypairs



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| ipnsBase| <br><br>Encoding used for keys: Can either be a multibase encoded CID or a base58btc encoded multihash. Takes {b58mh|base36|k|base32|b...}. Default: base36. Required: no.<br><br>
  
  
Content  
fun [ls](ls.md)(ipnsBase: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [ApiCall](../../-api-call/index.md)<[Types.Keys](../../-types/-keys/index.md)>  




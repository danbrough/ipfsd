//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Network](index.md)/[id](id.md)



# id  
[jvm]  
Brief description  


Show ipfs node id info.



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| format| <br><br>Supports the format option for output with the following keys: <id> : The peers id. <aver>: Agent version. <pver>: Protocol version. <pubkey>: Public key. <addrs>: Addresses (newline delimited).<br><br>
| peerID| <br><br>Peer.ID of node to look up. Required: no.<br><br>
| peerIDBase| <br><br>Encoding used for peer IDs: Can either be a multibase encoded CID or a base58btc encoded multihash. Takes {b58mh|base36|k|base32|b...}. Default: b58mh. Required: no.<br><br>
  
  
Content  
@[JvmStatic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-static/index.html)()  
@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  
  
fun [id](id.md)(peerID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, peerIDBase: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?): [ApiCall](../../-api-call/index.md)<[API.Network.ID](-i-d/index.md)>  




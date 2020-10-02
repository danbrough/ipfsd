//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[Types](../index.md)/[ID](index.md)



# ID  
 [jvm] data class [ID](index.md)(**id**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **agentVersion**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **protocolVersion**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **publicKey**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **protocols**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>, **addresses**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>)   


## Functions  
  
|  Name|  Summary| 
|---|---|
| [component1](component1.md)| [jvm]  <br>Content  <br>operator fun [component1](component1.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [component2](component2.md)| [jvm]  <br>Content  <br>operator fun [component2](component2.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [component3](component3.md)| [jvm]  <br>Content  <br>operator fun [component3](component3.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [component4](component4.md)| [jvm]  <br>Content  <br>operator fun [component4](component4.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [component5](component5.md)| [jvm]  <br>Content  <br>operator fun [component5](component5.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>  <br><br><br>
| [component6](component6.md)| [jvm]  <br>Content  <br>operator fun [component6](component6.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>  <br><br><br>
| [copy](copy.md)| [jvm]  <br>Content  <br>fun [copy](copy.md)(id: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), agentVersion: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), protocolVersion: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), publicKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), protocols: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>, addresses: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>): [Types.ID](index.md)  <br><br><br>
| [equals](../-name-value/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../-name-value/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../-name-value/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../-name-value/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toString](../-name-value/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../-name-value/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [addresses](index.md#danbroid.ipfs.api/Types.ID/addresses/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Addresses")  <br>  <br>val [addresses](index.md#danbroid.ipfs.api/Types.ID/addresses/#/PointingToDeclaration/): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>   <br>
| [agentVersion](index.md#danbroid.ipfs.api/Types.ID/agentVersion/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "AgentVersion")  <br>  <br>val [agentVersion](index.md#danbroid.ipfs.api/Types.ID/agentVersion/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [id](index.md#danbroid.ipfs.api/Types.ID/id/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "ID")  <br>  <br>val [id](index.md#danbroid.ipfs.api/Types.ID/id/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [protocols](index.md#danbroid.ipfs.api/Types.ID/protocols/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Protocols")  <br>  <br>val [protocols](index.md#danbroid.ipfs.api/Types.ID/protocols/#/PointingToDeclaration/): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>   <br>
| [protocolVersion](index.md#danbroid.ipfs.api/Types.ID/protocolVersion/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "ProtocolVersion")  <br>  <br>val [protocolVersion](index.md#danbroid.ipfs.api/Types.ID/protocolVersion/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [publicKey](index.md#danbroid.ipfs.api/Types.ID/publicKey/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "PublicKey")  <br>  <br>val [publicKey](index.md#danbroid.ipfs.api/Types.ID/publicKey/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>


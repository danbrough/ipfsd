//[api](../../index.md)/[danbroid.ipfs.api](../index.md)/[Types](index.md)



# Types  
 [jvm] object [Types](index.md)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [Config](-config/index.md)| [jvm]  <br>Content  <br>class [Config](-config/index.md)  <br><br><br>
| [File](-file/index.md)| [jvm]  <br>Content  <br>data class [File](-file/index.md)(**bytes**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **hash**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **size**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html))  <br><br><br>
| [ID](-i-d/index.md)| [jvm]  <br>Content  <br>data class [ID](-i-d/index.md)(**id**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **agentVersion**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **protocolVersion**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **publicKey**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **protocols**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>, **addresses**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>)  <br><br><br>
| [Key](-key/index.md)| [jvm]  <br>Content  <br>data class [Key](-key/index.md)(**name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **id**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br><br><br>
| [KeyError](-key-error/index.md)| [jvm]  <br>Content  <br>data class [KeyError](-key-error/index.md)(**key**: [Types.KeyError.GcError](-key-error/-gc-error/index.md))  <br><br><br>
| [Keys](-keys/index.md)| [jvm]  <br>Content  <br>data class [Keys](-keys/index.md)(**keys**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[Types.Key](-key/index.md)>)  <br><br><br>
| [Link](-link/index.md)| [jvm]  <br>Content  <br>data class [Link](-link/index.md)(**hash**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **size**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **target**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **type**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>
| [NameValue](-name-value/index.md)| [jvm]  <br>Content  <br>data class [NameValue](-name-value/index.md)(**name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **value**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br><br><br>
| [Object](-object/index.md)| [jvm]  <br>Content  <br>data class [Object](-object/index.md)(**hash**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **links**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[Types.Link](-link/index.md)>)  <br><br><br>
| [Objects](-objects/index.md)| [jvm]  <br>Content  <br>data class [Objects](-objects/index.md)(**objects**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[Types.Object](-object/index.md)>)  <br><br><br>
| [PubSub](-pub-sub/index.md)| [jvm]  <br>Content  <br>class [PubSub](-pub-sub/index.md)  <br><br><br>
| [Stats](-stats/index.md)| [jvm]  <br>Content  <br>class [Stats](-stats/index.md)  <br><br><br>
| [Version](-version/index.md)| [jvm]  <br>Content  <br>data class [Version](-version/index.md)(**commit**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **goLang**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **repo**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **version**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **system**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](-config/-config-change/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](-config/-config-change/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](-config/-config-change/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](-config/-config-change/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toString](-config/-config-change/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](-config/-config-change/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


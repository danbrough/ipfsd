//[api](../../../index.md)/[danbroid.ipfs.api](../../index.md)/[API](../index.md)/[Object](index.md)



# Object  
 [jvm] data class [Object](index.md)(**hash**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **links**: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[API.Object.Link](-link/index.md)>)   


## Types  
  
|  Name|  Summary| 
|---|---|
| [Link](-link/index.md)| [jvm]  <br>Content  <br>data class [Link](-link/index.md)(**hash**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **size**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **target**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **type**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [component1](component1.md)| [jvm]  <br>Content  <br>operator fun [component1](component1.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [component2](component2.md)| [jvm]  <br>Content  <br>operator fun [component2](component2.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[API.Object.Link](-link/index.md)>  <br><br><br>
| [copy](copy.md)| [jvm]  <br>Content  <br>fun [copy](copy.md)(hash: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), links: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[API.Object.Link](-link/index.md)>): [API.Object](index.md)  <br><br><br>
| [equals](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toString](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../../../danbroid.ipfs.api.okhttp/-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [hash](index.md#danbroid.ipfs.api/API.Object/hash/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Hash")  <br>  <br>val [hash](index.md#danbroid.ipfs.api/API.Object/hash/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [links](index.md#danbroid.ipfs.api/API.Object/links/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Links")  <br>  <br>val [links](index.md#danbroid.ipfs.api/API.Object/links/#/PointingToDeclaration/): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[API.Object.Link](-link/index.md)>   <br>


//[api](../../../../index.md)/[danbroid.ipfs.api](../../../index.md)/[API](../../index.md)/[Object](../index.md)/[Link](index.md)



# Link  
 [jvm] data class [Link](index.md)(**hash**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **name**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **size**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **target**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **type**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))   


## Functions  
  
|  Name|  Summary| 
|---|---|
| [component1](component1.md)| [jvm]  <br>Content  <br>operator fun [component1](component1.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [component2](component2.md)| [jvm]  <br>Content  <br>operator fun [component2](component2.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [component3](component3.md)| [jvm]  <br>Content  <br>operator fun [component3](component3.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)  <br><br><br>
| [component4](component4.md)| [jvm]  <br>Content  <br>operator fun [component4](component4.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>
| [component5](component5.md)| [jvm]  <br>Content  <br>operator fun [component5](component5.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [copy](copy.md)| [jvm]  <br>Content  <br>fun [copy](copy.md)(hash: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), size: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), target: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), type: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [API.Object.Link](index.md)  <br><br><br>
| [equals](../../../-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](../../../-ok-http-call-executor/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](../../../-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](../../../-ok-http-call-executor/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toString](../../../-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](../../../-ok-http-call-executor/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [hash](index.md#danbroid.ipfs.api/API.Object.Link/hash/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Hash")  <br>  <br>val [hash](index.md#danbroid.ipfs.api/API.Object.Link/hash/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [isDirectory](index.md#danbroid.ipfs.api/API.Object.Link/isDirectory/#/PointingToDeclaration/)|  [jvm] val [isDirectory](index.md#danbroid.ipfs.api/API.Object.Link/isDirectory/#/PointingToDeclaration/): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)   <br>
| [isFile](index.md#danbroid.ipfs.api/API.Object.Link/isFile/#/PointingToDeclaration/)|  [jvm] val [isFile](index.md#danbroid.ipfs.api/API.Object.Link/isFile/#/PointingToDeclaration/): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)   <br>
| [name](index.md#danbroid.ipfs.api/API.Object.Link/name/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Name")  <br>  <br>val [name](index.md#danbroid.ipfs.api/API.Object.Link/name/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [size](index.md#danbroid.ipfs.api/API.Object.Link/size/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Size")  <br>  <br>val [size](index.md#danbroid.ipfs.api/API.Object.Link/size/#/PointingToDeclaration/): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)   <br>
| [target](index.md#danbroid.ipfs.api/API.Object.Link/target/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Target")  <br>  <br>val [target](index.md#danbroid.ipfs.api/API.Object.Link/target/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [type](index.md#danbroid.ipfs.api/API.Object.Link/type/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "Type")  <br>  <br>val [type](index.md#danbroid.ipfs.api/API.Object.Link/type/#/PointingToDeclaration/): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)   <br>


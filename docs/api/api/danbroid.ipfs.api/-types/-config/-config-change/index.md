//[api](../../../../index.md)/[danbroid.ipfs.api](../../../index.md)/[Types](../../index.md)/[Config](../index.md)/[ConfigChange](index.md)



# ConfigChange  
 [jvm] data class [ConfigChange](index.md)(**newConfig**: JsonObject, **oldConfig**: JsonObject)   


## Functions  
  
|  Name|  Summary| 
|---|---|
| [component1](component1.md)| [jvm]  <br>Content  <br>operator fun [component1](component1.md)(): JsonObject  <br><br><br>
| [component2](component2.md)| [jvm]  <br>Content  <br>operator fun [component2](component2.md)(): JsonObject  <br><br><br>
| [copy](copy.md)| [jvm]  <br>Content  <br>fun [copy](copy.md)(newConfig: JsonObject, oldConfig: JsonObject): [Types.Config.ConfigChange](index.md)  <br><br><br>
| [equals](index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [jvm]  <br>Content  <br>open operator override fun [equals](index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [hashCode](index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [hashCode](index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toString](index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [jvm]  <br>Content  <br>open override fun [toString](index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [newConfig](index.md#danbroid.ipfs.api/Types.Config.ConfigChange/newConfig/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "NewCfg")  <br>  <br>val [newConfig](index.md#danbroid.ipfs.api/Types.Config.ConfigChange/newConfig/#/PointingToDeclaration/): JsonObject   <br>
| [oldConfig](index.md#danbroid.ipfs.api/Types.Config.ConfigChange/oldConfig/#/PointingToDeclaration/)|  [jvm] @SerializedName(value = "OldCfg")  <br>  <br>val [oldConfig](index.md#danbroid.ipfs.api/Types.Config.ConfigChange/oldConfig/#/PointingToDeclaration/): JsonObject   <br>


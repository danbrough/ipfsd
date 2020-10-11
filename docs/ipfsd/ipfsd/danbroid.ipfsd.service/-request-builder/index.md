//[ipfsd](../../index.md)/[danbroid.ipfsd.service](../index.md)/[RequestBuilder](index.md)



# RequestBuilder  
 [androidJvm] 

RequestBuilder is an IPFS command request builder.

open class [RequestBuilder](index.md)   


## Constructors  
  
|  Name|  Summary| 
|---|---|
| [RequestBuilder](-request-builder.md)|  [androidJvm] <br><br>Package-Private class constructor using RequestBuilder passed by IPFS.newRequest method.<br><br>open fun [RequestBuilder](-request-builder.md)(requestBuilder: RequestBuilder)   <br>


## Types  
  
|  Name|  Summary| 
|---|---|
| [RequestBuilderException](-request-builder-exception/index.md)| [androidJvm]  <br>Content  <br>open class [RequestBuilderException](-request-builder-exception/index.md) : [Exception](https://developer.android.com/reference/kotlin/java/lang/Exception.html)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [send](send.md)| [androidJvm]  <br>Brief description  <br><br><br>Sends the request to the underlying go-ipfs node.<br><br>  <br>Content  <br>open fun [send](send.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)>  <br><br><br>
| [sendToJSONList](send-to-j-s-o-n-list.md)| [androidJvm]  <br>Brief description  <br><br><br>Sends the request to the underlying go-ipfs node and returns an array of JSONObject.<br><br>  <br>Content  <br>open fun [sendToJSONList](send-to-j-s-o-n-list.md)(): [ArrayList](https://developer.android.com/reference/kotlin/java/util/ArrayList.html)<[JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html)>  <br><br><br>
| [withArgument](with-argument.md)| [androidJvm]  <br>Brief description  <br><br><br>Adds an argument to the request.<br><br>  <br>Content  <br>open fun [withArgument](with-argument.md)(argument: [String](https://developer.android.com/reference/kotlin/java/lang/String.html)): [RequestBuilder](index.md)  <br><br><br>
| [withBody](with-body.md)| [androidJvm]  <br>Brief description  <br><br><br>Adds a byte array body to the request.<br><br>  <br>Content  <br>open fun [withBody](with-body.md)(body: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)>): [RequestBuilder](index.md)  <br><br><br>[androidJvm]  <br>Brief description  <br><br><br>Adds a string body to the request.<br><br>  <br>Content  <br>open fun [withBody](with-body.md)(body: [String](https://developer.android.com/reference/kotlin/java/lang/String.html)): [RequestBuilder](index.md)  <br><br><br>
| [withHeader](with-header.md)| [androidJvm]  <br>Brief description  <br><br><br>Adds a header to the request.<br><br>  <br>Content  <br>open fun [withHeader](with-header.md)(key: [String](https://developer.android.com/reference/kotlin/java/lang/String.html), value: [String](https://developer.android.com/reference/kotlin/java/lang/String.html)): [RequestBuilder](index.md)  <br><br><br>
| [withOption](with-option.md)| [androidJvm]  <br>Brief description  <br><br><br>Adds a boolean option to the request.<br><br>  <br>Content  <br>open fun [withOption](with-option.md)(option: [String](https://developer.android.com/reference/kotlin/java/lang/String.html), value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [RequestBuilder](index.md)  <br><br><br>[androidJvm]  <br>Brief description  <br><br><br>Adds a byte array option to the request.<br><br>  <br>Content  <br>open fun [withOption](with-option.md)(option: [String](https://developer.android.com/reference/kotlin/java/lang/String.html), value: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)<[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)>): [RequestBuilder](index.md)  <br><br><br>[androidJvm]  <br>Brief description  <br><br><br>Adds a string option to the request.<br><br>  <br>Content  <br>open fun [withOption](with-option.md)(option: [String](https://developer.android.com/reference/kotlin/java/lang/String.html), value: [String](https://developer.android.com/reference/kotlin/java/lang/String.html)): [RequestBuilder](index.md)  <br><br><br>


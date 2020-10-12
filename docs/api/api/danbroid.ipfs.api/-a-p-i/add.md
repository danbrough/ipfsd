//[api](../../index.md)/[danbroid.ipfs.api](../index.md)/[API](index.md)/[add](add.md)



# add  
[jvm]  
Brief description  


Add data to ipfs.



## Parameters  
  
jvm  
  
|  Name|  Summary| 
|---|---|
| chunker| <br><br>Chunking algorithm, size-<bytes>, rabin-<min>-<avg>-<max> or buzhash. Default: size-262144<br><br>
| data| <br><br>String data to add<br><br>
| fsCache| <br><br>: Check the filestore for pre-existing blocks. (experimental). Required: no.<br><br>
| inline| <br><br>Inline small blocks into CIDs. (experimental). Required: no.<br><br>
| inlineLimit| <br><br>Maximum block size to inline. (experimental). Default: 32. Required: no.<br><br>
| noCopy| <br><br>Add the file using filestore. Implies raw-leaves. (experimental). Required: no.<br><br>
| onlyHash| <br><br>Only chunk and hash - do not write to disk<br><br>
| pin| <br><br>Whether to pin the content<br><br>
| rawLeaves| <br><br>Use raw blocks for leaf nodes. (experimental). Required: no.<br><br>
| trickle| <br><br>Use trickle-dag format for dag generation. Required: no.<br><br>
| wrapWithDirectory| <br><br>Wrap files with a directory object<br><br>
  
  
Content  
@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  
  
fun [add](add.md)(data: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)?, recurseDirectory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, fileName: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, wrapWithDirectory: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, chunker: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, pin: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), onlyHash: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, trickle: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, rawLeaves: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, inline: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, inlineLimit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)?, fsCache: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?, noCopy: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)?): [ApiCall](../-api-call/index.md)<[API.FileResponse](-file-response/index.md)>  




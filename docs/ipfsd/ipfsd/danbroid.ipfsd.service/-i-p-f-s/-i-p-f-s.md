//[ipfsd](../../index.md)/[danbroid.ipfsd.service](../index.md)/[IPFS](index.md)/[IPFS](-i-p-f-s.md)



# IPFS  
[androidJvm]  
Brief description  


Class constructor using defaultRepoPath "/ipfs/repo" on internal storage.



## Parameters  
  
androidJvm  
  
|  Name|  Summary| 
|---|---|
| context| <br><br>The application context<br><br>
  
  
Content  
open fun [IPFS](-i-p-f-s.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))  


[androidJvm]  
Brief description  


Class constructor using repoPath passed as parameter on internal storage.



## Parameters  
  
androidJvm  
  
|  Name|  Summary| 
|---|---|
| context| <br><br>The application context<br><br>
| repoPath| <br><br>The path of the go-ipfs repo (relative to internal root)<br><br>
  
  
Content  
open fun [IPFS](-i-p-f-s.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), repoPath: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html))  


[androidJvm]  
Brief description  


Class constructor using repoPath and storage location passed as parameters.



## Parameters  
  
androidJvm  
  
|  Name|  Summary| 
|---|---|
| context| <br><br>The application context<br><br>
| internalStorage| <br><br>true, if the desired storage location for the repo path is internal false, if the desired storage location for the repo path is external<br><br>
| repoPath| <br><br>The path of the go-ipfs repo (relative to internal/external root)<br><br>
  
  
Content  
open fun [IPFS](-i-p-f-s.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), repoPath: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), internalStorage: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))  




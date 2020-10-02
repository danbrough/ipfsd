//[app](../index.md)/[danbroid.ipfsd.demo.sync](index.md)



# Package danbroid.ipfsd.demo.sync  


## Types  
  
|  Name|  Summary| 
|---|---|
| [AccountAuthenticator](-account-authenticator/index.md)| [androidJvm]  <br>Content  <br>class [AccountAuthenticator](-account-authenticator/index.md)(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)) : [AbstractAccountAuthenticator](https://developer.android.com/reference/kotlin/android/accounts/AbstractAccountAuthenticator.html)  <br><br><br>
| [AuthenticatorService](-authenticator-service/index.md)| [androidJvm]  <br>Brief description  <br><br><br>A bound Service that instantiates the authenticator when started.<br><br>  <br>Content  <br>class [AuthenticatorService](-authenticator-service/index.md) : [Service](https://developer.android.com/reference/kotlin/android/app/Service.html)  <br><br><br>
| [DBContentProvider](-d-b-content-provider/index.md)| [androidJvm]  <br>Content  <br>class [DBContentProvider](-d-b-content-provider/index.md) : [ContentProvider](https://developer.android.com/reference/kotlin/android/content/ContentProvider.html)  <br><br><br>
| [DBSyncAdapter](-d-b-sync-adapter/index.md)| [androidJvm]  <br>Brief description  <br><br><br>Handle the transfer of data between a server and an app, using the Android sync adapter framework.<br><br>  <br>Content  <br>class [DBSyncAdapter](-d-b-sync-adapter/index.md)@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()constructor(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), **autoInitialize**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), **allowParallelSyncs**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), **contentResolver**: [ContentResolver](https://developer.android.com/reference/kotlin/android/content/ContentResolver.html)) : [AbstractThreadedSyncAdapter](https://developer.android.com/reference/kotlin/android/content/AbstractThreadedSyncAdapter.html)  <br><br><br>
| [SyncService](-sync-service/index.md)| [androidJvm]  <br>Brief description  <br><br><br>Define a Service that returns an [android.os.IBinder](https://developer.android.com/reference/kotlin/android/os/IBinder.html) for the sync adapter class, allowing the sync adapter framework to call onPerformSync().<br><br>  <br>Content  <br>class [SyncService](-sync-service/index.md) : [Service](https://developer.android.com/reference/kotlin/android/app/Service.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [ACCOUNT](index.md#danbroid.ipfsd.demo.sync//ACCOUNT/#/PointingToDeclaration/)|  [androidJvm] const val [ACCOUNT](index.md#danbroid.ipfsd.demo.sync//ACCOUNT/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [ACCOUNT_TYPE](index.md#danbroid.ipfsd.demo.sync//ACCOUNT_TYPE/#/PointingToDeclaration/)|  [androidJvm] const val [ACCOUNT_TYPE](index.md#danbroid.ipfsd.demo.sync//ACCOUNT_TYPE/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>
| [AUTHORITY](index.md#danbroid.ipfsd.demo.sync//AUTHORITY/#/PointingToDeclaration/)|  [androidJvm] const val [AUTHORITY](index.md#danbroid.ipfsd.demo.sync//AUTHORITY/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>


//[app](../../index.md)/[danbroid.ipfsd.demo.sync](../index.md)/[DBSyncAdapter](index.md)



# DBSyncAdapter  
 [androidJvm] 

Handle the transfer of data between a server and an app, using the Android sync adapter framework.

class [DBSyncAdapter](index.md)@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()constructor(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), **autoInitialize**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), **allowParallelSyncs**: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), **contentResolver**: [ContentResolver](https://developer.android.com/reference/kotlin/android/content/ContentResolver.html)) : [AbstractThreadedSyncAdapter](https://developer.android.com/reference/kotlin/android/content/AbstractThreadedSyncAdapter.html)   


## Constructors  
  
|  Name|  Summary| 
|---|---|
| [DBSyncAdapter](-d-b-sync-adapter.md)|  [androidJvm] @[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [DBSyncAdapter](-d-b-sync-adapter.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), autoInitialize: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), allowParallelSyncs: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), contentResolver: [ContentResolver](https://developer.android.com/reference/kotlin/android/content/ContentResolver.html))   <br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [equals](../../danbroid.ipfsd.demo.ui.www/-nested-scroll-web-view/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open operator override fun [equals](../../danbroid.ipfsd.demo.ui.www/-nested-scroll-web-view/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [getContext](index.md#android.content/AbstractThreadedSyncAdapter/getContext/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [getContext](index.md#android.content/AbstractThreadedSyncAdapter/getContext/#/PointingToDeclaration/)(): [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)  <br><br><br>
| [getSyncAdapterBinder](index.md#android.content/AbstractThreadedSyncAdapter/getSyncAdapterBinder/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>override fun [getSyncAdapterBinder](index.md#android.content/AbstractThreadedSyncAdapter/getSyncAdapterBinder/#/PointingToDeclaration/)(): [IBinder](https://developer.android.com/reference/kotlin/android/os/IBinder.html)  <br><br><br>
| [hashCode](../../danbroid.ipfsd.demo.ui.www/-nested-scroll-web-view/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [hashCode](../../danbroid.ipfsd.demo.ui.www/-nested-scroll-web-view/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [onPerformSync](on-perform-sync.md)| [androidJvm]  <br>Content  <br>open override fun [onPerformSync](on-perform-sync.md)(account: [Account](https://developer.android.com/reference/kotlin/android/accounts/Account.html)?, extras: [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)?, authority: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, provider: [ContentProviderClient](https://developer.android.com/reference/kotlin/android/content/ContentProviderClient.html)?, syncResult: [SyncResult](https://developer.android.com/reference/kotlin/android/content/SyncResult.html)?)  <br><br><br>
| [onSecurityException](on-security-exception.md)| [androidJvm]  <br>Content  <br>open override fun [onSecurityException](on-security-exception.md)(account: [Account](https://developer.android.com/reference/kotlin/android/accounts/Account.html)?, extras: [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)?, authority: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)?, syncResult: [SyncResult](https://developer.android.com/reference/kotlin/android/content/SyncResult.html)?)  <br><br><br>
| [onSyncCanceled](on-sync-canceled.md)| [androidJvm]  <br>Content  <br>open override fun [onSyncCanceled](on-sync-canceled.md)()  <br>open override fun [onSyncCanceled](index.md#android.content/AbstractThreadedSyncAdapter/onSyncCanceled/#java.lang.Thread/PointingToDeclaration/)(p0: [Thread](https://developer.android.com/reference/kotlin/java/lang/Thread.html))  <br><br><br>
| [onUnsyncableAccount](on-unsyncable-account.md)| [androidJvm]  <br>Content  <br>open override fun [onUnsyncableAccount](on-unsyncable-account.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [toString](../../danbroid.ipfsd.demo.ui.www/-browser-fragment/-web-client/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [toString](../../danbroid.ipfsd.demo.ui.www/-browser-fragment/-web-client/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [contentResolver](index.md#danbroid.ipfsd.demo.sync/DBSyncAdapter/contentResolver/#/PointingToDeclaration/)|  [androidJvm] val [contentResolver](index.md#danbroid.ipfsd.demo.sync/DBSyncAdapter/contentResolver/#/PointingToDeclaration/): [ContentResolver](https://developer.android.com/reference/kotlin/android/content/ContentResolver.html)   <br>


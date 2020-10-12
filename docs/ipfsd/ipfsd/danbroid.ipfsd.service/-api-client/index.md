//[ipfsd](../../index.md)/[danbroid.ipfsd.service](../index.md)/[ApiClient](index.md)



# ApiClient  
 [androidJvm] class [ApiClient](index.md)(**context**: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), **port**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **urlBase**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : OkHttpCallExecutor   


## Functions  
  
|  Name|  Summary| 
|---|---|
| [close](close.md)| [androidJvm]  <br>Content  <br>fun [close](close.md)()  <br><br><br>
| [equals](../../danbroid.ipfsd.service.settings/-settings-activity/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open operator override fun [equals](../../danbroid.ipfsd.service.settings/-settings-activity/-companion/index.md#kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [exec](index.md#danbroid.ipfs.api/CallExecutor/exec/#danbroid.ipfs.api.ApiCall[TypeParam(bounds=[kotlin.Any?])]#danbroid.ipfs.api.CallExecutor.JavaResultHandler[TypeParam(bounds=[kotlin.Any?])]/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun <T> [exec](index.md#danbroid.ipfs.api/CallExecutor/exec/#danbroid.ipfs.api.ApiCall[TypeParam(bounds=[kotlin.Any?])]#danbroid.ipfs.api.CallExecutor.JavaResultHandler[TypeParam(bounds=[kotlin.Any?])]/PointingToDeclaration/)(call: ApiCall<T>, handler: CallExecutor.JavaResultHandler<T>)  <br>open suspend override fun <[T](exec.md)> [exec](exec.md)(call: ApiCall<[T](exec.md)>, handler: ResultHandler<[T](exec.md)>)  <br><br><br>
| [hashCode](../../danbroid.ipfsd.service.settings/-settings-activity/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [hashCode](../../danbroid.ipfsd.service.settings/-settings-activity/-companion/index.md#kotlin/Any/hashCode/#/PointingToDeclaration/)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| [toInputSource](index.md#danbroid.ipfs.api.okhttp/OkHttpCallExecutor/toInputSource/okhttp3.ResponseBody#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>override fun ResponseBody.[toInputSource](index.md#danbroid.ipfs.api.okhttp/OkHttpCallExecutor/toInputSource/okhttp3.ResponseBody#/PointingToDeclaration/)(): OkHttpCallExecutor$toInputSource$1  <br><br><br>
| [toString](../../danbroid.ipfsd.service.settings/-settings-activity/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)| [androidJvm]  <br>Content  <br>open override fun [toString](../../danbroid.ipfsd.service.settings/-settings-activity/-companion/index.md#kotlin/Any/toString/#/PointingToDeclaration/)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| [context](index.md#danbroid.ipfsd.service/ApiClient/context/#/PointingToDeclaration/)|  [androidJvm] val [context](index.md#danbroid.ipfsd.service/ApiClient/context/#/PointingToDeclaration/): [Context](https://developer.android.com/reference/kotlin/android/content/Context.html)   <br>
| [httpClient](index.md#danbroid.ipfsd.service/ApiClient/httpClient/#/PointingToDeclaration/)|  [androidJvm] override val [httpClient](index.md#danbroid.ipfsd.service/ApiClient/httpClient/#/PointingToDeclaration/): OkHttpClient   <br>
| [ipfsClient](index.md#danbroid.ipfsd.service/ApiClient/ipfsClient/#/PointingToDeclaration/)|  [androidJvm] var [ipfsClient](index.md#danbroid.ipfsd.service/ApiClient/ipfsClient/#/PointingToDeclaration/): [IPFSClient](../-i-p-f-s-client/index.md)?   <br>
| [okHttpClientNoTimeout](index.md#danbroid.ipfsd.service/ApiClient/okHttpClientNoTimeout/#/PointingToDeclaration/)|  [androidJvm] override val [okHttpClientNoTimeout](index.md#danbroid.ipfsd.service/ApiClient/okHttpClientNoTimeout/#/PointingToDeclaration/): OkHttpClient   <br>
| [urlBase](index.md#danbroid.ipfsd.service/ApiClient/urlBase/#/PointingToDeclaration/)|  [androidJvm] override val [urlBase](index.md#danbroid.ipfsd.service/ApiClient/urlBase/#/PointingToDeclaration/): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)   <br>


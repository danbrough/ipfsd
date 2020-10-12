//[ipfsd](../../index.md)/[danbroid.ipfsd.service](../index.md)/[IPFS](index.md)



# IPFS  
 [androidJvm] 

IPFS is a class that wraps a go-ipfs node and its shell over UDS.

open class [IPFS](index.md)   


## Constructors  
  
|  Name|  Summary| 
|---|---|
| [IPFS](-i-p-f-s.md)|  [androidJvm] <br><br>Class constructor using defaultRepoPath "/ipfs/repo" on internal storage.<br><br>open fun [IPFS](-i-p-f-s.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html))   <br>
| [IPFS](-i-p-f-s.md)|  [androidJvm] <br><br>Class constructor using repoPath passed as parameter on internal storage.<br><br>open fun [IPFS](-i-p-f-s.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), repoPath: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html))   <br>
| [IPFS](-i-p-f-s.md)|  [androidJvm] <br><br>Class constructor using repoPath and storage location passed as parameters.<br><br>open fun [IPFS](-i-p-f-s.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), repoPath: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), internalStorage: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))   <br>


## Types  
  
|  Name|  Summary| 
|---|---|
| [ConfigCreationException](-config-creation-exception/index.md)| [androidJvm]  <br>Content  <br>open class [ConfigCreationException](-config-creation-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [ConfigGettingException](-config-getting-exception/index.md)| [androidJvm]  <br>Content  <br>open class [ConfigGettingException](-config-getting-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [ConfigSettingException](-config-setting-exception/index.md)| [androidJvm]  <br>Content  <br>open class [ConfigSettingException](-config-setting-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [ExtraOptionException](-extra-option-exception/index.md)| [androidJvm]  <br>Content  <br>open class [ExtraOptionException](-extra-option-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [NodeStartException](-node-start-exception/index.md)| [androidJvm]  <br>Content  <br>open class [NodeStartException](-node-start-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [NodeStopException](-node-stop-exception/index.md)| [androidJvm]  <br>Content  <br>open class [NodeStopException](-node-stop-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [RepoInitException](-repo-init-exception/index.md)| [androidJvm]  <br>Content  <br>open class [RepoInitException](-repo-init-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [RepoOpenException](-repo-open-exception/index.md)| [androidJvm]  <br>Content  <br>open class [RepoOpenException](-repo-open-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [ShellRequestException](-shell-request-exception/index.md)| [androidJvm]  <br>Content  <br>open class [ShellRequestException](-shell-request-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>
| [SockManagerException](-sock-manager-exception/index.md)| [androidJvm]  <br>Content  <br>open class [SockManagerException](-sock-manager-exception/index.md) : [Exception](https://docs.oracle.com/javase/8/docs/api/java/lang/Exception.html)  <br><br><br>


## Functions  
  
|  Name|  Summary| 
|---|---|
| [enableNamesysPubsub](enable-namesys-pubsub.md)| [androidJvm]  <br>Brief description  <br><br><br>Enable IPNS record distribution through pubsub; enables pubsub.<br><br>  <br>Content  <br>open fun [enableNamesysPubsub](enable-namesys-pubsub.md)()  <br><br><br>
| [enablePubsubExperiment](enable-pubsub-experiment.md)| [androidJvm]  <br>Brief description  <br><br><br>Instantiate the ipfs node with the experimental pubsub feature enabled.<br><br>  <br>Content  <br>open fun [enablePubsubExperiment](enable-pubsub-experiment.md)()  <br><br><br>
| [getConfig](get-config.md)| [androidJvm]  <br>Brief description  <br><br><br>Gets the IPFS instance config as a JSON.<br><br>  <br>Content  <br>open fun [getConfig](get-config.md)(): [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html)  <br><br><br>
| [getConfigKey](get-config-key.md)| [androidJvm]  <br>Brief description  <br><br><br>Gets the JSON value associated to the key passed as parameter in the IPFS instance config.<br><br>  <br>Content  <br>open fun [getConfigKey](get-config-key.md)(key: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html)  <br><br><br>
| [getRepoAbsolutePath](get-repo-absolute-path.md)| [androidJvm]  <br>Brief description  <br><br><br>Returns the repo absolute path as a string.<br><br>  <br>Content  <br>open fun [getRepoAbsolutePath](get-repo-absolute-path.md)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)  <br><br><br>
| [isStarted](is-started.md)| [androidJvm]  <br>Brief description  <br><br><br>Returns true if this IPFS instance is "started" by checking if the underlying go-ipfs node is instantiated.<br><br>  <br>Content  <br>open fun [isStarted](is-started.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| [newRequest](new-request.md)| [androidJvm]  <br>Brief description  <br><br><br>Creates and returns a RequestBuilder associated to this IPFS instance shell.<br><br>  <br>Content  <br>open fun [newRequest](new-request.md)(command: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)): [RequestBuilder](../-request-builder/index.md)  <br><br><br>
| [restart](restart.md)| [androidJvm]  <br>Brief description  <br><br><br>Restarts this IPFS instance.<br><br>  <br>Content  <br>open fun [restart](restart.md)()  <br><br><br>
| [setConfig](set-config.md)| [androidJvm]  <br>Brief description  <br><br><br>Sets JSON config passed as parameter as IPFS config or reset to default config (with a new identity) if the config parameter is null.**A started instance must be restarted for its config to be applied.**<br><br>  <br>Content  <br>open fun [setConfig](set-config.md)(config: [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html))  <br><br><br>
| [setConfigKey](set-config-key.md)| [androidJvm]  <br>Brief description  <br><br><br>Sets JSON config value to the key passed as parameters in the IPFS instance config.**A started instance must be restarted for its config to be applied.**<br><br>  <br>Content  <br>open fun [setConfigKey](set-config-key.md)(key: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), value: [JSONObject](https://developer.android.com/reference/kotlin/org/json/JSONObject.html))  <br><br><br>
| [setDNSPair](set-d-n-s-pair.md)| [androidJvm]  <br>Brief description  <br><br><br>Sets the primary and secondary DNS for gomobile (hacky, will be removed in future version)<br><br>  <br>Content  <br>open fun [setDNSPair](set-d-n-s-pair.md)(primary: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), secondary: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html))  <br><br><br>
| [start](start.md)| [androidJvm]  <br>Brief description  <br><br><br>Starts this IPFS instance.<br><br>  <br>Content  <br>open fun [start](start.md)()  <br><br><br>
| [stop](stop.md)| [androidJvm]  <br>Brief description  <br><br><br>Stops this IPFS instance.<br><br>  <br>Content  <br>open fun [stop](stop.md)()  <br><br><br>


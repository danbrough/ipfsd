# Module api

## IPFS API

This is an implementation of the IPFS api written in kotlin but supporting java.

### Usage


```kotlin

//Create an HTTP api client for the local node
val executor = OkHttpCallExecutor()

//Start a coroutine
runBlocking {
    val msg = "${javaClass.simpleName} addMessage at ${Date()}\n"
    //execute an API call
    executor.exec(API.add(msg, fileName = "test_message.txt")) {
      println("Recevied response: $it")
    }
}
```

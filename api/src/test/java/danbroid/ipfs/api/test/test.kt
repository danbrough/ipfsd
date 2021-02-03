package danbroid.ipfs.api.test

import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.okhttp.OkHttpExecutor
import org.slf4j.LoggerFactory


object api : IPFS(OkHttpExecutor())

private val log = LoggerFactory.getLogger(api::class.java)


/*
private int responseCount(Response response) {
    int result = 1;
    while ((response = response.priorResponse()) != null) {
        result++;
    }
    return result;
}
*/

package danbroid.ipfs.api.test

import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.okhttp.OkHttpExecutor


object api : IPFS(OkHttpExecutor())


/*
private int responseCount(Response response) {
    int result = 1;
    while ((response = response.priorResponse()) != null) {
        result++;
    }
    return result;
}
*/

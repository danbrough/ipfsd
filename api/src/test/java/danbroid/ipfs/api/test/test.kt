package danbroid.ipfs.api.test

import danbroid.ipfs.api.IPFS
import danbroid.ipfs.api.okhttp.OkHttpExecutor


object api : IPFS(OkHttpExecutor("http://192.168.1.4:5001/api/v0"))


/*
private int responseCount(Response response) {
    int result = 1;
    while ((response = response.priorResponse()) != null) {
        result++;
    }
    return result;
}
*/

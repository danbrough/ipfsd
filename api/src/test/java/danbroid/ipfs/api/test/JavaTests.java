package danbroid.ipfs.api.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import danbroid.ipfs.api.API;
import danbroid.ipfs.api.okhttp.OkHttpCallExecutor;

public class JavaTests extends CallTest {
    private static final Logger log = LoggerFactory.getLogger(JavaTests.class);

    @Test
    public void test1() {
        log.info("running test1()");

        new OkHttpCallExecutor(5001).exec(API.Key.ls(), (result) -> {
            log.warn("received result: {}", result);
        });



/*    api.key.ls().exec(new ApiCall.FlowCallback<Types.Keys>() {

      @Override
      public void onCompletion(Throwable thr) {
        if (thr != null)
          log.error("Call failed. " + thr.getMessage(), thr);
      }

      @Override
      public void onResult(Types.Keys result) {
        log.info("RESULT: {}", result);
      }
    });*/

    }
}

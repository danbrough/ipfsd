package danbroid.ipfs.api.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import danbroid.ipfs.api.ApiCall;
import danbroid.ipfs.api.Types;

public class JavaTests extends CallTest {
  private static final Logger log = LoggerFactory.getLogger(JavaTests.class);

  @Test
  public void test1() {
    log.info("running test1()");
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

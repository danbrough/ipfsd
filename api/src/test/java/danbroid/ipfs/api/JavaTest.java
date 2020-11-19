package danbroid.ipfs.api;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import danbroid.ipfs.api.okhttp.OkHttpCallExecutor;

public class JavaTest {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JavaTest.class);
  IPFSAPI ipfs;

  @Before
  public void onSetup() {
    log.warn("onSetup()");
    ipfs = new IPFSAPI(new OkHttpCallExecutor());
  }

  @After
  public void onTearDown() {
    log.warn("onTearDown()");
  }

  @Test
  public void test() {
    API.Basic.VersionResponse versionResponse = ipfs.basic.version().getBlocking().valueOrThrow();
    log.info("Version: " + versionResponse);
  }
}

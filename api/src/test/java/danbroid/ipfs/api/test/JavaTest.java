package danbroid.ipfs.api.test;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import danbroid.ipfs.api.API;


public class JavaTest {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JavaTest.class);
  API ipfs;

  @Before
  public void onSetup() {
    log.warn("onSetup()");
    ipfs = new API();
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

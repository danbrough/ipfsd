// Code generated by gobind. DO NOT EDIT.

// Java class core.Core is a proxy for talking to a Go program.
//
//   autogenerated by gobind -lang=java github.com/ipfs-shipyard/gomobile-ipfs/go/bind/core
package core;

import go.Seq;

public abstract class Core {
	static {
		Seq.touch(); // for loading the native library
		_init();
	}
	
	private Core() {} // uninstantiable
	
	// touch is called from other bound packages to initialize this package
	public static void touch() {}
	
	private static native void _init();
	
	
	public static final String UDSDir = "sock";
	
	public static native void initRepo(String path, Config cfg) throws Exception;
	public static native Config newConfig(byte[] raw_json) throws Exception;
	public static native Config newDefaultConfig() throws Exception;
	public static native Node newNode(Repo r) throws Exception;
	public static native Shell newShell(String url);
	public static native SockManager newSockManager(String path) throws Exception;
	public static native Shell newTCPShell(String port);
	/**
	 * New unix socket domain shell
	 */
	public static native Shell newUDSShell(String sockpath);
	public static native Repo openRepo(String path) throws Exception;
	public static native boolean repoIsInitialized(String path);
	public static native void setDNSPair(String primary, String secondary, boolean loadFromSystem);
}

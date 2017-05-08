package com.cn.proxy.server.ana.common.error;

/**
 * Interface to all the different concrete states,
 * interface methods include "Connect", "Disconnect", "Synchronize", and "Abort".
 * 
 * User: vzhou
 * Date: Jan 13, 2005
 * Time: 5:04:00 PM
 */
public interface NetworkState {
    // state constants
    static final int STATE_DISCONNECTED             = 0;
    static final int STATE_CONNECTING               = 1;
    static final int STATE_SECURE_CONNECTING        = 2;
    static final int STATE_CONNECTED                = 3;
    static final int STATE_SYNCHRONIZING            = 4;
    static final int STATE_SYNCHRONIZED             = 5;
    static final int STATE_DYNAMIC_SYNCHRONIZING    = 6;
}

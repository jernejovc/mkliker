package com.jernejovc.mkliker.net;

/**
 * Interface used when we have a class that is waiting for a websocket connection.
 * @author matej
 *
 */
public interface WaitForConnection {
	public void connectionEstablished();
}

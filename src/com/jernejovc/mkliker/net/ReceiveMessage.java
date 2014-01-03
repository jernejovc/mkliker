package com.jernejovc.mkliker.net;

import com.jernejovc.mkliker.message.Message;

/**
 * Interface used for relaying messages.
 * @author matej
 *
 */
public interface ReceiveMessage {
	/**
	 * Actual message handler to be implemented.
	 * @param msg Message to be handled
	 */
	public abstract void receiveMessage(Message msg);
}

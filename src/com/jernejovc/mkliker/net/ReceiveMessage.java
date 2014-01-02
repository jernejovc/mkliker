package com.jernejovc.mkliker.net;

import com.jernejovc.mkliker.message.Message;

public interface ReceiveMessage {
	public abstract void receiveMessage(Message msg);
}

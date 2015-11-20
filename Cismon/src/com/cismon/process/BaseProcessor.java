package com.cismon.process;

import com.cismon.listener.CMDListener;
import com.cismon.tts.Speaker;



public interface BaseProcessor {
	public void initialize();
	public void process();
	public void destory();
	public Speaker getSpeaker();
	public CMDListener getListener();
}

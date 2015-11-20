package com.cismon.tts;

public interface Speaker {
	public boolean init();
	public boolean speak(String text);
	public boolean destroy();
}

package com.cismon.plugins;

public interface PluginEgg {
	public String getEggKey();
	public boolean destoryEgg();
	public boolean initialize();
	public boolean accept(String text);
	public boolean processCMD(String cmd);
}

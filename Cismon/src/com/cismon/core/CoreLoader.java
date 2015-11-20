package com.cismon.core;

import com.cismon.listener.CMDListener;
import com.cismon.plugins.PluginManager;
import com.cismon.process.BaseProcessor;
import com.cismon.process.SingleThreadProcessor;
import com.cismon.tts.Speaker;
import com.cismon.util.PropertiesManager;

public class CoreLoader {
	protected static PropertiesManager pm = PropertiesManager.instance();
	protected static PluginManager plum = PluginManager.instance();
	protected static BaseProcessor bp = null;
	public static void loadAll()
	{
		pm.initialize();
		plum.initialize();
		if("single_thread".equals(pm.getProcessorMode()))
		{
			bp = new SingleThreadProcessor();
		}
		bp.initialize();
		bp.process();
	}
	
	public static BaseProcessor getCurrentBaseProcessor()
	{
		return bp;
	}
	
	public static Speaker getMainSpeaker()
	{
		return bp.getSpeaker();
	}
	
	public static CMDListener getListener()
	{
		return bp.getListener();
	}
	public static void main(String[] args)
	{
		PropertiesManager.instance().setDevMode(true);
		CoreLoader.loadAll();
	}
}

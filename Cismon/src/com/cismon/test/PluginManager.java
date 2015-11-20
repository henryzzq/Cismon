package com.cismon.test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {
	private static ClassLoader cl;
	private static Map<String, PluginEgg> plugins = 
			new HashMap<String, PluginEgg>();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		PluginEgg pe = loadPlugin(args[0]);
		plugins.put(pe.getEggKey(), pe);
		for(PluginEgg peItem : plugins.values())
		{
			peItem.test();
		}
	}
	
	public static PluginEgg loadPlugin(String filename) throws Exception
	{
		URL[] externalURLs = new URL[]{new URL(filename)};
		cl = new URLClassLoader(externalURLs);
		Class a = cl.loadClass("com.cismon.plugin.ppm.PluginMain");
		if(a == null)
		{
			System.out.println("adfasdf");
		}
		return (PluginEgg) a.newInstance();
	}
}

package com.cismon.plugins;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cismon.plugins.PluginEgg;
import com.cismon.util.PropertiesManager;
import com.cismon.util.PropertiesUtil;

public class PluginManager {
	public static final String PLUGIN_MAIN = "PLUGIN_MAIN";
	public static final String LIB_MAIN = "LIB_MAIN";
	public static final String PLUGIN_TYPE = "PLUGIN_TYPE";
	private static PropertiesManager _pm = PropertiesManager.instance();
	private static PluginManager _instance = null;
	private static Object _singletonObj = new Object();
	private static Map<String, PluginEgg> plugins = 
			new ConcurrentHashMap<String, PluginEgg>();
	
	private static Map<String, Properties> pluginPros = 
						new ConcurrentHashMap<String, Properties>();
	/** ThreadPool **/
    private static final ExecutorService INVENTORY_FILE_MONITOR_THREAD = 
    		Executors.newFixedThreadPool(1);
	private Map<String, Long> fileTimeMap = new HashMap<String, Long>();
	
	public static PluginManager instance()
	{
		if(_instance == null)
		{
			synchronized(_singletonObj)
			{
				if(_instance == null)
				{
					_instance = new PluginManager();
				}
			}
		}
		return _instance;
	}
	
	
	public void initialize()
	{
		INVENTORY_FILE_MONITOR_THREAD.submit(new Runnable()
        {
			@Override
			public void run() {
				while(true)
				{
					loadPluginsAsNeeded();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
        });
	}
	
	private void loadPluginsAsNeeded()
	{
		String pluginDir = _pm.getPluginsDir();
		File dir = new File(pluginDir);
		String[] files = dir.list(); 
		if(files == null)
		{
			System.out.println(this.getClass().getName()
						       + ": No files to mornitor.");
			return;
		}
		Arrays.sort(files);
		
		String abPath = null;
		for(String fileName : files)
		{
			File file = new File(dir, fileName);
			abPath = file.getAbsolutePath();
			Long lstUpdateTime = fileTimeMap.get(abPath);
			if(lstUpdateTime == null)
			{
				lstUpdateTime = new Long(0);
			}
			if(file.isFile() 
		       && file.getName().endsWith(".properties"))
			{
				if(file.lastModified() > lstUpdateTime)
				{
					String eggKey = null;
					try {
						PluginEgg egg = loadPlugin(file.getAbsolutePath());
						if(egg != null)
						{
							eggKey = egg.getEggKey();
							System.out.println("PluginManager: " 
											   + eggKey + " is found");
							PluginEgg preEgg = plugins.put(eggKey, egg);
							if(preEgg != null)
							{
								preEgg.destoryEgg();
							}
							egg.initialize();
							fileTimeMap.put(abPath, file.lastModified());
							System.out.println("PluginManager: " 
											   + eggKey 
											   + " succussfully loaded");
						}
					} catch (Exception e) {
						e.printStackTrace();
						PluginEgg egg = plugins.remove(eggKey);
						if(egg != null)
						{
							egg.destoryEgg();
						}
						fileTimeMap.remove(abPath);
						continue;
					}
				}
			}
		}
	}
	
	public static PluginEgg loadPlugin(String path) throws Exception
	{
		Properties properties = 
			    PropertiesUtil.loadProperties(path);
		if(properties != null)
		{
			String pluginType = properties.getProperty(PLUGIN_TYPE);
			if(pluginType != null && pluginType.equalsIgnoreCase("COMMAND"))
			{
				return loadCMDPlugin(path, properties);
			}
			else
			{
				System.out.println("Plugin type not found : " + pluginType);
			}
		}
		return null;
	}
	
	public static PluginEgg loadCMDPlugin(String path, 
										  Properties properties) 
												  throws Exception
	{
		String libs = properties.getProperty(LIB_MAIN);
		String pluginMain = properties.getProperty(PLUGIN_MAIN);
		if(libs == null || pluginMain == null)
		{
			System.out.println("missing properties in file: " + path);
			return null;
		}
		String[] libraries = libs.split(",");
		URL[] urlArray = new URL[libraries.length];
		for(int i=0;i<libraries.length;i++)
		{
			urlArray[i] = new URL(libraries[i]);
		}
	    ClassLoader cl = new URLClassLoader(urlArray);
	    Class pluginClass = cl.loadClass(pluginMain);
	    if(pluginClass == null)
	    {
	    	System.out.println(pluginMain + " not found");
	    }
	    else
	    {
	    	PluginEgg egg = (PluginEgg) pluginClass.newInstance();
	    	if(egg != null)
	    	{
	    		pluginPros.put(egg.getEggKey(), properties);
	    	}
	    	return egg;
	    }
	    return null;
	}

	public static Map<String, PluginEgg> getPlugins() {
		return plugins;
	}

	public static Map<String, Properties> getPluginPros() {
		return pluginPros;
	}
}

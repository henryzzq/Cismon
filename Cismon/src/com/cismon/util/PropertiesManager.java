package com.cismon.util;

import java.io.File;
import java.util.Properties;

public class PropertiesManager {
	public static final String SYSTEM_PRO = "System.properties";
	private static final String VOICE_KEY = "TTS_VOICE";
	private static final String SPX_RECOGNIZER_KEY = "SPX_RECOGNIZER";
	private static final String SPX_CONFIG_FILE_KEY = "SPX_CONFIG_FILE";
	private static final String SPEAKER_TYPE_KEY = "SPEAKER_TYPE";
	private static final String SPEAKER_LAN_KEY = "SPEAKER_LAN";
	private static final String LISTENER_TYPE_KEY = "LISTENER_TYPE";
	private static final String PROCESSOR_MODE_KEY = "PROCESSOR_MODE";
	private static PropertiesManager _instance = null;
	private static Object _singletonObj = new Object();
	private Properties system = null;
	
	private boolean devMode = false;
	
	private PropertiesManager()
	{
		
	}
	
	public static PropertiesManager instance()
	{
		if(_instance == null)
		{
			synchronized(_singletonObj)
			{
				if(_instance == null)
				{
					_instance = new PropertiesManager();
				}
			}
		}
		return _instance;
	}
	
	
	public void initialize()
	{
		system =  PropertiesUtil.loadProperties(getPropertiesDir() 
												+ SYSTEM_PRO);
	}
	
	public String getPropertiesDir()
	{
		String devDir = "";
		if(isDevMode())
		{
			devDir = "src/";
		}
		return new File(devDir + "properties").getAbsolutePath() + "\\";
	}
	
	public String getPluginsDir()
	{
		String devDir = "";
		if(isDevMode())
		{
			devDir = "src/";
		}
		return new File(devDir + "plugins").getAbsolutePath() + "\\";
	}
	
	public String getVoiceConfigDir()
	{
		String devDir = "";
		if(isDevMode())
		{
			devDir = "src/";
		}
		return new File(devDir + "voice-config").getAbsolutePath() + "\\";
	}
	
	public String getAudioSaveDir()
	{
		String devDir = "";
		if(isDevMode())
		{
			devDir = "src/";
		}
		return new File(devDir + "audio").getAbsolutePath() + "\\";
	}
	
	public String getVoiceOpt()
	{
		return (String) system.get(VOICE_KEY);
	}

	public String getRecognizerOpt()
	{
		return (String) system.get(SPX_RECOGNIZER_KEY);
	}
	
	public String getSPXConfigPath()
	{
		return (String) system.getProperty(SPX_CONFIG_FILE_KEY);
	}
	
	public String getSpeakerType()
	{
		return (String) system.getProperty(SPEAKER_TYPE_KEY);
	}
	
	public String getSpeakerLan()
	{
		return (String) system.getProperty(SPEAKER_LAN_KEY);
	}
	
	public String getListenerType()
	{
		return (String) system.getProperty(LISTENER_TYPE_KEY); 
	}
	
	public String getProcessorMode()
	{
		return (String) system.getProperty(PROCESSOR_MODE_KEY); 
	}
	
	public boolean isDevMode() {
		return devMode;
	}

	public void setDevMode(boolean devMode) {
		this.devMode = devMode;
	}
}

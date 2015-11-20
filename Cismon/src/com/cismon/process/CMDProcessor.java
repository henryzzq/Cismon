package com.cismon.process;

import java.util.ArrayList;
import java.util.List;

import com.cismon.listener.CMDListener;
import com.cismon.listener.SphinxListener;
import com.cismon.plugins.PluginEgg;
import com.cismon.plugins.PluginManager;
import com.cismon.tts.GoogleSpeaker;
import com.cismon.tts.Speaker;
import com.cismon.tts.TTSSpeaker;
import com.cismon.util.PropertiesManager;

public abstract class CMDProcessor implements BaseProcessor{
	protected static PropertiesManager _pm = PropertiesManager.instance();
	protected static PluginManager _plum = PluginManager.instance();
	protected static Speaker _sp = null;
	protected static CMDListener _lis = null;
	
	public void initialize()
	{
		if(_pm.getSpeakerType().equalsIgnoreCase("tts"))
		{
			_sp = new TTSSpeaker();
		}
		else if(_pm.getSpeakerType().equalsIgnoreCase("google"))
		{
			_sp = new GoogleSpeaker();
		}
		if(_pm.getListenerType().equalsIgnoreCase("spx4"))
		{
			_lis = new SphinxListener();
		}
		_sp.init();
		_lis.init();
	}
	
	public void destory()
	{
		_sp.destroy();
		_lis.destroy();
	}
	
	public List<PluginEgg> accept(String cmd) {
		List<PluginEgg> list = new ArrayList<PluginEgg>();
		for(PluginEgg pe :_plum.getPlugins().values())
		{
			if(pe.accept(cmd))
			{
				list.add(pe);
				break;
			}
		}
		return list;
	}
	
	@Override
	public Speaker getSpeaker() {
		return _sp;
	}

	@Override
	public CMDListener getListener() {
		return _lis;
	}
	abstract public void process();
}

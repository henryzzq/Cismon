package com.cismon.process;

import java.util.List;

import com.cismon.plugins.PluginEgg;

public class SingleThreadProcessor extends CMDProcessor{

	@Override
	public void process() {
		_sp.speak("Hello Henry, My name is Cismon.");
		_sp.speak("Pleasure at your service.");
		boolean doing = true;
        while(doing)
        {
        	System.out.println("wait for your service...(say Cismon)");  
        	/* Synthesize speech. 
        	 */  
        	if("sis mon".equalsIgnoreCase(_lis.getMessage()))
        	{
        		_sp.speak("yes sir?");  
        		System.out.println("say your command...(such as ppm status?)");  
        	}
        	else
        	{
        		continue;
        	}
        	long currentTime = System.currentTimeMillis();
        	
        	while(System.currentTimeMillis() - currentTime <15000)
        	{
        		String cmd = _lis.getMessage();
        		List<PluginEgg> eggs = accept(cmd);
        		if(eggs.size() > 0)
        		{
        			eggs.get(0).processCMD(cmd);
        			break;
        		}
        		else
        		{
        			System.out.println("command not found");
        		}
        	}
        }
	}
}

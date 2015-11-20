package com.cismon.tts;

import com.cismon.core.CoreLoader;
import com.cismon.util.PropertiesManager;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTSSpeaker implements Speaker{
	private static PropertiesManager _pm = PropertiesManager.instance();
	private static VoiceManager voiceManager = null;
	private static Voice speaker = null;
	@Override
	public boolean init() {
		voiceManager = VoiceManager.getInstance();  
		speaker = voiceManager.getVoice(_pm.getVoiceOpt());
		if (speaker == null) {  
            System.err.println(  
                "Cannot find a voice named "  
                + _pm.getVoiceOpt() + ".  Please specify a different voice.");  
            System.exit(1);  
        }  
		speaker.allocate();  
		return true;
	}

	@Override
	public boolean speak(String text) {
		synchronized(this){
			System.out.println("Cismon: " + text);
			speaker.speak(text); 
		}
		return true;
	}

	@Override
	public boolean destroy() {
		speaker.deallocate();
		return true;
	}
	public static void main(String[] args)
	{
		PropertiesManager.instance().setDevMode(true);
		CoreLoader.loadAll();
		Speaker sp = new TTSSpeaker();
		sp.init();
		sp.speak("Thans for using the beta version of cismon. My name is skynet :)");
		sp.destroy();
	}
}

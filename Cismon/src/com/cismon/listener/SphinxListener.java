package com.cismon.listener;

import java.net.MalformedURLException;
import java.net.URL;

import com.cismon.core.CoreLoader;
import com.cismon.util.PropertiesManager;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

public class SphinxListener implements CMDListener{
	private static PropertiesManager _pm = PropertiesManager.instance();
	private static Recognizer recognizer = null;
	@Override
	public boolean init(){
		 ConfigurationManager cm = null;
		try {
			cm = new ConfigurationManager(new URL(_pm.getSPXConfigPath()));
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	 recognizer = (Recognizer) cm.lookup(_pm.getRecognizerOpt());
         recognizer.allocate();

         // start the microphone or exit if the programm if this is not possible
         Microphone microphone = (Microphone) cm.lookup("microphone");
         if (!microphone.startRecording()) {
             System.out.println("Cannot start microphone.");
             recognizer.deallocate();
             System.exit(1);
         }
		return true;
	}

	@Override
	public String getMessage() {
		synchronized(this)
		{
			// loop the recognition until the programm exits.
			while (true) {
				System.out.println("Start speaking. Press Ctrl-C to quit.\n");

				Result result = recognizer.recognize();

				if (result != null) {
					String resultText = result.getBestFinalResultNoFiller();
					if((resultText != null) && !"".equals(resultText))
					{
						System.out.println("Catch message : " + resultText);
               	 		return resultText;
					}
					System.out.println("I can't hear what you said.\n");
				}
			}
		}
	}

	@Override
	public String messageFilter(String msg) {
		return msg;
	}

	@Override
	public boolean destroy() {
		recognizer.deallocate();
		return true;
	}

	public static void main(String[] args)
	{
		_pm.setDevMode(true);
		CoreLoader.loadAll();
		SphinxListener sl = new SphinxListener();
		sl.init();
		sl.getMessage();
		sl.destroy();
	}
}

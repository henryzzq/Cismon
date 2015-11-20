package com.cismon.test;


import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

public class VoiceCommand {
	public static String response = "Yes sir?";
	public static String voiceToSay = "My name is skynet. Hello Henry and thanks for bring me life.";
    public static Recognizer recognizer = null;
    public static String command = "who are you";
	/** 
     * Example of how to list all the known voices. 
     */  
    public static void listAllVoices() {  
        System.out.println();  
        System.out.println("All voices available:");  
        VoiceManager voiceManager = VoiceManager.getInstance();  
        Voice[] voices = voiceManager.getVoices();  
        for (int i = 0; i < voices.length; i++) {  
            System.out.println("    " + voices[i].getName()  
                               + " (" + voices[i].getDomain() + " domain)");  
        }  
    }  
  
    public static void main(String[] args) {  
    	init();
        //listAllVoices();  
        
        String voiceName = (args.length > 0)  
            ? args[0]  
        : "kevin16";  
  
        //System.out.println("./mbrola");  
        //System.out.println("Using voice: " + voiceName);  
  
        /* The VoiceManager manages all the voices for FreeTTS. 
         */  
        VoiceManager voiceManager = VoiceManager.getInstance();  
        //System.out.println(voiceManager.getVoiceInfo());
        Voice helloVoice = voiceManager.getVoice(voiceName);  
  
        if (helloVoice == null) {  
            System.err.println(  
                "Cannot find a voice named "  
                + voiceName + ".  Please specify a different voice.");  
            System.exit(1);  
        }  
        
        /* Allocates the resources for the voice. 
         */  
        helloVoice.allocate();  
        boolean doing = true;
        while(doing)
        {
        	System.out.println("wait for your service...(say skynet)");  
        	/* Synthesize speech. 
        	 */  
        	if("sky net".equalsIgnoreCase(getMessage()))
        	{
        		System.out.println(response);
        		helloVoice.speak(response);  
        		System.out.println("say your command...(such as who are you?)");  
        	}
        	else
        	{
        		continue;
        	}
        	long currentTime = System.currentTimeMillis();
        	
        	while(System.currentTimeMillis() - currentTime <15000)
        	{
        		if(command.equalsIgnoreCase(getMessage()))
        		{
        			System.out.println(voiceToSay + ":)");
        			helloVoice.speak(voiceToSay);
        			doing = false;
        			break;
        		}
        	}
        }
  
        /* Clean up and leave. 
         */  
        helloVoice.deallocate();  
        System.exit(0);  
    }  
    
    public static void init()
    {
    	 ConfigurationManager cm = new ConfigurationManager(VoiceCommand.class.getResource("/com/cismon/test/helloworld.config.xml"));
    	 recognizer = (Recognizer) cm.lookup("recognizer");
         recognizer.allocate();

         // start the microphone or exit if the programm if this is not possible
         Microphone microphone = (Microphone) cm.lookup("microphone");
         if (!microphone.startRecording()) {
             System.out.println("Cannot start microphone.");
             recognizer.deallocate();
             System.exit(1);
         }
    }

    public static String getMessage()
    {
    	// loop the recognition until the programm exits.
        while (true) {
            System.out.println("Start speaking. Press Ctrl-C to quit.\n");

            Result result = recognizer.recognize();

            if (result != null) {
                String resultText = result.getBestFinalResultNoFiller();
                System.out.println(resultText);
                if((resultText != null))
                {
               	 	return resultText;
                }
            } else {
                System.out.println("I can't hear what you said.\n");
            }
        }
    }
}

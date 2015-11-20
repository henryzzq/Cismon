package com.cismon.tts;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javazoom.jl.player.Player;

import com.cismon.util.PropertiesManager;

public class GoogleSpeaker implements Speaker{
	private static final String TEXT_TO_SPEECH_SERVICE = 
            "http://translate.google.com/translate_tts";
    private static final String USER_AGENT =  
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) " +
            "Gecko/20100101 Firefox/11.0";
    private static final String SAVE_FILE_NAME = "google.mp3";
    private static PropertiesManager _pm = PropertiesManager.instance();
    private Language language = null;
	@Override
	public boolean init() {
		language = Language.valueOf(_pm.getSpeakerLan().toUpperCase());
		if(language == null)
		{
			System.err.println(  
	                "Cannot find a language named "  
	                + _pm.getSpeakerLan() + ".  Please specify a different language.");  
	            System.exit(1);  
		}
		return true;
	}

	@Override
	public boolean speak(String text) {
		synchronized(this)
		{
			System.out.println("Cismon: " + text);
			BufferedInputStream bufIn = null;
			BufferedOutputStream out = null;
			BufferedInputStream bis = null;
			FileInputStream fis = null;
			try{
				text = URLEncoder.encode(text, "utf-8");
				String strUrl = TEXT_TO_SPEECH_SERVICE + "?" + 
						"tl=" + language + "&q=" + text;
				URL url = new URL(strUrl);
				String savePath = _pm.getAudioSaveDir() + SAVE_FILE_NAME;
	        
				// Etablish connection
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				// Get method
				connection.setRequestMethod("GET");
				// Set User-Agent to "mimic" the behavior of a web browser. In this 
				// example, I used my browser's info
				connection.addRequestProperty("User-Agent", USER_AGENT);
				connection.connect();
	
				// Get content
				bufIn = new BufferedInputStream(connection.getInputStream());
				byte[] buffer = new byte[1024];
				int n;
				ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
				while ((n = bufIn.read(buffer)) > 0) {
					bufOut.write(buffer, 0, n);
				}
	        
				File output = new File(savePath);
				out =   new BufferedOutputStream(new FileOutputStream(output));
				out.write(bufOut.toByteArray());
				out.flush();
	        	
	        	fis = new FileInputStream(savePath);
	            bis = new BufferedInputStream(fis);
	            Player player = new Player(bis);
	            player.play();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			finally
			{
				try {
					bufIn.close();
					out.close();
					bis.close();
					fis.close();
				} catch (IOException e) {
					
					}
			}
		}
		return true;
	}

	@Override
	public boolean destroy() {
		return false;
	}
	
	public enum Language {
        FR("french"),
        EN("english");

        private final String language;
        private Language(String language) {
            this.language = language;
        }

        public String getFullName() {
            return language;
        }
    }
}

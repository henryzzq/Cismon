package com.cismon.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

public class PropertiesUtil {
    /**
     * Load properties according to file path
     * 
     * @param filePath
     * @return
     */
    public static Properties loadProperties(String filePath)
    {
        FileReader fr = null;
        BufferedReader br = null;
        Properties properties = new Properties();
        
        try
        {
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);
            
            properties.load(br);
            
            return properties;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (Exception e)
                {
                    // ignore it
                }
                
            }
            if (fr != null)
            {
                try
                {
                    fr.close();
                }
                catch (Exception e)
                {
                    // ignore it
                }
            }
        }
    }
    
    /**
     * Save properties
     * 
     * @param filePath
     * @param properties
     * @param comments
     */
    public static void saveProperties(String filePath, 
    						   Properties properties, 
    						   String comments)
    {
    	createPropertiesFile(filePath);
        
        FileWriter fw = null;
        BufferedWriter bw = null;
        
        try
        {

            fw = new FileWriter(filePath);
            bw = new BufferedWriter(fw);

            properties.store(bw, comments);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bw != null)
            {
                try
                {
                    bw.close();
                }
                catch (Exception e)
                {
                    // ignore it
                }
                
            }
            if (fw != null)
            {
                try
                {
                    fw.close();
                }
                catch (Exception e)
                {
                    // ignore it
                }
            }
            
        }

    }
    
    /**
     * Create properties file if it doesn't exist
     * 
     * @param filePaht
     * @return
     */
    public static boolean createPropertiesFile(String filePath)
    {
        File newFile = new File(filePath);
        
        if (!newFile.exists())
        {
            try
            {
                newFile.createNewFile();
            } 
            catch (Exception e)
            {
                e.printStackTrace(); // STACK_TRACE_OK
            }
        }
        
        return (newFile.exists());
        
    }
}

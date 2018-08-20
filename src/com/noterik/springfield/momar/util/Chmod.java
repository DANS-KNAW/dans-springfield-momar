
package com.noterik.springfield.momar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Chmod {


	/**
	 * Changes the file/dir permissions/rights
	 * 
	 * @param path	file/dir whose permission/rights you wanna change
	 * @param octa	any kind of rights (for example: 764)
	 * 
	 * @return	RetVal of the chmod execution
	 *  
	 * 			-1: this methods default return value indicating something went wrong
	 * 			 0: chmod execution was successful
	 */
	public static final Integer chmod(Integer octa, String path) {
		Integer retVal = -1;
		Process proc = null;
		try {
			System.out.println("MOMAR: Command to execute: chmod " + octa + " " + path);
			proc = Runtime.getRuntime().exec("chmod " + octa + " " + path);
			
	        BufferedReader br = null; 
	        String currentInput = null;
            try {
            	br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	            while ( (currentInput = br.readLine()) != null) {
	                System.out.println(currentInput);
	            }
            }
            catch (Exception e) {
            	System.out.println("MOMAR: Error during reading inputStream");
            }
            finally { 
            	if (br != null)
	            	try {
	            			br.close(); 
	            		}
	            	catch (Exception e) {
	            		//silently
	            	}
            }
	        
			retVal = proc.waitFor();
		} catch (IOException e) {
			// thrown by process getRuntime()
			e.printStackTrace();
		} catch (InterruptedException e) {
			// thrown by proc.waitFor()
			e.printStackTrace();
		}
		finally {
			if (proc != null)
				try {
					proc.getErrorStream().close();
					proc.getInputStream().close();
					proc.getOutputStream().close();
				}
				catch (Exception e) {
					//silently closing
				}
				
		}
		return retVal;
	}
	

	
}
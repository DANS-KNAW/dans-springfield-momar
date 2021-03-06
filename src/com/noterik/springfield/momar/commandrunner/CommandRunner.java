package com.noterik.springfield.momar.commandrunner;

import org.apache.log4j.Logger;
import org.restlet.engine.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class CommandRunner {
	private static Logger LOG = Logger.getLogger(CommandRunner.class);

	/**
	 * Runs command.
	 * 
	 * @param cmd
	 */
	public static String run(String[] cmd) {
		final StringBuffer result = new StringBuffer();

		try {
			// Execute command
			final Process child = Runtime.getRuntime().exec(cmd);
			
			// Handle stdout...
			new Thread() {
			    public void run() {
			    	BufferedInputStream bis = null;
			    	try {		
						// Get the input stream and read from it
						InputStream is = child.getInputStream();

						if (is != null) {
							bis = new BufferedInputStream(is);
							int c;
							
							while ((c = bis.read()) != -1) {					
								result.append((char) c);
							}
						}
					} catch (IOException e) {
			    		LOG.error("IOException when reading STDOUT of command", e);
						e.printStackTrace();
					} finally {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			    }
			}.start();

			// Handle stderr...
			new Thread() {
			    public void run() {
			    	BufferedInputStream bis = null;
			    	try {		
						// Get the error stream and read from it
						InputStream is = child.getErrorStream();

						if (is != null) {
							bis = new BufferedInputStream(is);
							int c;
							while ((c = bis.read()) != -1) {					
								result.append((char) c);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bis.close();
						} catch (IOException e) {
							LOG.error("IOException when reading STDERR of command", e);
							e.printStackTrace();
						}
					}
			    }
			}.start();
			
			child.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}

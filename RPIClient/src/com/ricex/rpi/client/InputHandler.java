package com.ricex.rpi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Thread for handling the user input for the client
 * 
 * @author Mitchell
 *
 */

public class InputHandler implements Runnable {

	/** The rpi client */
	private RPIClient client;
	
	/** Indicates whether or not the input handler should continue running */
	private boolean running;
	
	/** Creates a new instance of InputHandler
	 * 
	 */
	
	public InputHandler() {
		client = new RPIClient();
	}
	
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Welcome to RPIClient!");
		System.out.println("Usage: c - connect to server \n \t d - disconnect from server \n \t q - quit");
		
		while (running) {
			try {
				if (System.in.available() > 0) {
					String line = reader.readLine();
			
					if (line == null) {
						running = false;
					}
					else if (line.toLowerCase().startsWith("c")) {
						client.connectToServer();
					}
					else if (line.toLowerCase().startsWith("d")) {
						client.disconnectFromServer();
					}
					else if (line.toLowerCase().startsWith("q")) {
						client.disconnectFromServer();
						running = false;
					}
				}
				else {
					Thread.sleep(500);
				}
			}
			catch (IOException e) {
				System.out.println("Error reading input");
				e.printStackTrace();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("We are exiting the client..");
	}
}
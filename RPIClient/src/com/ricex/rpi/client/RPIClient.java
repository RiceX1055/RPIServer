package com.ricex.rpi.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ricex.rpi.common.message.DirectoryListingMessage;
import com.ricex.rpi.common.video.Video;

/**
 * RPI client that connects to the server
 * 
 * @author Mitchell
 * 
 */

public class RPIClient {

	/** The socket this will use to conenct to the server */
	private Socket socket;
	
	/** The server handle for this client */
	private ServerHandler serverHandler;
	
	/** The thread that the server handler will run in */
	private Thread serverHandlerThread;
	
	/** The ip address of the server */
	private String serverIp;
	
	/** Port of the server to connect to */
	private int serverPort;
	
	/** The directory listing of the videos this client has to play */
	private Video rootDirectory;

	public RPIClient(Video rootDirectory) {
		this.rootDirectory = rootDirectory;
		serverIp = RPIClientProperties.getInstance().getServerIp();
		serverPort = RPIClientProperties.getInstance().getRPIPort();

	}
	
	/** Sends the updated root directory to the server
	 * 
	 * @param rootDirectory The new root directory
	 */
	
	public void updateRootDirectory(Video rootDirectory) {
		this.rootDirectory = rootDirectory;
		serverHandler.sendMessage(new DirectoryListingMessage(rootDirectory));
	}
	
	/** Connects to the server
	 * 
	 * @throws UnknownHostException If the host cannot be found
	 * @throws IOException
	 */
	
	public void connectToServer() throws UnknownHostException, IOException {
		socket = new Socket(serverIp, serverPort);
		serverHandler = new ServerHandler(this,socket);	
		
		//create and start the server handler thread
		serverHandlerThread = new Thread(serverHandler);
		serverHandlerThread.start();
		
		serverHandler.sendMessage(new DirectoryListingMessage(rootDirectory));
	}
	
	/** Disconnects from the server, waits for the server thread to finish, and closes the socket connections
	 */
	
	public void disconnectFromServer() {
		if (serverHandler != null) {		
			System.out.println("Disconnecting from server");
			serverHandler.disconnect();
			System.out.println("Interipting server handler");
			serverHandlerThread.interrupt();
			try {
				System.out.println("Waiting to join on server handler");
				serverHandlerThread.join();
			}
			catch (InterruptedException e) {
				System.out.println("Error waiting on server handler thread");
				e.printStackTrace();
			}
			serverHandlerThread = null;
			
			try {
				socket.close();
			}
			catch (IOException e) {
				System.out.println("Error closing the server socket");
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * @return Whether the client is connected to the server or not
	 */
	
	public boolean isConnected() {
		return serverHandler != null && serverHandler.isConnected();
	}
	
	/** Returns the IP address and port that the server is connected to
	 * 
	 * @return serverIp:serverPort
	 */
	
	public String getServerInfo() {
		return serverIp + ":" + serverPort;
	}
	
	public static void main(String[] args) {
		InputHandler handler = new InputHandler();
		handler.run();
	}	
	
}

package com.ricex.rpi.server.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.ricex.rpi.common.RPIStatus;
import com.ricex.rpi.common.video.Video;
import com.ricex.rpi.server.Server;
import com.ricex.rpi.server.client.handler.RPIClientHandler;

/** A client that is connected to the server
 * 
 * @author Mitchell
 *
 */

public class RPIClient extends Client {
	
	/** The name of this client */
	private String name;
	
	/** The status of this client */
	private RPIStatus status;
	
	/** The root directory of this client */
	private Video rootDirectory;
	
	/** The list of change listeners registered for this client */
	private List<ClientChangeListener<RPIClient>> changeListeners;
	
	public RPIClient (Server<RPIClient> server, long id, Socket socket) {
		super(server, id, socket);
		
		name = "Unnamed Client " + id;		
		status = new RPIStatus(RPIStatus.IDLE);		
		changeListeners = new ArrayList<ClientChangeListener<RPIClient>>();		
	}
	
	/** Closes the clients connection, and cleans up resources */
	
	public void close() {
		try {
			super.close();
			handler.close();
		}
		catch (IOException e) {		
		}
	}
	
	/** Returns the status of this client */
	
	public RPIStatus getStatus() {
		return status;
	}
	
	/** Sets the status of this client to the given status */
	
	public void setStatus(RPIStatus status) {
		this.status = status;
		ClientChangeEvent<RPIClient> changeEvent = new ClientChangeEvent<RPIClient>(this, ClientChangeEvent.EVENT_STATUS_CHANGE);
		notifyChangeListeners(changeEvent); //notify listeners that the status has been changed
	}
	
	/** Returns the name of this client */
	
	public String getName() {
		return name;
	}
	
	/** Sets the name of the client to the given string */
	
	public void setName(String name) {
		this.name = name;
		ClientChangeEvent<RPIClient> changeEvent = new ClientChangeEvent<RPIClient>(this, ClientChangeEvent.EVENT_NAME_CHANGE);
		notifyChangeListeners(changeEvent); //notify listeners that the status has been changed
	}
	
	/** To string method of RPI CLient, returns the clients name
	 * 
	 */
	
	public String toString() {
		return getName();
	}	
	
	/**
	 * @return the rootDirectory
	 */
	public Video getRootDirectory() {
		return rootDirectory;
	}
	
	/**
	 * @param rootDirectory the rootDirectory to set
	 */
	public void setRootDirectory(Video rootDirectory) {
		this.rootDirectory = rootDirectory;
		ClientChangeEvent<RPIClient> changeEvent = new ClientChangeEvent<RPIClient>(this, ClientChangeEvent.EVENT_ROOT_DIRECTORY_CHANGE);
		notifyChangeListeners(changeEvent); //notify listeners that the status has been changed
	}

	/** Sets the connected value of this client */
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}		
	
	/** Adds the given change listener */
	public void addChangeListener(ClientChangeListener<RPIClient> listener) {
		changeListeners.add(listener);
	}
	
	/** Removes the given change listener */	
	public void removeChangeListener(ClientChangeListener<RPIClient> listener) {
		changeListeners.remove(listener);
	}	

	protected void notifyChangeListeners(ClientChangeEvent<RPIClient> changeEvent) {
		for (ClientChangeListener<RPIClient> listener : changeListeners) {
			listener.clientChanged(changeEvent);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected RPIClientHandler createClientHandler() {
		return new RPIClientHandler(this);
	}
}

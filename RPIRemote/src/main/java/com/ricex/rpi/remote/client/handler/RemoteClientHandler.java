package com.ricex.rpi.remote.client.handler;

import com.ricex.rpi.common.message.IMessage;
import com.ricex.rpi.common.message.MovieMessage;
import com.ricex.rpi.common.message.MovieMessage.Command;
import com.ricex.rpi.common.message.QuitMessage;
import com.ricex.rpi.common.message.request.RemoteMovieMessage;
import com.ricex.rpi.remote.RPIServer;
import com.ricex.rpi.remote.client.RPIClient;
import com.ricex.rpi.remote.client.RemoteClient;


public class RemoteClientHandler extends ClientHandler<RemoteClient> {
	
	/** Creates a new Remote Client handler */
	
	public RemoteClientHandler(RemoteClient client) {
		super(client);
	}
	
	/** 
	 * {@inheritDoc}
	 */

	protected void processMessage(IMessage message) {
		if (message instanceof RemoteMovieMessage) {
			RemoteMovieMessage rmsg = (RemoteMovieMessage) message;
			RPIClient client = RPIServer.getInstance().getClient(rmsg.getClientId());
			if (rmsg.getCommand() == Command.PLAY) {
				//lets play the given playlist
				client.getPlayerModule().play(rmsg.getPlaylist());
			}
			else {
				//if it is not play message, create a new movie message and sent it to the client
				MovieMessage msg = new MovieMessage(rmsg.getCommand());
				client.sendMessage(msg);
			}
		}
		else if (message instanceof QuitMessage) {
			client.setConnected(false); // client is disconnecting
		}
	}

}

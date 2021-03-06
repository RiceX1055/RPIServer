package com.ricex.rpi.common.message;

/** Message using for Acknowledging the receiving of messages
 * 
 * @author Mitchell Caisse
 *
 */

public class AcknowledgeMessage implements IMessage {

	/** The message to acknowledge */
	private IMessage ackMessage;
	
	/** Create a new instance of Acknowledge message,
	 * 
	 * @param ackMessage The message to acknowlege we received it
	 */
	
	public AcknowledgeMessage(IMessage ackMessage) {
		this.ackMessage = ackMessage;
	}
	
}

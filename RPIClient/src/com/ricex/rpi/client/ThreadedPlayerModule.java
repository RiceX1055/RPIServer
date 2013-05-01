package com.ricex.rpi.client;

import com.ricex.rpi.common.PlayerModule;

/**
 *  The player module for RPIClient
 *  This will run on the Raspberry Pi itself, and is the controller around the omx player
 *  It interacts with the omx process, and can issue commands to the omxplayer process by using
 *  the output stream on the process.
 *  
 *  Only one video is allowed to be played at a time.
 * 
 * @author Mitchell
 * 
 */

public class ThreadedPlayerModule implements PlayerModule {

	/** The singleton instance of this class */
	private static ThreadedPlayerModule _instance;
	
	/** The player that this class uses */
	private Player player;

	/** Gets the singleton instance of this class */
	public static ThreadedPlayerModule getInstance() {
		if (_instance == null) {
			_instance = new ThreadedPlayerModule();
		}
		return _instance;
	}

	/** Private contructor to preserve the singleton
	 * 
	 */
	
	private ThreadedPlayerModule() {
	}

	/**
	 * Plays the given video, using the path from the /mnt/video directory,
	 * Plays the video in a new thread, so the server can continue executing
	 * 
	 * @param videoPath
	 */

	public void play(String videoPath) {
		//String command = "/home/mitchell/play.sh /mnt/videos/" + videoPath.trim();
		String command = "omxplayer -o hdmi /mnt/videos/" + videoPath.trim();
		
		//create and run the thread
		//stop the currently running video before we decide to start a new one
		stop();
		player = new Player(command);
		
	}
	
	/** Stops the currently playing video 
	 * 
	 */
	
	public void stop() {
		if (player != null) {
			player.writeToProcess("q");
			player = null;
		}
	}
	
	/** Pause / resumes the currently playing video
	 * 
	 */
	
	public void pause() {
		if (player != null && player.isPlaying()) {
			player.writeToProcess("p");
		}
	}
	
	/** Seeks the video to the next chapter */
	
	public void nextChapter() {
		if (player != null && player.isPlaying()) {
			player.writeToProcess("o");
		}
	}
	
	/** Seeks the video to the previous chapter */
	
	public void previousChapter() {
		if (player != null && player.isPlaying()) {
			player.writeToProcess("i");
		}
	}
	
	/** Turns the volume up */
	
	public void volumeUp() {
		if (player != null && player.isPlaying()) {
			player.writeToProcess("+");
		}
	}
	
	/** Turns the volume down */
	
	public void volumeDown() {
		if (player != null && player.isPlaying()) {
			player.writeToProcess("-");
		}
	}
	
	/** Constants for the key codes for seeking */
	
	private final char KEY_LEFT = 0x5b44;
	private final char KEY_RIGHT = 0x5b43;
	private final char KEY_UP = 0x5b41;
	private final char KEY_DOWN = 0x5b42;
	
	/** Seek forward 30 seconds */
	
	public void seekForwardSlow() {
		//RIGHT ARROW
		if (player != null && player.isPlaying()) {
			player.writeToProcess(KEY_RIGHT + "");
		}
	}
	
	/** Seek forward 600 seconds */
	
	public void seekForwardFast() {
		//UP ARROW
		if (player != null && player.isPlaying()) {
			player.writeToProcess(KEY_UP + "");
		}
	}
	
	/** Seek backwards 30 seconds */
	
	public void seekBackwardSlow() { 
		//LEFT ARROW
		if (player != null && player.isPlaying()) {
			player.writeToProcess(KEY_LEFT + "");
		}		
	}
	
	/** Seek backwards 600 seconds */
	
	public void seekBackwardFast() {
		//DOWN ARROW
		if (player != null && player.isPlaying()) {
			player.writeToProcess(KEY_DOWN + "");
		}
	}


}
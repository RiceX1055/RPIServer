package com.ricex.rpi.server.player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import com.ricex.rpi.server.RPIServer;
import com.ricex.rpi.server.RemoteServer;
import com.ricex.rpi.server.ServerPlayerModule;

/** The gui for the server that will report status of the server, as well
 * 		as allowing the user to control the playing movie
 * 
 * TODO: Implement play lists
 * 
 * @author Mitchell
 *
 */

public class RPIPlayer extends Application {
	
	/** Instance of the server that this GUI will interact with */
	private RPIServer rpiServer;
	
	/** The server for the remotes */
	private RemoteServer remoteServer;
	
	/** The thread that the rpi server will run in */
	private Thread rpiServerThread;
	
	/** The thread that the remote server will run in */
	private Thread remoteServerThread;
	
	/** The list view for the movies */
	private VideoListView movieListView;
	
	/** The list view for the clients */
	private ClientTableView clientTableView;
	
	/** The pane for the button controls on the right */
	private ButtonPane buttonPane;
	
	/** The player module for this GUI */
	private ServerPlayerModule playerModule;	
	
	/** The status label */
	private Label labStatus;
	
	/** The tab pane for the different views */
	private TabPane tabPane;
	
	public RPIPlayer() {	
		initServers();
		
		playerModule = new ServerPlayerModule(rpiServer.getConnectedClients());		
	}

	/** Initialize two servers, and start thier threads */
	
	private void initServers() {
		rpiServer = new RPIServer();
		remoteServer = new RemoteServer();
		
		rpiServerThread = new Thread(rpiServer);
		remoteServerThread = new Thread(remoteServer);
		
		rpiServerThread.setDaemon(true);
		remoteServerThread.setDaemon(true);
		
		rpiServerThread.start();
		remoteServerThread.start();
	}
	
	public static void main (String[] args) {
		launch();		
	}
	

	public void start(Stage stage) {
		stage.setTitle("RPI Player");
		
		movieListView = new VideoListView();	
		clientTableView = new ClientTableView(rpiServer);
		buttonPane = new ButtonPane(rpiServer, playerModule, movieListView);
		
		labStatus = new Label("Status goes here");
		
		BorderPane borderPane = new BorderPane();
		borderPane.setCenter(movieListView);
		borderPane.setRight(buttonPane);
		borderPane.setBottom(labStatus);
		
		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		
		Tab tabMovieView = new Tab("Movies");
		tabMovieView.setContent(borderPane);
		
		Tab tabClientView = new Tab("Clients");
		tabClientView.setContent(clientTableView);
	
		
		tabPane.getTabs().add(tabMovieView);
		tabPane.getTabs().add(tabClientView);
		
		Scene scene = new Scene(tabPane, 800, 600);
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
	}
}

package com.ricex.rpi.server.player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import com.ricex.rpi.server.Client;
import com.ricex.rpi.server.ClientChangeListener;
import com.ricex.rpi.server.ClientConnectionListener;
import com.ricex.rpi.server.RPIServer;


/**The view for displaying a table of the known clients, and thier status + id and possibly name
 * 
 * @author Mitchell
 *
 */

public class ClientTableView extends BorderPane implements ClientChangeListener, ClientConnectionListener {

	/** Instance of the running server to fetch information about the clients */
	private RPIServer server;
	
	/** List of the clients currently being displayed */	
	private ObservableList<Client> clients;
	
	/** The table view for displaying the clients */
	private TableView<Client> clientTable;
	
	
	public ClientTableView(RPIServer server) {
		this.server = server;
		
		server.addConnectionListener(this);
		
		clientTable = new TableView<Client>();
		clients = FXCollections.observableArrayList(server.getConnectedClients());
		clientTable.setItems(clients);
		
		TableColumn<Client, Long> idColumn = new TableColumn<Client, Long>("Id");
		TableColumn<Client, String> statusColumn = new TableColumn<Client, String>("Status");
		
		clientTable.getColumns().addAll(idColumn, statusColumn);
		
		setCenter(clientTable);
	}


	public void clientConnected(Client client) {
		clients.add(client);
	}

	public void clientDisconnected(Client client) {
		clients.remove(client);
	}

	public void clientChanged(Client client) {
		//update the given client
	}
}

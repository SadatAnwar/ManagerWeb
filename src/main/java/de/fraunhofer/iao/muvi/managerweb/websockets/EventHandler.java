package de.fraunhofer.iao.muvi.managerweb.websockets;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import de.fraunhofer.iao.muvi.managerweb.domain.ScreenID;
import de.fraunhofer.iao.muvi.managerweb.domain.ScreenState;

public class EventHandler {
	public static final String WEBSOCKET_EVENT_CONNECTION_CLOSE = "disconnect";
	public static final String WEBSOCKET_EVENT_CONNECTION_OPEN = "connection";
	private static final Log log = LogFactory.getLog(EventHandler.class);
	private Map<Integer, WebsocketClient> screenClients = new HashMap<Integer, WebsocketClient>();
	private Map<Integer, WebsocketClient> remoteClients = new HashMap<Integer, WebsocketClient>();

	private static EventHandler instance = null;

	protected EventHandler() {
		// Exists only to defeat instantiation.
	}

	public static EventHandler getInstance() {
		if (instance == null) {
			instance = new EventHandler();
		}
		return instance;
	}

	public void handleEvent(Session session, String message) {
		// Extract Data from incoming message
		String eventName;
		try {
			Object obj = new JSONObject(message);
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				eventName = jsonObject.getString("event");
				if (!jsonObject.isNull("data")) {
					Object intervention = jsonObject.get("data");
					if (intervention instanceof JSONObject) {
						// It's an object
						JSONObject dataObject = (JSONObject) intervention;
						if (eventName != null && dataObject != null) {
							handleEventType(session, eventName, dataObject);
						}
					} else {
						return;
					}
				} else {
					handleEventType(session, eventName, null);
				}
			}
		} catch (JSONException e) {
			log.error("Not a JSON-Object: " + message);
		}
	}

	private void handleEventType(Session session, String event,
			JSONObject dataObject) {
		log.debug("Handling Event with name: " + event);

		JSONObject data;
		String message = null;
		switch (event) {
		default:
		case "message":

			log.info("Received ClientMessage: " + dataObject.toString());
			break;

		case WEBSOCKET_EVENT_CONNECTION_OPEN:
			log.info("New Client connected to Server");

			// Reply and trigger registration

			try {
				data = new JSONObject();
				data.accumulate("message", "New here?");

				message = createJSONEvent("welcome", data).toString();
				session.getRemote().sendString(message);
			} catch (JSONException e) {
				log.error("JSON-Message creation failed. Please check data", e);
			} catch (IOException e) {
				log.error("Sending message to remote endpoint failed: "
						+ message, e);
			}

			break;

		case WEBSOCKET_EVENT_CONNECTION_CLOSE:
			log.info("Client disconnected from Server");

			WebsocketClient disconnectedClient = getDisconnectedClientBySession(session);
			// Search Session which disconnected
			if (disconnectedClient != null) {
				if(!disconnectedClient.isRemoteClient()) {
					removeClient(disconnectedClient.getScreenID());
					
					try {
						data = new JSONObject();				
						data.accumulate("id", disconnectedClient.getScreenID());
						
						// Inform all Remotes
						forwardToAllRemotes("remote_feedback_screen_unregistered",
								data.toString());
					} catch (JSONException e) {
						log.error("Couldn't add the ID of the disconnected client to a JSON String",e);
					}					
				} else {
					removeRemote(disconnectedClient.getScreenID());
				}
			}
			break;

		case "register_screen_client":
			try {
				// Get ScreenID from Message Payload
				int screenID = dataObject.getInt("id");

				// Register new client with this Instance
				this.addNewClient(session, screenID);

				// Let RemoteControlls know about the new Screen
				try {
					data = new JSONObject();
					data.accumulate("id", screenID);

					forwardToAllRemotes("remote_feedback_screen_registered",
							data.toString());

				} catch (JSONException e) {
					log.error(
							"JSON-Message creation failed. Please check data",
							e);
				}
			} catch (JSONException e) {
				log.error("ScreenID could not be found in JSON-Message", e);
			}

			break;

		case "register_remote_client":
			try {
				// Get remoteID from Message Payload
				int remoteID = dataObject.getInt("id");

				// Register new client with this Instance
				this.addNewRemote(session, remoteID);

			} catch (JSONException e) {
				log.error("ScreenID could not be found in JSON-Message", e);
			}

			break;

		case "remote_discoveryUpdate":
			// Some RemoteClient requested a discovery update of all currently
			// active screens
			try {
				log.debug("Update DiscoveryRequest received");
				for (WebsocketClient client : screenClients.values()) {
					data = new JSONObject();
					data.accumulate("id", client.getScreenID());

					// Create JSON message
					message = createJSONEvent(
							"remote_feedback_screen_registered", data)
							.toString();

					// Send JSON Message to requesting session
					session.getRemote().sendString(message);
				}
			} catch (JSONException e) {
				log.error("JSON-Message creation failed. Please check data", e);
			} catch (IOException e) {
				log.error(
						"Sending DiscoveryResponse message to remote endpoint failed: "
								+ message, e);
			}

			break;
			
		case "screenshot_response":
			// Get the requested action name from the data payload
			// String actionName = dataObject.getString("name");

			int sourceScreenID = -1;

			try {
				if (!dataObject.isNull("source")) {
					sourceScreenID = dataObject.getInt("source");
				}
				try {
					data = new JSONObject();
					data.accumulate("id", sourceScreenID);
					data.accumulate("data", dataObject);

					forwardToAllRemotes("screenshot_response",
							data.toString());

				} catch (JSONException e) {
					log.error(
							"JSON-Message creation failed. Please check data",
							e);			
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;			

		case "remote_event":
			// Get the requested action name from the data payload
			// String actionName = dataObject.getString("name");

			int destinedScreenID = -1;

			try {
				if (!dataObject.isNull("destination")) {
					destinedScreenID = dataObject.getInt("destination");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (destinedScreenID != -1) {
				// Send Event to a specific Screen with ID
				sendToClient(destinedScreenID, event, dataObject.toString());
			} else {
				// Relay Event to all available clients
				sendToClient(destinedScreenID, event, dataObject.toString());
				forwardToAllClients(event, dataObject.toString());
			}

			break;
		}
	}

	private WebsocketClient getDisconnectedClientBySession(Session session) {
		for (WebsocketClient client : screenClients.values()) {
			if (session.equals(client.getWsSession())) {
				return client;
			}
		}
		for (WebsocketClient client : remoteClients.values()) {
			if (session.equals(client.getWsSession())) {
				return client;
			}
		}
		return null;
	}

	private void addNewClient(Session session, int screenID) {
		WebsocketClient newClient = new WebsocketClient(session, screenID, false);
		screenClients.put(Integer.valueOf(screenID), newClient);
		log.debug("Added new ScreenClient: " + screenID);
	}
	
	public boolean isActive(int screenID) {
		return screenClients.containsKey(Integer.valueOf(screenID));
	}

	private void addNewRemote(Session session, int remoteID) {
		WebsocketClient newRemote = new WebsocketClient(session, remoteID, true);
		remoteClients.put(Integer.valueOf(remoteID), newRemote);
		log.debug("Added new Remote: " + remoteID);
	}

	private void removeClient(int screenID) {
		if (screenClients.containsKey(Integer.valueOf(screenID))) {
			screenClients.remove(Integer.valueOf(screenID));
			log.debug("Removed ScreenClient: " + screenID);
		}
	}
	
	private void removeRemote(int screenID) {
		if (remoteClients.containsKey(Integer.valueOf(screenID))) {
			remoteClients.remove(Integer.valueOf(screenID));
			log.debug("Removed RemoteClient: " + screenID);
		}
	}	

	public void sendToClient(int screenID, String event, String data) {
		if (screenClients.containsKey(Integer.valueOf(screenID))) {
			WebsocketClient client = screenClients.get(Integer
					.valueOf(screenID));
			// send a message to the current client WebSocket.
			sendEvent(client, event, data);
		}
	}
	
	public void gotoURL(ScreenID screen, URL url, ScreenState screenState) throws Exception{
		gotoURL(screen, url);
	}
	
	private void gotoURL(ScreenID screen, URL url) throws Exception {
		String event = "remote_event";
		String actionName = "LoadURLEvent";
		JSONObject jsonEventObject = createGenericActionEvent(screen.getId(), actionName);
	
		jsonEventObject.accumulate("url", url.toString());
		sendToClient(screen.getId(), event, jsonEventObject.toString());
	}

	public void forwardToAllClients(String event) {
		forwardToAllClients(event, null);
	}

	public void forwardToAllClients(String event, String data) {
		for (WebsocketClient client : screenClients.values()) {
			// send a message to the current client WebSocket.
			sendEvent(client, event, data);
		}
	}

	public void forwardToAllRemotes(String event, String data) {
		for (WebsocketClient client : remoteClients.values()) {
			// send a message to the current client WebSocket.
			synchronized(client) {
				sendEvent(client, event, data);
			}
		}
	}

	private void sendEvent(WebsocketClient client, String event, String data) {
		try {
			log.debug("Sending event \"" + event + "\" (data: " + data
					+ ") to client (id: " + client.getScreenID() + ")");
			client.sendEvent(event, data);
		} catch (IOException e) {
			log.error(
					"Event \"" + event + "\" couldn't be sent to client (id: "
							+ client.getScreenID() + ")", e);
		} catch (JSONException e) {
			log.error("Malformed JSON String during sendEvent", e);
		}
	}

	private static JSONObject createJSONData() throws JSONException {
		JSONObject data = new JSONObject();
		data.accumulate("id", "10");
		data.accumulate("name", "Julien");
		data.accumulate("nullData", null);

		return data;
	}
	
	private JSONObject createGenericActionEvent(int screenID, String action) throws JSONException {
		JSONObject data = new JSONObject();
		data.accumulate("name", action);
		data.accumulate("destination", screenID);

		return data;
	}

	public static JSONObject createJSONEvent(String event, JSONObject data)
			throws JSONException {
		JSONObject jsonEventObject = new JSONObject();
		jsonEventObject.accumulate("event", event);
		jsonEventObject.accumulate("data", data);

		return jsonEventObject;
	}

	private String createEvent(String event, JSONObject data)
			throws JSONException {
		return createJSONEvent(event, data).toString();
	}
}

package de.fraunhofer.iao.muvi.managerweb.websockets;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

public class WebsocketClient {
	private Session wsSession;
	private int screenID;
	private boolean externalMonitor;
	private boolean remoteClient;

	public WebsocketClient(Session session, int screenID, boolean remoteClient) {
		super();
		this.wsSession = session;
		this.screenID = screenID;
		this.remoteClient = remoteClient;
	}

	public Session getWsSession() {
		return wsSession;
	}

	public void setWsSession(Session wsSession) {
		this.wsSession = wsSession;
	}

	public int getScreenID() {
		return screenID;
	}

	public void setScreenID(int screenID) {
		this.screenID = screenID;
	}

	public boolean isExternalMonitor() {
		return externalMonitor;
	}

	public void setExternalMonitor(boolean externalMonitor) {
		this.externalMonitor = externalMonitor;
	}

	public void sendEvent(String eventName) throws IOException, JSONException {
		this.sendEvent(eventName, null);
	}	

	public boolean isRemoteClient() {
		return remoteClient;
	}

	public void setRemoteClient(boolean remoteClient) {
		this.remoteClient = remoteClient;
	}

	public void sendEvent(String event, String data) throws IOException,
			JSONException {
		if (wsSession != null) {
			JSONObject jsonEventObject = new JSONObject();
			jsonEventObject.accumulate("event", event);

			// Convert JSON Data String to JSON-Object to prevent encapsulation
			// of a "STRING" inside a JSON Object
			JSONObject jsonDataObject = new JSONObject(data);
			jsonEventObject.accumulate("data", jsonDataObject);

			wsSession.getRemote().sendString(jsonEventObject.toString());
		}
	}

}

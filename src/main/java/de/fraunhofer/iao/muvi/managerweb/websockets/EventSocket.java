package de.fraunhofer.iao.muvi.managerweb.websockets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.json.JSONException;

public class EventSocket extends WebSocketAdapter {

	private static final Log log = LogFactory.getLog(EventSocket.class);
	private EventHandler eventHandler;
	private Session session;

	public EventSocket() {
		eventHandler = EventHandler.getInstance();
	}

	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		// Store session:
		this.session = sess;
		this.session.setIdleTimeout(1000000);
		try {
			eventHandler
					.handleEvent(session,
							EventHandler.createJSONEvent(EventHandler.WEBSOCKET_EVENT_CONNECTION_OPEN, null)
									.toString());
		} catch (JSONException e) {
			log.error("JSON Event \"connected\" could not be processed correctly", e);
		}
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		//log.debug("Received TEXT message: " + message);
		eventHandler.handleEvent(session, message);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		try {
			eventHandler
					.handleEvent(session,
							EventHandler.createJSONEvent(EventHandler.WEBSOCKET_EVENT_CONNECTION_CLOSE, null)
									.toString());
		} catch (JSONException e) {
			log.error("JSON Event \"disconnect\" could not be processed correctly", e);
		}		
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		cause.printStackTrace(System.err);
	}
}
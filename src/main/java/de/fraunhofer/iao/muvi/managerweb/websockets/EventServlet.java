package de.fraunhofer.iao.muvi.managerweb.websockets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
@WebServlet(name = "MyEcho WebSocket Servlet", urlPatterns = { "/websocket" })
public class EventServlet extends WebSocketServlet
{
	private static final Log log = LogFactory.getLog(EventServlet.class);
	
	public EventServlet() throws ServletException {			
		log.debug("EventServlet initialized");
	}

    @Override
    public void configure(WebSocketServletFactory factory)
    {
    	factory.getPolicy().setIdleTimeout(990000);
        factory.register(EventSocket.class);
    }
}
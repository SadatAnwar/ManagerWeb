package de.fraunhofer.iao.muvi.managerweb;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import de.fraunhofer.iao.muvi.managerweb.utils.Utils;

/**
 * Starts the local development Web Server in Eclipse.
 * 
 * @author Bertram Frueh
 */
public class LocalJettyLauncher {

    private static final Log log = LogFactory.getLog(LocalJettyLauncher.class);

    private static final String HOST = "localhost";
    private static final int PORT = 3636;

    public static void main(String[] args) throws Exception {

        if (Utils.isPortInUse(HOST, PORT)) {
            log.error("Jetty (or something else) is already running on port " + PORT + "!");
        } else {
            String webapp = new File("src/main/webapp").getAbsolutePath();

            log.info("Starting local Jetty with " + webapp);

            // Request handler listening on "/monitoring".
            WebAppContext context = new WebAppContext();
            context.setContextPath("/manager");
            context.setResourceBase(webapp);
            context.setDescriptor(webapp + "/WEB-INF/web.xml");
            // Change "useFileMappedBuffer" to "false" to allow saving .css and .jsp files in
            // Eclipse
            // development environment while running a Jetty server.
            context.setDefaultsDescriptor("src/test/resources/webdefault.xml");

            // Redirect all requests not starting with "/monitoring" to "/monitoring".
            Handler redirect = new AbstractHandler() {

                @Override
                public void handle(String target, Request baseRequest, HttpServletRequest request,
                        HttpServletResponse response) throws IOException, ServletException {

                    String uri = request.getRequestURI();
                    if (!uri.startsWith("/manager/") && !uri.equals("/manager")
                            && !uri.equals("/favicon.ico")) {
                        response.sendRedirect("/manager");
                    }
                }
            };

            HandlerCollection handlers = new HandlerCollection();                      
            handlers.addHandler(context);
            handlers.addHandler(redirect);            

            final Server server = new Server(PORT);
            server.setHandler(handlers);
            server.start();

            log.info("Successfully started Jetty server (http://localhost:" + PORT + "/).");

            server.join();
        }
    }

}

package de.fraunhofer.iao.muvi.managerweb.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Response sent by the {@link HttpSender}.
 * 
 * @author Bertram Frueh
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "http-response")
public class HttpResponse {

    @XmlElement(name = "http-status-code")
    private int httpStatusCode;

    @XmlElement(name = "http-status-message", nillable = true)
    private String httpStatusMessage;

    @XmlElement
    private String content;

    public HttpResponse(int httpStatusCode, String httpStatusMessage, String content) {

        this.httpStatusCode = httpStatusCode;
        this.httpStatusMessage = httpStatusMessage;
        this.content = content;
    }

    /**
     * Returns true, if the HTTP status code is "200".
     */
    public boolean isOk() {

        return httpStatusCode == 200;
    }

    public int getHttpStatusCode() {

        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {

        this.httpStatusCode = httpStatusCode;
    }

    /**
     * Returns the POI status message which explains why a message has not been successfully
     * processed in case an error occurred.
     */
    public String getHttpStatusMessage() {

        return httpStatusMessage;
    }

    public void setHttpStatusMessage(String httpStatusMessage) {

        this.httpStatusMessage = httpStatusMessage;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    @Override
    public String toString() {

        return String.format("HTTP status code '%s' - HTTP status message '%s'", httpStatusCode,
                httpStatusMessage);
    }
}

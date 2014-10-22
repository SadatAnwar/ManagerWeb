package de.fraunhofer.iao.muvi.managerweb.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sends HTTP requests.
 * 
 * @author Bertram Frueh
 */
public class HttpSender {

    private static final Log log = LogFactory.getLog(HttpSender.class);

    private HttpClient client;

    public HttpSender() {

        client = new HttpClient();

    }

    /**
     * Sets the HTTP response timeout in milli seconds.
     */
    public void setTimeout(int timeout) {

        HttpParams params = client.getParams();
        params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeout);
    }

    /**
     * Sends a HTTP GET request to the given URL.
     */
    public synchronized HttpResponse doGet(String url) {

        String encodedUrl;
        try {
            encodedUrl = URIUtil.encodeQuery(url);
        } catch (URIException ex) {
            String message = "Invalid URL '" + url + "': " + ex.getMessage();
            log.error(message);
            throw new IllegalArgumentException(message, ex);
        }
        log.info("Calling " + url);

        GetMethod method = new GetMethod(encodedUrl);
        HttpResponse response = executeMethod(method);
        log.debug("Received " + response.toString());

        return response;
    }

    /**
     * Sends a HTTP GET request to the given URL.
     */
    public synchronized HttpResponse doGet(String url, Map<String, String> parameters) {

        if (parameters != null && !parameters.isEmpty()) {
            url += "?";
            for (Entry<String, String> parameter : parameters.entrySet()) {
                try {
                    url += parameter.getKey()
                            + "="
                            + URLEncoder.encode(parameter.getValue(),
                                    StandardCharsets.UTF_8.displayName()) + "&";
                } catch (UnsupportedEncodingException | IllegalArgumentException e) {
                    log.error("Unable to URL-encode parameter: " + parameter);
                    return new HttpResponse(-1, "Unable to URL-encode parameter: " + parameter,
                            null);
                }
            }
            url = url.substring(0, url.length() - 1);
        }
        log.info("Calling " + url);

        GetMethod method = new GetMethod(url);
        HttpResponse response = executeMethod(method);
        log.debug("Received " + response.toString());

        return response;
    }

    /**
     * Sends a HTTP POST request to the given URL.
     */
    public synchronized HttpResponse doPost(String url, Map<String, String> parameters) {

        log.info("Calling " + url);

        PostMethod method = new PostMethod(url);
        for (Entry<String, String> parameter : parameters.entrySet()) {
            try {
                method.addParameter(
                        parameter.getKey(),
                        URLEncoder.encode(parameter.getValue(),
                                StandardCharsets.UTF_8.displayName()));
            } catch (UnsupportedEncodingException | IllegalArgumentException e) {
                log.error("Unable to URL-encode parameter: " + parameter);
                return new HttpResponse(-1, "Unable to URL-encode parameter: " + parameter, null);
            }
        }
        HttpResponse response = executeMethod(method);
        log.debug("Received " + response.toString());

        return response;
    }

    /**
     * Sends the HTTP request to the server and wraps the response into a HttpResponse transport
     * object.
     * <p>
     * The HTTP status code is always filled and never {@code null}. The default is {@code -1}.
     */
    private HttpResponse executeMethod(HttpMethodBase method) {

        HttpResponse response;
        try {
            int httpStatusCode = client.executeMethod(method);
            String content = IOUtils.toString(method.getResponseBodyAsStream(),
                    method.getResponseCharSet());
            response = new HttpResponse(httpStatusCode, method.getStatusText(), content);
        } catch (Exception ex) {
            log.debug("Error occurred while sending HTTP request: " + ex.getClass().getName()
                    + ": " + ex.getMessage());
            response = new HttpResponse(-1, "Error occurred while sending HTTP request: "
                    + ex.getClass().getName() + ": " + ex.getMessage(), null);
        } finally {
            method.releaseConnection();
        }
        return response;
    }

    public static void main(String[] args) {

        HttpSender sender = new HttpSender();
        Map<String, String> parameters = new HashMap<>();

        String request = "{                     'addr': {                        'country': 'L',                        'postCode': '',                        'city': 'Wellenstein',                        'city2': '',                        'street': 'Rue de la Moselle 5',                        'houseNumber': ''                     },                     'options': [],                     'sorting': [],                     'additionalFields': [],                    'callerContext': {                        'properties': [{                           'key': 'CoordFormat',                          'value': 'PTV_MERCATOR'                        }, {                           'key': 'Profile',                          'value': 'default'                        }]                     }                  };";

        parameters.put("request", request);

        // parameters
        // .put("addr",
        // "<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns0=\"http://exception.core.jabba.ptvag.com\" xmlns:ns1=\"http://common.xserver.ptvag.com\" xmlns:ns2=\"http://xlocate.xserver.ptvag.com\" xmlns:ns3=\"http://types.xlocate.xserver.ptvag.com\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><env:Body><ns3:findAddress><ns3:Address_1 city=\"Stuttgart\" city2=\"\" country=\"\" houseNumber=\"\" postCode=\"70569\" state=\"\" street=\"Nobelstr. 12\" xsi:type=\"ns2:Address\"/><ns3:ArrayOfSearchOptionBase_2><ns2:SearchOptionBase param=\"STREET_HNRPOSITION\" value=\"3\" xsi:type=\"ns2:SearchOption\"/></ns3:ArrayOfSearchOptionBase_2><ns3:ArrayOfSortOption_3 xsi:nil=\"1\"/><ns3:ArrayOfResultField_4 xsi:nil=\"1\"/><ns3:CallerContext_5 log1=\"xserver-sample\" log2=\"java-client\" log3=\"ptv\"><ans1:wrappedProperties xmlns:ans1=\"http://baseservices.service.jabba.ptvag.com\"><ans1:CallerContextProperty key=\"Profile\" value=\"default\"/><ans1:CallerContextProperty key=\"CoordFormat\" value=\"OG_GEODECIMAL\"/><ans1:CallerContextProperty key=\"ResponseGeometry\" value=\"PLAIN\"/></ans1:wrappedProperties></ns3:CallerContext_5></ns3:findAddress></env:Body></env:Envelope>");
        HttpResponse res = sender.doPost(
                "http://137.251.109.179:50020/xlocate/rs/XLocate/findAddress", parameters);
        System.out.println(res.getContent());
    }

}

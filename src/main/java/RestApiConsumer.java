import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;



public class RestApiConsumer {

    public static final int GET = 0;
    public static final int POST = 1;
    public static final int DELETE = 2;
    public static final int PUT = 3;
    
    private ArrayList<NameValuePair> params;
    private ArrayList<NameValuePair> headers;


    private int responseCode;
    private String message;

    private String response;

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void AddParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }

    public void AddHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }


    public void execute(Integer method, String url, String body) throws Exception {
        switch (method) {
            case GET: {
                //add parameters
                String combinedParams = "";
                if (params != null && !params.isEmpty()) {
                    combinedParams += "?";
                    for (NameValuePair p : params) {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                        if (combinedParams.length() > 1) {
                            combinedParams += "&" + paramString;
                        } else {
                            combinedParams += paramString;
                        }
                    }
                }

                HttpGet request = new HttpGet(url + combinedParams);
                //add headers
                if (headers != null)
                    for (NameValuePair h : headers) {
                        request.addHeader(h.getName(), h.getValue());
                    }

                executeRequest(request);
                break;
            }
            case POST: {
                HttpPost request = new HttpPost(url);
                request.setHeader("Content-Type", "application/json");
                //add headers
                if (headers != null)
                    for (NameValuePair h : headers) {
                        request.addHeader(h.getName(), h.getValue());
                    }

                if (params != null && !params.isEmpty()) {
                    request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                }
                if (body != null)
                    request.setEntity(new ByteArrayEntity(body.getBytes(HTTP.UTF_8)));

                executeRequest(request);
                break;
            }
            case DELETE:{
                HttpDelete delete =new HttpDelete(url);
                delete.setHeader("Content-Type", "application/json");
                executeRequest(delete);
            }
            case PUT:{
                HttpPut put =new HttpPut(url);
                if (body != null)
                    put.setEntity(new ByteArrayEntity(body.getBytes(HTTP.UTF_8)));
                put.setHeader("Content-Type", "application/json");
                executeRequest(put);
            }
        }
    }

    private void executeRequest(HttpUriRequest request) {
        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse httpResponse;

        request.addHeader("content-type", "text/plain");
        request.addHeader("Accept","text/plain");
       // request.setHeader("Content-Type", "text/json");

        try {

            HttpContext localContext = new BasicHttpContext();
            httpResponse = httpClient.execute(request, localContext);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {

                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);

                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e) {
            httpClient.getConnectionManager().shutdown();
            Logger.getLogger("").log(Level.SEVERE,  e.toString());
        } catch (IOException e) {
            httpClient.getConnectionManager().shutdown();
            Logger.getLogger("").log(Level.SEVERE,  e.toString());;
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        	Logger.getLogger("").log(Level.SEVERE,  e.toString());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            	Logger.getLogger("").log(Level.SEVERE,  e.toString());
            }
        }
        return sb.toString();
    }

}

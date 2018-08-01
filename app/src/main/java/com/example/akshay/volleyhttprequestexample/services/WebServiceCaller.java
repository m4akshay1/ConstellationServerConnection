package com.example.akshay.volleyhttprequestexample.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.akshay.volleyhttprequestexample.http.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class WebServiceCaller {

    private static final String FROM_KEY = "from";
    private static final String TO_KEY = "to";
    private static final String STAIRS_KEY = "stairs";
    private static final String ELEVATORS_KEY = "elevators";

    public static void request(final Context context,
                               String serverUrl,
                               String route,
                               String from,
                               String to,
                               boolean stairs,
                               boolean elevators) {
        // declare
        String webServiceQuery;
        RequestQueue requestQueue;
        Response.Listener<String> responseListener;
        Response.ErrorListener errorListener;
        StringRequest cmxApiCallRequest;
        // define
        webServiceQuery = getWebServiceQuery(serverUrl, route);
        requestQueue = Volley.newRequestQueue(context);
        responseListener = getResponseListener(requestQueue);
        errorListener = HttpClient.getResponseErrorListener(context,
                requestQueue);
        cmxApiCallRequest = getApiCallRequest(webServiceQuery, responseListener,
                errorListener, from, to, stairs, elevators);
        // add
        requestQueue.add(cmxApiCallRequest);
    }

    /**
     * Listens for a response. Once the response has been retrieved,
     * the instructions provided in onResponse(...) will be carried out.
     * @param httpRequestQueue
     * @return An instance of a response listener, as specified by this method
     */
    private static Response.Listener<String> getResponseListener(
            final RequestQueue httpRequestQueue) {
        return new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    // TODO: grab the response here and carry out what's needed
                    UiService.printString(response);
                } catch (Exception error) {
                    error.printStackTrace();
                    httpRequestQueue.stop();
                }
            }

        };
    }

    /**
     * @param url              String containing the URL for the API call
     * @param responseListener
     * @param errorListener
     *  @return
     */
    private static StringRequest getApiCallRequest(
            String url, Response.Listener<String> responseListener,
            Response.ErrorListener errorListener, String from, String to,
            boolean stairs, boolean elevators) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        params.put(FROM_KEY, from);
        params.put(TO_KEY, to);
        params.put(STAIRS_KEY, String.valueOf(stairs));
        params.put(ELEVATORS_KEY, String.valueOf(elevators));
        //headers.put(HttpClient.AUTHORIZATION, auth)
        headers.put(HttpClient.CONTENT_TYPE, HttpClient.APP_X_WWW_FORM_URLENCODED);
        return HttpClient.newStringPostRequest(url, responseListener,
                errorListener, params, headers);
    }

    private static String getWebServiceQuery(String apiUrl, String route) {
        return apiUrl + route;
    }


}

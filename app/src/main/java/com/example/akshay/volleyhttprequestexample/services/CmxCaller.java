// Created by Ron Rihoo on 07/30/2018
package com.example.akshay.volleyhttprequestexample.services;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.akshay.volleyhttprequestexample.http.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles CMX API calls.
 */
public class CmxCaller {

    public static final String QUESTION = "?";
    public static final String SLASH = "/";

    public static final String CLIENTS = "clients";
    public static final String IP_ADDRESS_KEY = "ipAddress=";

    public static void request(final Context context,
                               String serverUrl,
                               String route,
                               String ipAddress,
                               String user,
                               String pass) {
        // declare
        String cmxApiCall;
        RequestQueue requestQueue;
        Response.Listener<String> responseListener;
        Response.ErrorListener errorListener;
        StringRequest cmxApiCallRequest;
        // define
        cmxApiCall = getCmxCallUrl(serverUrl, route, ipAddress);
        requestQueue = Volley.newRequestQueue(context);
        responseListener = getResponseListener(requestQueue);
        errorListener = HttpClient.getResponseErrorListener(context,
                requestQueue);
        cmxApiCallRequest = getApiCallRequest(cmxApiCall, responseListener,
                errorListener, HttpClient.getAuthString(user, pass));
        // add
        requestQueue.add(cmxApiCallRequest);
    }

    public static void requestWithIpAddress(final Context context,
                                            String serverUrl,
                                            String ipAddress,
                                            String user,
                                            String pass) {
        request(context, serverUrl, CLIENTS, ipAddress, user, pass);
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
     * @param auth             A concatenation of the String "Basic " and a
     *                         Base-64 encoded String containing the user and
     *                         pass in the following format: "user:pass"
     * @return
     */
    private static StringRequest getApiCallRequest(
            String url, Response.Listener<String> responseListener,
            Response.ErrorListener errorListener, final String auth) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpClient.AUTHORIZATION, auth);
        headers.put(HttpClient.CONTENT_TYPE, HttpClient.APP_JSON);
        return HttpClient.newStringGetRequest(url, responseListener,
                errorListener, params, headers);
    }

    private static String getCmxCallUrl(String apiUrl, String route,
                                        String ipAddress) {
        return apiUrl + SLASH + route + QUESTION + IP_ADDRESS_KEY + ipAddress;
    }


}

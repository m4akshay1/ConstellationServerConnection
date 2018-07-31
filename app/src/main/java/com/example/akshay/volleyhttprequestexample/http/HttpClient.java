// Created by Ron Rihoo on 07/30/2018
package com.example.akshay.volleyhttprequestexample.http;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Provide independent client-side HTTP handling.
 */
public class HttpClient {

    private static boolean debug = true;

    private static final String TAG = "HttpClient";

    // error messages
    public static final String CONNECTION_ERROR = "An error occurred with " +
            "the server...";

    // HTTP params
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APP_X_WWW_FORM_URLENCODED = "application/" +
            "x-www-form-urlencoded";
    public static final String APP_JSON = "application/json";
    public static final String BASIC_ = "Basic ";

    // common strings
    public static final String COLON = ":";
    public static final String UTF_8 = "UTF-8";

    public static StringRequest newStringPostRequest(String url,
                                   Response.Listener<String> responseListener,
                                   Response.ErrorListener errorListener,
                                   final Map<String, String> params,
                                   final Map<String, String> headers) {
        return getStringRequest(url, responseListener, errorListener,
                params, headers, Request.Method.POST);
    }


    public static StringRequest newStringGetRequest(String url,
                                   Response.Listener<String> responseListener,
                                   Response.ErrorListener errorListener,
                                   final Map<String, String> params,
                                   final Map<String, String> headers) {
        return getStringRequest(url, responseListener, errorListener,
                params, headers, Request.Method.GET);
    }

    /**
     *
     * @param url A String containing the complete URL for the API call
     * @param responseListener
     * @param errorListener
     * @param params Map<String, String> object containing HTTP parameters
     * @param headers Map<String, String> object containing HTTP headers
     * @param requestMethod int from Request.Method to set method (GET/POST)
     * @return
     */
    public static StringRequest getStringRequest(String url,
            Response.Listener<String> responseListener,
            Response.ErrorListener errorListener,
            final Map<String, String> params,
            final Map<String, String> headers, int requestMethod) {
        return new StringRequest(requestMethod, url, responseListener,
                errorListener) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

        };
    }

    public static Response.ErrorListener getResponseErrorListener(
            final Context context, final RequestQueue httpRequestQueue) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        CONNECTION_ERROR,
                        Toast.LENGTH_LONG).show();
                error.printStackTrace();
                httpRequestQueue.stop();
            }
        };
    }

    public static String getAuthString(String user, String pass) {
        try {
            return BASIC_ + encodeToBase64(user + COLON + pass);
        } catch (UnsupportedEncodingException e) {
            if (debug) {
                Log.d(TAG, "getAuthString(...) – Could not encode to base 64");
            }
            e.printStackTrace();
        }
        return BASIC_;
    }

    public static String encodeToBase64(String str)
            throws UnsupportedEncodingException {
        return Base64.encodeToString(str.getBytes(UTF_8), Base64.DEFAULT);
    }

}

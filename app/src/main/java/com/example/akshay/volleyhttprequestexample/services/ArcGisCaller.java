// Created by Ron Rihoo on 07/30/2018
package com.example.akshay.volleyhttprequestexample.services;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.akshay.volleyhttprequestexample.http.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles ArcGIS web service API calls and path data parsing.
 */
public class ArcGisCaller {

    public static final String COMMA = ",";
    public static final String CLOSING_BRACKET = "]";
    public static final String OPENING_BRACKET = "[";

    public static final String DESTINATION = "destination";

    private static ArrayList<String> coordinateList = null;

    public static void requestPaths(final Context context,
                                    String serverUrl,
                                    String route,
                                    final String roomNumberDestination,
                                    ArrayList<String[]> parsedPathData) {
        // declare
        RequestQueue requestQueue;
        Response.Listener<String> responseListener;
        Response.ErrorListener errorListener;
        StringRequest destinationPostRequest;
        // define
        requestQueue = Volley.newRequestQueue(context);
        responseListener = getResponseListener(
                parsedPathData, requestQueue);
        errorListener = HttpClient.getResponseErrorListener(
                context, requestQueue);
        destinationPostRequest = getPostRequestForDestination(
                serverUrl + route, responseListener, errorListener,
                roomNumberDestination);
        // add
        requestQueue.add(destinationPostRequest);
    }

    private static Response.Listener<String> getResponseListener(
            final ArrayList<String[]> parsedPathData,
            final RequestQueue httpRequestQueue) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String[] paths = response.split(COMMA);
                    initializeCoordinateList();
                    splitPathsIntoXyz(paths, parsedPathData);
                    formatPathData(parsedPathData);
                    printPaths(getCoordinateList());
                } catch (Exception error) {
                    error.printStackTrace();
                    httpRequestQueue.stop();
                }
            }
        };
    }

    private static StringRequest getPostRequestForDestination(
            String url, Response.Listener<String> responseListener,
            Response.ErrorListener errorListener,
            final String roomNumberDestination) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String> headers = new HashMap<String, String>();
        params.put(DESTINATION, roomNumberDestination);
        headers.put(HttpClient.CONTENT_TYPE,
                HttpClient.APP_X_WWW_FORM_URLENCODED);
        return HttpClient.newStringPostRequest(url, responseListener,
                errorListener, params, headers);
    }

    private static void splitPathsIntoXyz(String[] paths,
                                             ArrayList<String[]> pathList) {
        for (int i = 0; i < paths.length; i += 3) {
            String[] coordinate = new String[3];
            if (!(i + 2 >= paths.length)) {
                coordinate[0] = paths[i];
                coordinate[1] = paths[i + 1];
                coordinate[2] = paths[i + 2];
                pathList.add(coordinate);
            }
        }
    }

    private static void formatPathData(ArrayList<String[]> pathList) {
        for (int j = 0; j < pathList.size(); j++) {
            coordinateList.add(OPENING_BRACKET +
                    pathList.get(j)[0] + COMMA +
                    pathList.get(j)[1] + COMMA +
                    pathList.get(j)[2] + CLOSING_BRACKET);
        }
    }

    private static void printPaths(ArrayList<String> paths) {
        for (String path : paths) {
            UiService.printString(path);
        }
    }

    private static void initializeCoordinateList() {
        coordinateList = new ArrayList<>();
    }

    public static ArrayList<String> getCoordinateList() {
        return coordinateList;
    }

}

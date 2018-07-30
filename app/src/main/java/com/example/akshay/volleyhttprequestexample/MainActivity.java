package com.example.akshay.volleyhttprequestexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
This is an HTTP Request that sends the destination to the web-server and receives the ArcGIS output.
This program is designed for the UT Dallas indoor-navigation system.
This program was coded by Akshay Durvasula on 7/29/2018.
hi
 */

public class MainActivity extends AppCompatActivity {

    //URL to the web-server, change it to your web-server's URL
    private String serverURL = "http://10.0.2.2/";
    //a variable to hold the parsed path data from ArcGIS
    ArrayList<String[]> parsedPathData = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //trigger this event when clicking the sendDestinationBTN in the UI
        Button sendDestinationBTN = findViewById(R.id.sendDestinationBTN);
        sendDestinationBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get the destination to the room number in the first parameter
                getArcGISPath("2.326");
            }
        });
    }

    //sends an HTTP Request to the PHP page, which is the URL in the StringRequest variable
    private void getArcGISPath(final String roomNumberDestination) {
        //send an HTTPRequest to the server to send the destination to the server
        final RequestQueue httpRequestQueue = Volley.newRequestQueue(MainActivity.this);
        final StringRequest httpSendDestination = new StringRequest(Request.Method.POST, (serverURL + "index.php"),
                //success, the request responded successfully!
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //getting the path data and parsing it into an array
                            String[] pathData = response.split(",");

                            /*spliting the path data into individual coordinates, for every 3 commas in pathData
                            it is considered a coordinate(z,x,y)*/
                            for(int i = 0; i < pathData.length; i += 3) {
                                String[] coordinate = new String[3];
                                //make sure we don't cause an ArrayOutOfBounds exception
                                if(!(i + 2 >= pathData.length)) {
                                    coordinate[0] = pathData[i];
                                    coordinate[1] = pathData[i + 1];
                                    coordinate[2] = pathData[i + 2];
                                    parsedPathData.add(coordinate);
                                }
                            }

                            //output each coordinate into an array format
                            for(int j = 0; j < parsedPathData.size(); j++) {
                                System.out.println("[" + parsedPathData.get(j)[0] + "," + parsedPathData.get(j)[1] + "," + parsedPathData.get(j)[2] + "]");
                            }
                        } catch (Exception error) {
                            error.printStackTrace();
                            httpRequestQueue.stop();
                        }
                    }
                },
                //error, the request responded with a failure...
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "An error occurred with the server...", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        httpRequestQueue.stop();
                    }
                }
        ) {
            //POST variables to send to the web-server
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("destination", roomNumberDestination);
                return params;
            }
            //header values to send to the web-server
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        httpRequestQueue.add(httpSendDestination);
    }
}

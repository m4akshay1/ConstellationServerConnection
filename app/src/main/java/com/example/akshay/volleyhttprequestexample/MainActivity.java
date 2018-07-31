// This app is designed for the UT Dallas indoor-navigation system.
//
// Created by Akshay Durvasula on 7/29/2018
// Forked and modified by Ron Rihoo on 7/30/2018
package com.example.akshay.volleyhttprequestexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.akshay.volleyhttprequestexample.services.ArcGisCaller;
import com.example.akshay.volleyhttprequestexample.services.CmxCaller;

import java.util.ArrayList;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private final String cmxApiUrl = "cmxproxy01.utdallas.edu/api";
    private final String arcGisApiUrl = "10.0.2.2";
    private final String arcGisApiRoute = "index.php";

    private String roomNumberDestination;
    private String ipAddress;
    private String cmxUser;
    private String cmxPass;

    private ArrayList<String[]> parsedPathData = new ArrayList<String[]>();
    private Button sendDestinationButton;
    private Button cmxRequestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        setVariables();
        setupComponents();
        setButtonOnClickListeners();
    }

    private void setVariables() {
        roomNumberDestination = "2.326";
        ipAddress = "";
        cmxUser = "";
        cmxPass = "";
    }

    private void setupComponents() {
        sendDestinationButton = findViewById(R.id.send_destination_button);
        cmxRequestButton = findViewById(R.id.cmx_request_button);
    }

    private void setButtonOnClickListeners() {
        sendDestinationButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                getArcGisPaths(arcGisApiUrl, arcGisApiRoute,
                        roomNumberDestination);
            }

        });
        cmxRequestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getCmxData(cmxApiUrl, ipAddress, cmxUser, cmxPass);
            }

        });
    }

    /**
     * Sends an HTTP request to the ArcGIS web service API.
     * @param arcGisApiUrl
     * @param arcGisApiRoute
     * @param roomNumberDestination
     */
    private void getArcGisPaths(String arcGisApiUrl, String arcGisApiRoute,
                               String roomNumberDestination) {
        ArcGisCaller.requestPaths(this, arcGisApiUrl, arcGisApiRoute,
                roomNumberDestination, parsedPathData);
    }

    /**
     * Sends an HTTP request to the CMX proxy web service API.
     * @param cmxApiUrl
     * @param ipAddress Target IP address
     * @param user CMX proxy username
     * @param pass CMX proxy password
     */
    private void getCmxData(String cmxApiUrl, String ipAddress, String user,
                            String pass) {
        CmxCaller.requestWithIpAddress(this, cmxApiUrl, ipAddress,
                user, pass);
    }

}

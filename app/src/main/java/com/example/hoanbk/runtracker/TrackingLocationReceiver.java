package com.example.hoanbk.runtracker;

import android.content.Context;
import android.location.Location;
import android.util.Log;

/**
 * Created by hoanbk on 3/25/2017.
 */

public class TrackingLocationReceiver extends LocationReceiver {
    private static final String TAG = "TrackingLocationReceiver";
    @Override
    protected void onLocationReceived(Context context, Location loc) {
        Log.i(TAG, "onLocationReceived");

        RunManager.getInstance(context).insertLocation(loc);
    }
}

package com.example.hoanbk.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by hoanbk on 3/24/2017.
 */

public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = "LocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        // if got a Location Extra, use it
        Location loc = (Location)intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (loc != null) {
            onLocationReceived(context, loc);
            return;
        }
        // If get here, something else has happened
        if (intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)) {
            boolean enabled = intent
                    .getBooleanExtra(LocationManager.KEY_LOCATION_CHANGED, false);
            onProviderEnabledChanged(enabled);
        }
    }

    protected void onLocationReceived(Context context, Location loc) {
        Log.d(TAG, this + " got location from " + loc.getProvider() + ": "
        +loc.getLatitude() + ", " + loc.getLongitude());
    }

    protected void onProviderEnabledChanged(boolean enabled) {
        Log.d(TAG, "Provider " + (enabled ? "enabled" : "disabled"));
    }
}

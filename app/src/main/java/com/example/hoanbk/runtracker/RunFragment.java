package com.example.hoanbk.runtracker;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hoanbk on 3/23/2017.
 */

public class RunFragment extends Fragment {
    private static final String TAG = "RunFragment";

    private static final int REQUEST_LOCATION = 1;
    private Button mStartButton, mStopButton;
    private TextView mStartTextView, mLatitudeTextView
            , mLongitudeTextView, mAltitudeTextView
            , mDurationTextView;

    private RunManager mRunManager;
    private Location mLastLocation;
    private Run mRun;

    private LocationReceiver mLocationReceiver = new LocationReceiver(){
        @Override
        protected void onLocationReceived(Context context, Location loc) {
            super.onLocationReceived(context, loc);
            mLastLocation = loc;
            // update UI
            if (isVisible()) {
                updateUI();
            }
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            super.onProviderEnabledChanged(enabled);
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRunManager = RunManager.getInstance(getActivity());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            int permission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if(permissions.size() != 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_LOCATION);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_run, container, false);

        mStartTextView = (TextView)v.findViewById(R.id.run_startedTextView);
        mLatitudeTextView = (TextView)v.findViewById(R.id.run_latitudeTextView);
        mLongitudeTextView = (TextView)v.findViewById(R.id.run_longitudeTextView);
        mAltitudeTextView = (TextView)v.findViewById(R.id.run_altitudeTextView);
        mDurationTextView = (TextView)v.findViewById(R.id.run_durationTextView);

        mStartButton = (Button)v.findViewById(R.id.run_startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRun = new Run();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mRunManager.startLocationUpdates();
                        updateUI();
                    }
                } else {
                    mRunManager.startLocationUpdates();
                    updateUI();
                }
            }
        });

        mStopButton = (Button)v.findViewById(R.id.run_stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mRunManager.stopLocationUpdates();
                        updateUI();
                    }
                } else {
                    mRunManager.stopLocationUpdates();
                    updateUI();
                }
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(RunManager.ACTION_LOCATION));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    private void updateUI() {
        boolean started = mRunManager.isTrackingRun();
        mStartButton.setEnabled(!started);
        mStopButton.setEnabled(started);

        if (mRun != null) {
            mStartTextView.setText(mRun.getStartDate().toString());
        }

        int durationSeconds = 0;
        if (mRun!= null && mLastLocation != null) {
            durationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());
            mLatitudeTextView.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(String.valueOf(mLastLocation.getAltitude()));
        }
        mDurationTextView.setText(Run.formatDuration(durationSeconds));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION: {
//                for (int i = 0; i <= grantResults.length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                        mRunManager.startLocationUpdates();
//                        updateUI();
//                    }
//                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

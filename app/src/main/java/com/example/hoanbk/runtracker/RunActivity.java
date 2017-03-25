package com.example.hoanbk.runtracker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RunActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new RunFragment();
    }
}

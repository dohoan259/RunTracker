package com.example.hoanbk.runtracker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RunActivity extends SingleFragmentActivity {
    public static final String EXTRA_RUN_ID =
            "com.example.hoanbk.runtracker.run_id";

    @Override
    public Fragment createFragment() {
        long run_id = getIntent().getLongExtra(EXTRA_RUN_ID, -1);
        if (run_id != -1) {
            return RunFragment.newInstance(run_id);
        } else {
            return new RunFragment();
        }
    }
}

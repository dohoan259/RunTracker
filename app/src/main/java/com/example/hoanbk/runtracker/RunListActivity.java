package com.example.hoanbk.runtracker;

import android.support.v4.app.Fragment;

/**
 * Created by hoanbk on 3/25/2017.
 */

public class RunListActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new RunListFragment();
    }
}

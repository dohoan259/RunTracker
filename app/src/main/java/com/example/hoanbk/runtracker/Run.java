package com.example.hoanbk.runtracker;

import java.util.Date;

/**
 * Created by hoanbk on 3/25/2017.
 */

public class Run {
    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date startDate) {
        mStartDate = startDate;
    }

    private Date mStartDate;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    private long mId;

    public Run() {
        mStartDate = new Date();
    }

    public int getDurationSeconds(long endMillis) {
        return (int)((endMillis - mStartDate.getTime())/1000);
    }

    public static String formatDuration(int durationSeconds) {
        int seconds = durationSeconds % 60;
        int minutes = ((durationSeconds - seconds) / 60) % 60;
        int hours = (durationSeconds - (minutes * 60) -seconds) / 3600;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

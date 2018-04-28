package com.github.seelikes.android.parser.demo;

import android.app.Application;

import com.github.seelikes.android.log.SaLog;
import com.github.seelikes.android.log.SaLogConstants;
import com.github.seelikes.android.parser.AndroidParser;

import java.util.List;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        SaLogConstants.setLogLevel(BuildConfig.DEBUG ? SaLogConstants.VERBOSE : SaLogConstants.ERROR);
        super.onCreate();
        List<Class<? extends MainEntry>> entries = AndroidParser.getClassExtends(this, MainEntry.class, getPackageName());
        SaLog.i(MainApplication.class, "onCreate", "entries.isEmpty(): " + entries.isEmpty());
        if (!entries.isEmpty()) {
            for (int i = 0; i < entries.size(); ++i) {
                try {
                    entries.get(i).newInstance().onCreate(this);
                }
                catch (InstantiationException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

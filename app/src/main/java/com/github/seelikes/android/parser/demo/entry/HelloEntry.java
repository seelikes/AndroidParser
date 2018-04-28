package com.github.seelikes.android.parser.demo.entry;

import android.app.Application;

import com.github.seelikes.android.log.SaLog;
import com.github.seelikes.android.parser.demo.MainEntry;

public class HelloEntry extends MainEntry {
    @Override
    public void onCreate(Application application) {
        SaLog.i(HelloEntry.class, "onCreate", "enter");
    }
}

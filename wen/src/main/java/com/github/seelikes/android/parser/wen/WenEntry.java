package com.github.seelikes.android.parser.wen;

import android.app.Application;

import com.github.seelikes.android.log.SaLog;
import com.github.seelikes.android.parser.library.entry.MainEntry;

public class WenEntry extends MainEntry {
    @Override
    public void onCreate(Application application) {
        SaLog.i(WenEntry.class, "onCreate.UL1901LP.DI1211", "enter.");
    }
}

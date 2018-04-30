package com.github.seelikes.android.parser.library;

import android.app.Application;

import com.github.seelikes.android.log.SaLog;
import com.github.seelikes.android.parser.library.entry.MainEntry;

public class LibraryEntry extends MainEntry {
    @Override
    public void onCreate(Application application) {
        SaLog.v(LibraryEntry.class, "onCreate.UL1901LP.DI1211", "enter.");
    }
}

package com.github.seelikes.android.parser.demo;

import android.app.Application;
import android.widget.Toast;

import com.github.seelikes.android.log.SaLog;
import com.github.seelikes.android.log.SaLogConstants;
import com.github.seelikes.android.parser.AndroidParser;
import com.github.seelikes.android.parser.library.entry.MainEntry;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        SaLogConstants.setLogLevel(BuildConfig.DEBUG ? SaLogConstants.VERBOSE : SaLogConstants.ERROR);
        SaLog.i(MainApplication.class, "onCreate.UL1901LP.DI1211", "enter,");
        super.onCreate();

        Observable.just(MainEntry.class)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.computation())
            .subscribe(c -> {
                try {
                    SaLog.i(MainApplication.class, "onCreate.UL1901LP.DI1211", "class pass start.");
                    List<Class<? extends MainEntry>> entries = AndroidParser.getClassExtends(this, MainEntry.class, getPackageName().substring(0, getPackageName().lastIndexOf(".")));
                    SaLog.i(MainApplication.class, "onCreate.UL1901LP.DI1211", "class pass complete.");
                    SaLog.i(MainApplication.class, "onCreate.UL1901LP.DI1211", "entries.isEmpty(): " + entries.isEmpty());
                    if (!entries.isEmpty()) {
                        SaLog.i(MainApplication.class, "onCreate.UL1901LP.DI1211", "entries.size(): " + entries.size());
                        for (int i = 0; i < entries.size(); ++i) {
                            try {
                                SaLog.i(MainApplication.class, "onCreate.UL1901LP.DI1211", "i: " + i + "; entries.size().getCanonicalName(): " + entries.get(i).getCanonicalName());
                                entries.get(i).newInstance().onCreate(this);
                            }
                            catch (InstantiationException e) {
                                e.printStackTrace();
                            }
                            catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        SaLog.i(MainApplication.class, "onCreate.UL1901LP.DI1211", "class parse over.");
                        Observable.just(MainApplication.class)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(m -> Toast.makeText(this, "class pass over", Toast.LENGTH_LONG).show());
                    }
                }
                catch (Throwable e) {
                    SaLog.e(MainApplication.class, "onCreate.UL1901LP.DI1211", e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            });

        SaLog.i(MainApplication.class, "onCreate.UL1901LP.DI1211", "leave,");
    }
}

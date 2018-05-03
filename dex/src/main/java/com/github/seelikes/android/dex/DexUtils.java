package com.github.seelikes.android.dex;

import android.content.Context;

public class DexUtils {
    public static Parser with(Context context) {
        return new DexParser(context);
    }
}

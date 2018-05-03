package com.github.seelikes.android.dex;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * 解析Dex实体封装
 */
class DexParser implements Parser {
    private Context context;
    private String basePackage;
    private boolean instantRun;

    DexParser(Context context) {
        this.context = context;
    }

    @Override
    public DexParser basePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    @Override
    public DexParser instantRun(boolean instantRun) {
        this.instantRun = instantRun;
        return this;
    }

    /**
     * 获取所有的类路径
     * @param acceptor 结果接收回调
     * @return U know
     */
    public DexParser getSourcePaths(Action<List<String>> acceptor) {
        List<String> sourcePaths = new ArrayList<>();
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            sourcePaths.add(applicationInfo.sourceDir);

            File sourceApk = new File(applicationInfo.sourceDir);
            File dexDir = new File(applicationInfo.dataDir, "code_cache" + File.separator + "secondary-dexes");
            SharedPreferences preferences = context.getSharedPreferences("multidex.version", Context.MODE_MULTI_PROCESS);
            int totalDexNumber = preferences.getInt("dex.number", 1);
            for (int secondaryNumber = 2; secondaryNumber <= totalDexNumber; ++secondaryNumber) {
                String fileName = sourceApk.getName() + secondaryNumber + ".zip";
                File extractedFile = new File(dexDir, fileName);
                if (extractedFile.isFile()) {
                    sourcePaths.add(extractedFile.getAbsolutePath());
                }
                else {
                    acceptor.onAction(sourcePaths);
                    return this;
                }
            }

            if (instantRun) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && applicationInfo.splitSourceDirs != null) {
                    sourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
                }
                else {
                    try {
                        Class pathsByInstantRun = Class.forName("com.android.tools.fd.runtime.Paths");
                        Method getDexFileDirectory = pathsByInstantRun.getMethod("getDexFileDirectory", String.class);
                        String instantRunDexPath = (String) getDexFileDirectory.invoke(null, applicationInfo.packageName);
                        File instantRunFilePath = new File(instantRunDexPath);
                        if (instantRunFilePath.exists() && instantRunFilePath.isDirectory()) {
                            File[] dexFile = instantRunFilePath.listFiles();
                            for (File file : dexFile) {
                                if (file != null && file.exists() && file.isFile() && file.getName().endsWith(".dex")) {
                                    sourcePaths.add(file.getAbsolutePath());
                                }
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        acceptor.onAction(sourcePaths);
                        return this;
                    }
                }
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            acceptor.onAction(sourcePaths);
            return this;
        }

        acceptor.onAction(sourcePaths);
        return this;
    }

    @Override
    public DexParser getClass(Action<Class<?>> checker) {
        getSourcePaths(paths -> {
            if (paths.isEmpty()) {
                return;
            }
            for (String path : paths) {
                try {
                    DexFile dex = new DexFile(path);
                    Enumeration<String> entries = dex.entries();
                    while (entries.hasMoreElements()) {
                        String className = entries.nextElement();
                        if (basePackage == null || basePackage.isEmpty() || className.startsWith(basePackage)) {
                            try {
                                checker.onAction(context.getClassLoader().loadClass(className));
                            }
                            catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return this;
    }
}

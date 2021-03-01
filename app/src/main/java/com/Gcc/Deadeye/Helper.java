package com.Gcc.Deadeye;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

public class Helper {

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean checkVPN(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_VPN).isConnectedOrConnecting();

    }

    /**
     * Get the current Xposed version installed on the device.
     *
     * @param context The application context
     * @return The Xposed version or {@code null} if Xposed isn't installed.
     */
    public static boolean getXposedVersion(Context context) {
        try {
            File xposedBridge = new File("/system/framework/XposedBridge.jar");
            if (xposedBridge.exists()) {
                File optimizedDir = context.getDir("dex", Context.MODE_PRIVATE);
                DexClassLoader dexClassLoader = new DexClassLoader(xposedBridge.getPath(),
                        optimizedDir.getPath(), null, ClassLoader.getSystemClassLoader());
                Class<?> XposedBridge = dexClassLoader.loadClass("de.robv.android.xposed.XposedBridge");
                Method getXposedVersion = XposedBridge.getDeclaredMethod("getXposedVersion");
                if (!getXposedVersion.isAccessible()) getXposedVersion.setAccessible(true);
                Log.d("loooooooooooo", String.valueOf((boolean) getXposedVersion.invoke(null)));
                return (boolean) getXposedVersion.invoke(null);
            }
        } catch (Exception ignored) {
        }

        return false;
    }
}
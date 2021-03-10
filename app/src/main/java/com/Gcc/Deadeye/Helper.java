package com.Gcc.Deadeye;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import dalvik.system.DexClassLoader;

import static java.security.CryptoPrimitive.SIGNATURE;

public class Helper {
   private static final String APP_SIGNATURE = "46490ACB753B0D1DF9F14B6F1FEEC3FE32EC8508";  //release
  //  private static final String APP_SIGNATURE = "37F760E29CF520940697C165525198B2D9A6D764";  //debug
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

    public static boolean hookdetection(){
        Set<String> libraries=new HashSet<String>();
        String mapsFilename="/proc/"+android.os.Process.myPid()+"/maps";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while((line=reader.readLine())!=null){
                if(line.endsWith(".so")||line.endsWith(".jar")){
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n+1));
                }


            }
            for(String library:libraries){
                if(library.contains("com.saurik.substrate")) {
                    Log.wtf("HookDetection", "Substrate shared object found: " + library);
                    return true;
                }
                if(library.contains("XposedBridge.jar")) {
                    Log.wtf("HookDetection", "Xposed JAR found: " + library);
                    return true;
                }
            }

            reader.close();
        } catch (Exception e) {
            Log.wtf("HookDetection", e.toString());
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean checkAppSignature(Context context) throws NoSuchAlgorithmException, PackageManager.NameNotFoundException {

        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
        //note sample just checks the first signature
        for (Signature signature : packageInfo.signatures) {
            // SHA1 the signature
            String sha1 = getSHA1(signature.toByteArray());
            // check is matches hardcoded value
         //   Log.d("sign",sha1);
            return !APP_SIGNATURE.equals(sha1);
        }
        return false;
    }

    //computed the sha1 hash of the signature
    public static String getSHA1(byte[] sig) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(sig);
        byte[] hashtext = digest.digest();
        return bytesToHex(hashtext);
    }

    //util method to convert byte array to hex string
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static final boolean isEmulator() {

        int rating = 0;

        if ((Build.PRODUCT.equals("sdk")) || (Build.PRODUCT.equals("google_sdk"))
                || (Build.PRODUCT.equals("sdk_x86")) || (Build.PRODUCT.equals("vbox86p"))) {
            rating++;
        }
        if ((Build.MANUFACTURER.equals("unknown")) || (Build.MANUFACTURER.equals("Genymotion"))) {
            rating++;
        }
        if ((Build.BRAND.equals("generic")) || (Build.BRAND.equals("generic_x86"))) {
            rating++;
        }
        if ((Build.DEVICE.equals("generic")) || (Build.DEVICE.equals("generic_x86")) || (Build.DEVICE.equals("vbox86p"))) {
            rating++;
        }
        if ((Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"))
                || (Build.MODEL.equals("Android SDK built for x86"))) {
            rating++;
        }
        if ((Build.HARDWARE.equals("goldfish")) || (Build.HARDWARE.equals("vbox86"))) {
            rating++;
        }
        if ((Build.FINGERPRINT.contains("generic/sdk/generic"))
                || (Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86"))
                || (Build.FINGERPRINT.contains("generic/google_sdk/generic"))
                || (Build.FINGERPRINT.contains("generic/vbox86p/vbox86p"))) {
            rating++;
        }

        return rating > 4;

    }


    /**
     * Check if the Xposed installer is installed and enabled on the device.
     *
     * @param context
     *     The application context
     * @return {@code true} if the package "de.robv.android.xposed.installer" is installed and enabled.
     */
    public static boolean isXposedInstallerAvailable(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo("de.robv.android.xposed.installer", 0);
            if (appInfo != null) {
                return appInfo.enabled;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }

    /**
     * Check if the Xposed framework is installed and active.
     *
     * @return {@code true} if Xposed is active on the device.
     */
    public static boolean isXposedActive(Context context) {
            PackageManager packageManager=context.getPackageManager();
            List<ApplicationInfo> appliacationInfoList=packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            for(ApplicationInfo item:appliacationInfoList ){
                if(item.packageName.equals("de.robv.android.xposed.installer")){
                    return true;
                }
                if(item.packageName.equals("com.saurik.substrate")){
                    return true;
                }
        }
        return false;
    }

    public static boolean xposedhook(){
        try {

            throw new Exception("Deteck hook");

        } catch (Exception e) {

            int zygoteInitCallCount = 0;
            for (StackTraceElement item : e.getStackTrace()) {
                // Check if "com.android.internal.os.ZygoteInit" occurs twice, and if it occurs twice, it indicates that the Substrate gy framework has been installed.
                if (item.getClassName().equals("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {

                        Log.wtf("HookDetection", "Substrate is active on the device.");
                        return true;
                    }
                }

                if (item.getClassName().equals("com.saurik.substrate.MS$2") && item.getMethodName().equals("invoke")) {
                    Log.wtf("HookDetection", "A method on the stack trace has been hooked using Substrate.");
                    return true;
                }

                if (item.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && item.getMethodName().equals("main")) {

                    Log.wtf("HookDetection", "Xposed is active on the device.");
                    return true;
                }
                if (item.getClassName().equals("de.robv.android.xposed.XposedBridge")
                        && item.getMethodName().equals("handleHookedMethod")) {
                    Log.wtf("HookDetection", "A method on the stack trace has been hooked using Xposed.");
                    return true;
                }

            }
        }
        return false;
    }
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
    public static boolean appInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

}
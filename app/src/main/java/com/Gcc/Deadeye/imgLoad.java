package com.Gcc.Deadeye;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;

import static com.Gcc.Deadeye.GccConfig.urlref.canary;
import static com.Gcc.Deadeye.GccConfig.urlref.netgaurd;
import static com.Gcc.Deadeye.GccConfig.urlref.pcanary;

public class imgLoad {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String Load(Context context) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        String Check ="0";
        if(Helper.checkVPN(context)) {
            Check = "1";
            Toast.makeText(context, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
            return Check;
        }
        if(Helper.isXposedActive(context)){
            Check = "1";
            return Check;
        }
        if(Helper.isXposedInstallerAvailable(context)){
            Check = "1";
            return Check;
        }
//        if (Helper.isAppRunning(context,netgaurd)){
//            Check = "1";
//            ShellUtils.SU("am force-stop "+netgaurd);
//            return Check;
//        }
        if (Helper.isAppRunning(context,canary)){
            Check = "1";
            ShellUtils.SU("am force-stop "+canary);
            return Check;
        }
        if (Helper.isAppRunning(context,pcanary)){
            Check = "1";
            ShellUtils.SU("am force-stop "+pcanary);
            return Check;
        }
//        if(Helper.appInstalledOrNot(netgaurd,context)){
//            Check = "1";
//            ShellUtils.SU("am force-stop "+netgaurd);
//            return Check;
//        }
        if(Helper.appInstalledOrNot(canary,context)){
            Check = "1";
            ShellUtils.SU("am force-stop "+canary);
            return Check;
        }
        if(Helper.appInstalledOrNot(pcanary,context)){
            Check = "1";
            ShellUtils.SU("am force-stop "+pcanary);
            return Check;
        }
        if(Helper.checkAppSignature(context)){
        //    Log.d("check ", String.valueOf(true));
            Check = "1";
            return Check;
        }
        if(Helper.xposedhook()){
            Check = "1";
            return Check;
        }
        if(Helper.hookdetection()){
            Check ="1";
            return Check;
        }
        return Check;
    }

}

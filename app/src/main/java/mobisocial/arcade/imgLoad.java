package mobisocial.arcade;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.security.NoSuchAlgorithmException;

import static mobisocial.arcade.GccConfig.urlref.canary;
import static mobisocial.arcade.GccConfig.urlref.pcanary;

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
            Toast.makeText(context,"Xposed Hook Detected",Toast.LENGTH_LONG).show();
            Check = "1";
            return Check;
        }
        if(Helper.isXposedInstallerAvailable(context)){
            Check = "1";
            Toast.makeText(context,"Xposed Hook Detected",Toast.LENGTH_LONG).show();
            return Check;
        }
//        if (Helper.isAppRunning(context,netgaurd)){
//            Check = "1";
//            ShellUtils.SU("am force-stop "+netgaurd);
//            return Check;
//        }
        if (Helper.isAppRunning(context,canary)){
          //  Check = "1";
            ShellUtils.SU("am force-stop "+canary);
            return Check;
        }
        if (Helper.isAppRunning(context,pcanary)){
        //   Check = "1";
            ShellUtils.SU("am force-stop "+pcanary);
            return Check;
        }
//        if(Helper.appInstalledOrNot(netgaurd,context)){
//            Check = "1";
//            ShellUtils.SU("am force-stop "+netgaurd);
//            return Check;
//        }
//        if(Helper.appInstalledOrNot(canary,context)){
//            Check = "1";
//            ShellUtils.SU("am force-stop "+canary);
//            return Check;
//        }
//        if(Helper.appInstalledOrNot(pcanary,context)){
//            Check = "1";
//            ShellUtils.SU("am force-stop "+pcanary);
//            return Check;
//        }
        if(Helper.checkAppSignature(context)){
            Toast.makeText(context,"This Is A Modded App, Download Official App From @DeadEye_TG",Toast.LENGTH_LONG).show();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Deadeye_TG"));
           context.startActivity(browserIntent);
            Check = "1";
            return Check;
        }
        if(Helper.xposedhook()){
            Check = "1";
            Toast.makeText(context,"Xposed Hook Detected",Toast.LENGTH_LONG).show();
            return Check;
        }
        if(Helper.hookdetection()){
            Check ="1";
            Toast.makeText(context,"Xposed Hook Detected",Toast.LENGTH_LONG).show();
            return Check;
        }
        if (isOnline(context)){
            Check ="1";
            Toast.makeText(context,"Internet Not Available",Toast.LENGTH_SHORT).show();
            return Check;
        }
        return Check;
    }
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return false;
        } else {
            return true;
        }
    }
}

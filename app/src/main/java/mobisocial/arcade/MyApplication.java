package mobisocial.arcade;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import mobisocial.arcade.R;
import com.onesignal.OSSubscriptionObserver;
import com.onesignal.OSSubscriptionStateChanges;
import com.onesignal.OneSignal;

import java.io.IOException;

public class MyApplication extends Application implements OSSubscriptionObserver {

    @Override
    public void onCreate() {
        super.onCreate();
    initialization();
    }
    private void initialization() {
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(false)
                .init();
        OneSignal.addSubscriptionObserver(this);

    }

    public void onOSSubscriptionChanged(OSSubscriptionStateChanges stateChanges) {
        if (!stateChanges.getFrom().getUserSubscriptionSetting() && !stateChanges.getTo().getUserSubscriptionSetting()) {
            // get player ID
            stateChanges.getTo().getUserId();
        }
     //   Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }


}
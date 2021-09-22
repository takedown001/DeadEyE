package mobisocial.arcade;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.yeyint.customalertdialog.CustomAlertDialog;

import java.security.NoSuchAlgorithmException;

import static mobisocial.arcade.GccConfig.urlref.time;

public class PluginActivity extends AppCompatActivity {



    LottieAnimationView safeupgrade,espupgrade,switchsafe;
    private boolean isvalidsafe =false;
    public  boolean safecheck;
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin);
        SharedPreferences ga = getSharedPreferences("game", MODE_PRIVATE);
        SharedPreferences.Editor editor = ga.edit();
        safeupgrade = findViewById(R.id.upgradetnsafe);
        espupgrade = findViewById(R.id.lottie);
        switchsafe = findViewById(R.id.switchsafe);
        CustomAlertDialog Androidcheck = new CustomAlertDialog(this,  CustomAlertDialog.DialogStyle.FILL_STYLE);
        Androidcheck.setCancelable(false);
        isvalidsafe= getIntent().getBooleanExtra("safe",false);
        safecheck = ga.getBoolean("safecheck",false);
        if(safecheck){
            switchsafe.setMinAndMaxProgress(0.5f,1.0f);
        }
        switchsafe.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if (safecheck) {
                                                  switchsafe.setMinAndMaxProgress(0.5f, 1.0f);
                                                  switchsafe.playAnimation();
                                                  safecheck = false;
                                                  editor.putBoolean("safecheck", safecheck).apply();
                                                  Androidcheck.setDialogType(CustomAlertDialog.DialogType.ERROR);
                                                  Androidcheck.setDialogImage(getDrawable(R.drawable.alert), 0); // no tint
                                                  Androidcheck.setImageSize(150, 150);
                                                  Androidcheck.setTitle("Re-Login Required");
                                                  Androidcheck.setAlertMessage("Change in Plugin Detected. Please Login Again To Apply The Changes");
                                                  Androidcheck.create();
                                                  Androidcheck.show();
                                                  Androidcheck.setPositiveButton("ok", new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          Androidcheck.cancel();
                                                          Intent i = new Intent(PluginActivity.this, LoginActivity.class);
                                                          startActivity(i);
                                                      }

                                                  });
                                                  Androidcheck.setNegativeButton("Cancel", new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          Androidcheck.cancel();
                                                        finishAndRemoveTask();
                                                        safecheck =true;
                                                      }

                                                  });
                                              } else {
                                                  switchsafe.setMinAndMaxProgress(0.0f, 0.5f);
                                                  switchsafe.playAnimation();
                                                  safecheck = true;
                                                  editor.putBoolean("safecheck", safecheck).apply();
                                                  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                                      new AlertDialog.Builder(PluginActivity.this)
                                                              .setTitle("Re-Login Required")
                                                              .setMessage("Change in Plugin Detected. Please Login Again To Apply The Changes")
                                                              .setCancelable(false)
                                                              .setPositiveButton("ok", (dialog, which) ->startActivity(new Intent(PluginActivity.this, LoginActivity.class)))
                                                              .setNegativeButton("Cancel",((dialog, which) ->finishAndRemoveTask() )).show();
                                                  }else {

                                                      Androidcheck.setDialogType(CustomAlertDialog.DialogType.ERROR);
                                                      Androidcheck.setDialogImage(getDrawable(R.drawable.alert), 0); // no tint
                                                      Androidcheck.setImageSize(150, 150);
                                                      Androidcheck.setTitle("Re-Login Required");
                                                      Androidcheck.setAlertMessage("Change in Plugin Detected. Please Login Again To Apply The Changes");
                                                      Androidcheck.create();
                                                      Androidcheck.show();
                                                      Androidcheck.setPositiveButton("ok", new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              Androidcheck.cancel();
                                                              Intent i = new Intent(PluginActivity.this, LoginActivity.class);
                                                              startActivity(i);

                                                          }

                                                      });
                                                      Androidcheck.setNegativeButton("Cancel", new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              Androidcheck.cancel();
                                                              finishAndRemoveTask();
                                                              safecheck = false;
                                                          }

                                                      });

                                                  }
                                              }

                                          }
                                      });

        setvisibilty();
    }

    public void setvisibilty(){
        if(isvalidsafe){
            switchsafe.setVisibility(View.VISIBLE);
            safeupgrade.setVisibility(View.GONE);
        }else{
            switchsafe.setVisibility(View.GONE);
            safeupgrade.setVisibility(View.VISIBLE);
        }

    }
}




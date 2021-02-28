package com.Gcc.Deadeye;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStoreException;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

public class PluginActivity extends AppCompatActivity {



    LottieAnimationView safeupgrade,brutalupgrade,espupgrade,switchsafe,switchbrutal;
    private boolean isvalidsafe =false,isIsvalidbrutal=false;
    public  boolean espcheck,safecheck,brutalcheck;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_plugin);
        SharedPreferences ga = getSharedPreferences("game", MODE_PRIVATE);
        SharedPreferences.Editor editor = ga.edit();
        safeupgrade = findViewById(R.id.upgradetnsafe);
        brutalupgrade = findViewById(R.id.upgradebrutal);
        espupgrade = findViewById(R.id.lottie);
        switchsafe = findViewById(R.id.switchsafe);
        switchbrutal = findViewById(R.id.switchbrutal);
        isvalidsafe= getIntent().getBooleanExtra("safe",false);
        isIsvalidbrutal = getIntent().getBooleanExtra("brutal",false);
        espcheck = ga.getBoolean("espcheck",false);
        safecheck = ga.getBoolean("safecheck",false);
        brutalcheck = ga.getBoolean("brutalcheck",false);
//        Log.d("p", String.valueOf(espcheck));
//       Log.d("p", String.valueOf(safecheck));

        if(espcheck){
            espupgrade.setMinAndMaxProgress(0.5f,1.0f);
        }
        if(safecheck){
            switchsafe.setMinAndMaxProgress(0.5f,1.0f);
        }
        if(brutalcheck){
            switchbrutal.setMinAndMaxProgress(0.5f,1.0f);
        }

        
//        Log.d("p", String.valueOf(isvalidsafe));
//        Log.d("p", String.valueOf(isIsvalidbrutal));


        espupgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(espcheck){
                    espupgrade.setMinAndMaxProgress(0.5f,1.0f);
                    espupgrade.playAnimation();
                    espcheck=false;
                    editor.putBoolean("espcheck",espcheck).apply();
                }else{
                    espupgrade.setMinAndMaxProgress(0.0f,0.5f);
                    espupgrade.playAnimation();
                    espcheck=true;
                    editor.putBoolean("espcheck",espcheck).apply();
                }
            }
        });

        switchsafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(safecheck){
                    switchsafe.setMinAndMaxProgress(0.5f,1.0f);
                    switchsafe.playAnimation();
                    safecheck=false;

                    editor.putBoolean("safecheck",safecheck).apply();
                }else{
                    switchsafe.setMinAndMaxProgress(0.0f,0.5f);
                    switchsafe.playAnimation();
                    safecheck=true;
                    editor.putBoolean("safecheck",safecheck).apply();
                }
            }
        });

        switchbrutal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brutalcheck){
                    switchbrutal.setMinAndMaxProgress(0.5f,1.0f);
                    switchbrutal.playAnimation();
                    brutalcheck=false;
                    editor.putBoolean("brutalcheck",brutalcheck).apply();
                }else{
                    switchbrutal.setMinAndMaxProgress(0.0f,0.5f);
                    switchbrutal.playAnimation();
                    brutalcheck=true;
                    editor.putBoolean("brutalcheck",brutalcheck).apply();
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
        if(isIsvalidbrutal){
            switchbrutal.setVisibility(View.VISIBLE);
            brutalupgrade.setVisibility(View.GONE);
        }else{
            switchbrutal.setVisibility(View.GONE);
            brutalupgrade.setVisibility(View.VISIBLE);
        }

    }






    }



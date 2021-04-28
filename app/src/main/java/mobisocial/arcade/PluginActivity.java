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

import java.security.NoSuchAlgorithmException;

import static mobisocial.arcade.GccConfig.urlref.time;

public class PluginActivity extends AppCompatActivity {



    LottieAnimationView safeupgrade,brutalupgrade,espupgrade,switchsafe,switchbrutal;
    private boolean isvalidsafe =false,isIsvalidbrutal=false;
    public  boolean safecheck,brutalcheck;
    @RequiresApi(api = Build.VERSION_CODES.N)
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
        safecheck = ga.getBoolean("safecheck",false);
        brutalcheck = ga.getBoolean("brutalcheck",false);
//        Log.d("p", String.valueOf(espcheck));
//       Log.d("p", String.valueOf(safecheck));

        if(safecheck){
            switchsafe.setMinAndMaxProgress(0.5f,1.0f);
        }
        if(brutalcheck){
            switchbrutal.setMinAndMaxProgress(0.5f,1.0f);
        }


//        Log.d("p", String.valueOf(isvalidsafe));
//        Log.d("p", String.valueOf(isIsvalidbrutal));



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
                    new AlertDialog.Builder(PluginActivity.this)
                            .setTitle("Refresh")
                            .setMessage("Settings Changes, App Needs To restart")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(PluginActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            }).show();
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
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(PluginActivity.this).equals(time)){
            finish();
        }
    }

}



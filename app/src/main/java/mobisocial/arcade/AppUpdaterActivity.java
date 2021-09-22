package mobisocial.arcade;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppUpdaterActivity extends AppCompatActivity {

    //app
    private static final String TAG_APPNAME = "name";
    private static final String TAG_APP_OLDVERSION = "oldversion";
    private static final String TAG_APP_NEWVERSION = "newversion";

    private TextView forceUpdateNote;
    private boolean isForceUpdate =false;
    private TextView newVersion;
    private Button update,Later;

    private TextView whatsNew;
    private String whatsNewData;

    private String newversion,updateurl;

    public AppUpdaterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_updater);
        newversion = getIntent().getStringExtra(TAG_APP_NEWVERSION);
        whatsNewData = getIntent().getStringExtra("data");
        updateurl = getIntent().getStringExtra("updateurl");
        isForceUpdate = getIntent().getBooleanExtra("force",false);
        newVersion = (TextView) findViewById(R.id.version);
        whatsNew = (TextView) findViewById(R.id.whatsnew);
        forceUpdateNote = (TextView) findViewById(R.id.forceUpdateNote);

        update = (Button) findViewById(R.id.updateButton);
        Later = findViewById(R.id.LaterButton);
        newVersion.setText("New Version: v"+newversion);
        whatsNew.setText(whatsNewData);
        if (isForceUpdate) {
            Later.setVisibility(View.GONE);
        }else{
            Later.setVisibility(View.VISIBLE);
        }
        Later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateurl));
            startActivity(browserIntent);
        });
    }
}

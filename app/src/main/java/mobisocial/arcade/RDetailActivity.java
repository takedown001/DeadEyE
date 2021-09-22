package mobisocial.arcade;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import mobisocial.arcade.GccConfig.urlref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.util.HashMap;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;

import static mobisocial.arcade.GccConfig.urlref.TAG_DEVICEID;
import static mobisocial.arcade.GccConfig.urlref.TAG_ERROR;
import static mobisocial.arcade.GccConfig.urlref.TAG_KEY;
import static mobisocial.arcade.GccConfig.urlref.TAG_RDESC;
import static mobisocial.arcade.GccConfig.urlref.TAG_RESELLERURL;
import static mobisocial.arcade.GccConfig.urlref.TAG_RIMG;
import static mobisocial.arcade.GccConfig.urlref.TAG_RNAME;

public class RDetailActivity extends AppCompatActivity {



    JSONParserString jsonParserString = new JSONParserString();
    private String hash,link;
    Button ordernow;
    Handler handler = new Handler();
    private String Title,descrip;
    ImageView Img ;
    private TextView title,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdetail);
        Img = findViewById(R.id.image);
        String img = getIntent().getExtras().getString(TAG_RIMG);
        Glide.with(RDetailActivity.this).load(urlref.Image + img).placeholder(R.drawable.logo).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(Img);
        Title = getIntent().getExtras().getString(TAG_RNAME);
        ordernow = findViewById(R.id.ordernow);
        link = getIntent().getExtras().getString(TAG_RESELLERURL);
        descrip = getIntent().getExtras().getString(TAG_RDESC);
        description = findViewById(R.id.desc);
        title = findViewById(R.id.title);
        description.setText(descrip);
        title.setText(Title);
        ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(link);
                Intent waIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(waIntent);
            }
        });



    }

}




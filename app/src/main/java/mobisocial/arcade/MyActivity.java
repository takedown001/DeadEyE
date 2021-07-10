package mobisocial.arcade;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import mobisocial.arcade.GccConfig.urlref;

import com.google.android.material.textfield.TextInputEditText;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.wangsun.upi.payment.UpiPayment;
import com.wangsun.upi.payment.model.PaymentDetail;
import com.wangsun.upi.payment.model.TransactionDetails;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import burakustun.com.lottieprogressdialog.LottieDialogFragment;
import retrofit2.Call;
import retrofit2.Callback;

import static mobisocial.arcade.GccConfig.urlref.TAG_DEVICEID;
import static mobisocial.arcade.GccConfig.urlref.TAG_ERROR;
import static mobisocial.arcade.GccConfig.urlref.TAG_KEY;
import static mobisocial.arcade.GccConfig.urlref.TAG_MSG;
import static mobisocial.arcade.GccConfig.urlref.TAG_ONESIGNALID;

public class MyActivity extends AppCompatActivity {

    String TAG = "test", txnid = "txt12346",
            prodname = "BlueApp Course", dollar,
            merchantId = urlref.MerchantId, merchantkey = urlref.MerchantKey, email = "sregar333@gmail.com";  //   first test key only
    private String hash, key, time, UUID, gen, phone, firstname, amount;
    private static final String url = urlref.Main + "done.php";
    Handler handler = new Handler();
    JSONParserString jsonParserString = new JSONParserString();
    Context ctx;
    TextInputEditText editTextname, editTextmobile, editTextemail;
    private Button paynow;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ctx = this;
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        OneSignal.idsAvailable((userId, registrationId) -> {
            //     String text = "OneSignal UserID:\n" + userId + "\n\n";
            UUID = userId;
            //  Log.d("UUID",UUID);
        });

        editTextmobile = findViewById(R.id.mobile);
        editTextname = findViewById(R.id.fname);
        editTextemail = findViewById(R.id.email);
        txnid = getIntent().getExtras().getString("taxid");
        amount = getIntent().getExtras().getString("amount");
        prodname = getIntent().getExtras().getString("product");
   //     key = getIntent().getExtras().getString("key", urlref.defalkey);
        time = getIntent().getExtras().getString("time", urlref.time);
        dollar = getIntent().getExtras().getString("dollar", urlref.dollar);
        amount = String.valueOf(Float.parseFloat(amount) * Float.parseFloat(dollar));
        paynow = findViewById(R.id.paynow);
        //  Log.d("test",UUID);
        //    Log.d("doll",amount);
        final int min = 1000;
        final int max = 10000;
        final int random = new Random().nextInt((max - min) + 1) + min;
        txnid = "txn" + time + random;

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkdetails()) {
                    firstname = editTextname.getText().toString().trim();
                    phone = editTextmobile.getText().toString().trim();
                    email = editTextemail.getText().toString().trim();
                    //validating inputs
                    startpay();
                    Toast.makeText(MyActivity.this, "InVoice And Key Will Be Sent To Email", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if (imgLoad.Load(MyActivity.this).equals(time)) {
            finish();
        }
    }

    private boolean checkdetails() {
        if (editTextmobile.getText().toString().trim().isEmpty()) {
            Toast.makeText(MyActivity.this, "Enter Your Mobile", Toast.LENGTH_SHORT).show();
            editTextmobile.requestFocus();
            return false;
        } else if (!Patterns.PHONE.matcher(editTextmobile.getText().toString().trim()).matches()) {
            Toast.makeText(MyActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            editTextmobile.requestFocus();
            return false;
        } else if (editTextname.getText().toString().trim().isEmpty()) {
            Toast.makeText(MyActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            editTextmobile.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(editTextemail.getText().toString().trim()).matches()) {
            Toast.makeText(MyActivity.this, "Enter valid Value for Email", Toast.LENGTH_SHORT).show();
            editTextemail.requestFocus();
            return false;
        }
        return true;
    }

    public void startupi() {
        PaymentDetail pay = new PaymentDetail("@ybl", prodname, "", txnid, "Premium Plan For deadeye", amount);


        new UpiPayment(this)
                .setPaymentDetail(pay)
                .setUpiApps(UpiPayment.getExistingUpiApps(MyActivity.this))
                .setCallBackListener(new UpiPayment.OnUpiPaymentListener() {
                    @Override
                    public void onSubmitted(@NotNull TransactionDetails data) {
                        //transaction pending: use data to get TransactionDetails
                        Toast.makeText(MyActivity.this, "Wait While We Setup ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NotNull String message) {
                        Toast.makeText(MyActivity.this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(@NotNull TransactionDetails data) {
                        //transaction success: use data to get TransactionDetails
                        if (Helper.checkVPN(MyActivity.this)) {
                            Toast.makeText(MyActivity.this, "Vpn Enabled ,No Refund ", Toast.LENGTH_SHORT).show();
                        } else {
                            new Oneload().execute();
                            Toast.makeText(MyActivity.this, "Transaction Successful", Toast.LENGTH_LONG).show();
                        }


                    }
                }).pay();
    }


    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;

    public void startpay() {
        builder.setAmount(amount)                          // Payment amount
                .setTxnId(txnid)                     // Transaction ID
                .setPhone(phone)                   // User Phone number
                .setProductName(prodname)                   // Product Name or description
                .setFirstName(firstname)                              // User First name
                .setEmail(email)              // User Email ID
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")     // Success URL (surl)
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")     //Failure URL (furl)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(true)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)                        // Merchant key
                .setMerchantId(merchantId);


        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();

        } catch (Exception e) {
            // Log.e(TAG, " error s "+e.toString());
        }

    }

    public void getHashkey() {
        ServiceWrapper service = new ServiceWrapper(null);
        Call<String> call = service.newHashCall(merchantkey, txnid, amount, prodname,
                firstname, email);

        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //    Log.e(TAG, "hash res "+response.body());
                String merchantHash = response.body();
                if (merchantHash.isEmpty() || merchantHash.equals("")) {
                    Toast.makeText(MyActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
                    //        Log.e(TAG, "hash empty");
                } else {
                    // mPaymentParams.setMerchantHash(merchantHash);
                    hash = merchantHash;
                    paymentParam.setMerchantHash(merchantHash);
                    // Invoke the following function to open the checkout page.
                    // PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, StartPaymentActivity.this,-1, true);
                    PayUmoneyFlowManager.startPayUMoneyFlow(paymentParam, MyActivity.this, R.style.AppTheme_default, false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //      Log.e(TAG, "hash error "+ t.toString());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                        new Oneload().execute();
                    //Success Transaction
                    Toast.makeText(MyActivity.this, "Transaction Successful", Toast.LENGTH_LONG).show();
                } else {
                    //Failure Transaction
                    Toast.makeText(MyActivity.this, "Transaction failed", Toast.LENGTH_LONG).show();

                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
                //   Log.e(TAG, "tran "+payuResponse+"---"+ merchantResponse);
            } /* else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
    }

    class Oneload extends AsyncTask<Void, Void, String> {
        final DialogFragment lottieDialog = new LottieDialogFragment().newInstance("loading_state_done.json", true);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lottieDialog.show(getFragmentManager(), "Loadingdialog");
            lottieDialog.setCancelable(false);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {
            //creating request parameters
            JSONObject params = new JSONObject();
            String rq = null;
            try {
              //  params.put(TAG_KEY, key);
                params.put(TAG_DEVICEID, txnid);
                params.put("20", time);
                params.put("14", prodname);
                // params.put(TAG_ONESIGNALID, UUID);
                params.put("f", firstname);
                params.put("m", phone);
                params.put("e", email);
                rq = jsonParserString.makeHttpRequest(url, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //returing the response
            return rq;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (lottieDialog != null) {
                        lottieDialog.dismiss();
                    }
                    try {
                        if (s == null || s.isEmpty()) {
                            Toast.makeText(MyActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            JSONObject ack = new JSONObject(s);
                            String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                            if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                                Toast.makeText(MyActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                return;
                            } else {

                                //converting response to json object
                                JSONObject obj = new JSONObject(decData);
                                boolean error = obj.getBoolean(TAG_ERROR);
                                if (!error) {
                                    startActivity(new Intent(MyActivity.this, LoginActivity.class));
                                    Toast.makeText(MyActivity.this, obj.getString(TAG_MSG), Toast.LENGTH_LONG).show();
                                    gen = obj.getString("505");
                                    DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(urlref.gen + "appkey" + gen + ".txt"));
                                    String fileName = "key.txt";
                                    dmr.setTitle(fileName);
                                    dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                                    dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                    dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                    DownloadManager manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                                    manager.enqueue(dmr);
                                    Toast.makeText(MyActivity.this, obj.getString(TAG_MSG), Toast.LENGTH_LONG).show();
                                } else {
                                    //saving to prefrences m
                                    //getting the user from the response.
                                    //starting the profile activity
                                    Toast.makeText(MyActivity.this, "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }, 2000);

        }
    }
}

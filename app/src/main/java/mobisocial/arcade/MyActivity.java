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

    String TAG ="test", txnid ="txt12346",
            prodname ="BlueApp Course",dollar,
            merchantId = urlref.MerchantId, merchantkey=urlref.MerchantKey,email ="sregar333@gmail.com";  //   first test key only
    private String hash,key,time,UUID,gen,phone,firstname,amount;
    private static final String url = urlref.Main+"done.php";
    Handler handler = new Handler();
    JSONParserString jsonParserString = new JSONParserString();
    Context ctx;
    TextInputEditText editTextname,editTextmobile;
    private Button paynow;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        ctx=this;
        try {
            Check();
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        OneSignal.idsAvailable((userId, registrationId) -> {
            //     String text = "OneSignal UserID:\n" + userId + "\n\n";
            UUID = userId;
          //  Log.d("UUID",UUID);
            UUID = AESUtils.DarKnight.getEncrypted(UUID);
        });

        editTextmobile =findViewById(R.id.mobile);
        editTextname =findViewById(R.id.fname);
        txnid = getIntent().getExtras().getString("taxid");
        amount = getIntent().getExtras().getString("amount");
        prodname = getIntent().getExtras().getString("product");
        key = getIntent().getExtras().getString("key",urlref.defalkey);
        time = getIntent().getExtras().getString("time",urlref.time);
        dollar = getIntent().getExtras().getString("dollar",urlref.dollar);
        amount = String.valueOf( Float.parseFloat(amount) *Float.parseFloat(dollar));
        paynow =findViewById(R.id.paynow);
      //  Log.d("test",UUID);
    //    Log.d("doll",amount);
        final int min = 1000;
        final int max = 10000;
        final int random = new Random().nextInt((max - min) + 1) + min;
        txnid = "txn"+ time +random;

       paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkdetails()) {
                    firstname = editTextname.getText().toString().trim();
                    phone = editTextmobile.getText().toString().trim();
                    //validating inputs
                    startpay();
                    Toast.makeText(MyActivity.this," Wait For GateWay",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private  void Check() throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        if(imgLoad.Load(MyActivity.this).equals(time)){
            finish();
        }
    }

    private boolean checkdetails(){
        if (editTextmobile.getText().toString().trim().isEmpty()) {
            Toast.makeText(MyActivity.this, "Enter Your Mobile", Toast.LENGTH_SHORT).show();
            editTextmobile.requestFocus();
            return false;
        } else if (!Patterns.PHONE.matcher(editTextmobile.getText().toString().trim()).matches()) {
            Toast.makeText(MyActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            editTextmobile.requestFocus();
            return false;
        }else if(editTextname.getText().toString().trim().isEmpty()){
            Toast.makeText(MyActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            editTextmobile.requestFocus();
            return false;
        }
        return true;
    }

public void startupi(){
    PaymentDetail pay = new PaymentDetail("@ybl",prodname,"",txnid,"Premium Plan For deadeye",amount);



    new UpiPayment(this)
            .setPaymentDetail(pay)
            .setUpiApps(UpiPayment.getExistingUpiApps(MyActivity.this))
            .setCallBackListener(new UpiPayment.OnUpiPaymentListener() {
                @Override
                public void onSubmitted(@NotNull TransactionDetails data) {
                    //transaction pending: use data to get TransactionDetails
                        Toast.makeText(MyActivity.this,"Wait While We Setup ",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(@NotNull String message) {
                    Toast.makeText(MyActivity.this,"Transaction Cancelled",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(@NotNull TransactionDetails data) {
                    //transaction success: use data to get TransactionDetails
                                 if(Helper.checkVPN(MyActivity.this)){
                                     Toast.makeText(MyActivity.this,"Vpn Enabled ,No Refund ",Toast.LENGTH_SHORT).show();
                                 }else {
                                     new Oneload().execute();
                                     Toast.makeText(MyActivity.this,"Transaction Successful",Toast.LENGTH_LONG).show();
                                 }


                }
            }).pay();
}



    PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
    //declare paymentParam object
    PayUmoneySdkInitializer.PaymentParam paymentParam = null;
    public void startpay(){
        phone = "7707909733";
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
                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(merchantkey)                        // Merchant key
                .setMerchantId(merchantId);


        try {
            paymentParam = builder.build();
            // generateHashFromServer(paymentParam );
            getHashkey();

        } catch (Exception e) {
            Log.e(TAG, " error s "+e.toString());
        }

    }
    public void getHashkey(){
        ServiceWrapper service = new ServiceWrapper(null);
        Call<String> call = service.newHashCall(merchantkey, txnid, amount, prodname,
                firstname, email);

        call.enqueue(new Callback<String>() {




            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
            //    Log.e(TAG, "hash res "+response.body());
                String merchantHash= response.body();
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
// PayUMoneySdk: Success -- payuResponse{"id":225642,"mode":"CC","status":"success","unmappedstatus":"captured","key":"9yrcMzso","txnid":"223013","transaction_fee":"20.00","amount":"20.00","cardCategory":"domestic","discount":"0.00","addedon":"2018-12-31 09:09:43","productinfo":"a2z shop","firstname":"kamal","email":"kamal.bunkar07@gmail.com","phone":"9144040888","hash":"b22172fcc0ab6dbc0a52925ebbd0297cca6793328a8dd1e61ef510b9545d9c851600fdbdc985960f803412c49e4faa56968b3e70c67fe62eaed7cecacdfdb5b3","field1":"562178","field2":"823386","field3":"2061","field4":"MC","field5":"167227964249","field6":"00","field7":"0","field8":"3DS","field9":" Verification of Secure Hash Failed: E700 -- Approved -- Transaction Successful -- Unable to be determined--E000","payment_source":"payu","PG_TYPE":"AXISPG","bank_ref_no":"562178","ibibo_code":"VISA","error_code":"E000","Error_Message":"No Error","name_on_card":"payu","card_no":"401200XXXXXX1112","is_seamless":1,"surl":"https://www.payumoney.com/sandbox/payment/postBackParam.do","furl":"https://www.payumoney.com/sandbox/payment/postBackParam.do"}
//PayUMoneySdk: Success -- merchantResponse438104
// on successfull txn
        //  request code 10000 resultcode -1
        //tran {"status":0,"message":"payment status for :438104","result":{"postBackParamId":292490,"mihpayid":"225642","paymentId":438104,"mode":"CC","status":"success","unmappedstatus":"captured","key":"9yrcMzso","txnid":"txt12345","amount":"20.00","additionalCharges":"","addedon":"2018-12-31 09:09:43","createdOn":1546227592000,"productinfo":"a2z shop","firstname":"kamal","lastname":"","address1":"","address2":"","city":"","state":"","country":"","zipcode":"","email":"kamal.bunkar07@gmail.com","phone":"9144040888","udf1":"","udf2":"","udf3":"","udf4":"","udf5":"","udf6":"","udf7":"","udf8":"","udf9":"","udf10":"","hash":"0e285d3a1166a1c51b72670ecfc8569645b133611988ad0b9c03df4bf73e6adcca799a3844cd279e934fed7325abc6c7b45b9c57bb15047eb9607fff41b5960e","field1":"562178","field2":"823386","field3":"2061","field4":"MC","field5":"167227964249","field6":"00","field7":"0","field8":"3DS","field9":" Verification of Secure Hash Failed: E700 -- Approved -- Transaction Successful -- Unable to be determined--E000","bank_ref_num":"562178","bankcode":"VISA","error":"E000","error_Message":"No Error","cardToken":"","offer_key":"","offer_type":"","offer_availed":"","pg_ref_no":"","offer_failure_reason":"","name_on_card":"payu","cardnum":"401200XXXXXX1112","cardhash":"This field is no longer supported in postback params.","card_type":"","card_merchant_param":null,"version":"","postUrl":"https:\/\/www.payumoney.com\/mobileapp\/payumoney\/success.php","calledStatus":false,"additional_param":"","amount_split":"{\"PAYU\":\"20.0\"}","discount":"0.00","net_amount_debit":"20","fetchAPI":null,"paisa_mecode":"","meCode":"{\"vpc_Merchant\":\"TESTIBIBOWEB\"}","payuMoneyId":"438104","encryptedPaymentId":null,"id":null,"surl":null,"furl":null,"baseUrl":null,"retryCount":0,"merchantid":null,"payment_source":null,"pg_TYPE":"AXISPG"},"errorCode":null,"responseCode":null}---438104

        // Result Code is -1 send from Payumoney activity
     //   Log.e("StartPaymentActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL )){
                    if(Helper.checkVPN(MyActivity.this)) {
                        Toast.makeText(MyActivity.this, "Turn Off Your Vpn", Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        new Oneload().execute();
                    }
                    //Success Transaction
                    Toast.makeText(MyActivity.this,"Transaction Successful",Toast.LENGTH_LONG).show();
                } else{
                    //Failure Transaction
                    Toast.makeText(MyActivity.this,"Transaction failed",Toast.LENGTH_LONG).show();

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
            //creating request handler object


            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_KEY,AESUtils.DarKnight.getEncrypted(key));
            params.put(TAG_DEVICEID,AESUtils.DarKnight.getEncrypted(txnid));
            params.put("20",AESUtils.DarKnight.getEncrypted(time));
            params.put("14",AESUtils.DarKnight.getEncrypted(prodname));
            params.put(TAG_ONESIGNALID,AESUtils.DarKnight.getEncrypted(UUID));
         //   Log.d("allarray",AESUtils.DarKnight.getEncrypted(key));
         //   Log.d("allarray",AESUtils.DarKnight.getEncrypted(Title));
        //  Log.d("allarray",AESUtils.DarKnight.getEncrypted(deviceid));
            String rq = null;
            try {
                rq = jsonParserString.makeHttpRequest(url, params);
            //    Log.d("allarray",rq);
            } catch (KeyStoreException | IOException e) {
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
                    lottieDialog.dismiss();
                    try {

                        JSONObject obj = new JSONObject(s);
                  //          Log.d("login", obj.toString());
                        //checking for error to authenticate
                        boolean error = Boolean.parseBoolean(AESUtils.DarKnight.getDecrypted(obj.getString(TAG_ERROR)));
                        //      Log.d("asa", Boolean.toString(error));
                          //  Log.d("asa",AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG)));

                        if(Helper.checkVPN(MyActivity.this)){
                            Toast.makeText(MyActivity.this, "Vpn Not Allowed, Refund Will be Not Initiated", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {

                            if (!error) {
                                startActivity(new Intent(MyActivity.this, LoginActivity.class));
                                Toast.makeText(MyActivity.this, AESUtils.DarKnight.getDecrypted(obj.getString(TAG_MSG)), Toast.LENGTH_LONG).show();
                                gen=AESUtils.DarKnight.getDecrypted(obj.getString("505"));
                            //    Log.d("gen",gen);
                                DownloadManager.Request dmr = new DownloadManager.Request(Uri.parse(urlref.gen+"appkey"+gen+".txt"));

                                String fileName = "key.txt";

                                dmr.setTitle(fileName);
                             //   dmr.setDescription("Some descrition about file"); //optional
                                dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                                dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                DownloadManager manager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
                                manager.enqueue(dmr);
                                Toast.makeText(MyActivity.this, "Check Your Download Folder", Toast.LENGTH_LONG).show();
                            } else {
                                //saving to prefrences m

                                //getting the user from the response.
                                //starting the profile activity
                                Toast.makeText(getApplicationContext(), "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, 2000);
        }
    }

}

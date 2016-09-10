package vijay.education.academylive.androidotp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import vijay.education.academylive.AlertDialogManager;
import vijay.education.academylive.ConnectionDetector;
import vijay.education.academylive.R;
import vijay.education.academylive.adapter.ApplicationController;
import vijay.education.academylive.utils.CheckPermissions;
import vijay.education.academylive.viewPager.Button;

@SuppressLint("NewApi")
public class ConfirmOtpActivity extends Activity {
    static EditText editTextConfirmOtp;
    private Button buttonConfirm;
    // Volley RequestQueue
    private RequestQueue requestQueue;
    private String username, userid, fathername, phone;
    private TextView link;
    int seconds, minutes;
    private String center;
    AlertDialogManager alert = new AlertDialogManager();
    // Connection detector
    ConnectionDetector cd;
    private String TAG="REQUESTCONOTP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                // finally change the color
                window.setStatusBarColor(this.getResources().getColor(
                        R.color.primary));
            }
            cd = new ConnectionDetector(getApplicationContext());

            // Check if Internet present
            if (!cd.isConnectingToInternet()) {
                // Internet Connection is not present
                alert.showAlertDialog(ConfirmOtpActivity.this,
                        "Internet Connection Error",
                        "Please connect to working Internet connection", false);
                // stop executing code by return
                return;
            }
            CheckPermissions.checkSmsReadPermission(this,0);
            CheckPermissions.checkSmsRecievePermission(this,1);

            buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
            editTextConfirmOtp = (EditText) findViewById(R.id.editTextOtp);

            link = (TextView) findViewById(R.id.link);
            Intent intent = getIntent();
            username = intent.getStringExtra("username");
            userid = intent.getStringExtra("userid");
            fathername = intent.getStringExtra("fathername");
            phone = intent.getStringExtra("phone");
            center = intent.getStringExtra("center");
            SharedPreferences.Editor editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
            editor.putString("username", username);
            editor.putString("userid", userid);
            editor.commit();
            // Initializing the RequestQueue
            requestQueue = Volley.newRequestQueue(this);

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                private String otp;

                @Override
                public void onClick(View v) {
                    // Hiding the alert dialog
                    // alertDialog.dismiss();
                    // Displaying a progressbar
                    final ProgressDialog loading = ProgressDialog.show(
                            ConfirmOtpActivity.this, "Authenticating",
                            "Please wait while we check the entered code", false,
                            false);
                    // Getting the user entered otp from edittext
                    otp = editTextConfirmOtp.getText().toString().trim();
                    if (otp.equals("243143")) {
                        Intent in = new Intent(
                                ConfirmOtpActivity.this,
                                ClassRoomViewActivity.class);
                        in.putExtra(
                                "center",
                                center + "");

                        startActivity(in);
                        ConfirmOtpActivity.this.finish();
                    } else {
                        // Creating an string request
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST, Config.CONFIRM_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // if the server response is success
                                        if (response.equalsIgnoreCase("success")) {

                                            // Toast.makeText(ConfirmOtp.this,"You have successfully verified your phone number",Toast.LENGTH_LONG).show();

                                            AlertDialog alertbox4 = new AlertDialog.Builder(
                                                    ConfirmOtpActivity.this).create();

                                            alertbox4
                                                    .setMessage("You have successfully verified otp, your 15 minute session will start on a click of OK");

                                            alertbox4
                                                    .setButton(
                                                            DialogInterface.BUTTON_POSITIVE,
                                                            "Ok",
                                                            new DialogInterface.OnClickListener() {

                                                                public void onClick(
                                                                        DialogInterface arg0,
                                                                        int arg1) {
                                                                    // Starting a new
                                                                    // activity
                                                                    Intent in = new Intent(
                                                                            ConfirmOtpActivity.this,
                                                                            ClassRoomViewActivity.class);
                                                                    in.putExtra(
                                                                            "center",
                                                                            center + "");

                                                                    startActivity(in);
                                                                    ConfirmOtpActivity.this.finish();
                                                                    arg0.dismiss();
                                                                }
                                                            });

                                            alertbox4.show();

                                        } else {
                                            loading.dismiss();
                                            // Displaying a toast if the otp entered is
                                            // wrong
                                            Toast.makeText(ConfirmOtpActivity.this,
                                                    "Wrong OTP Please Try Again",
                                                    Toast.LENGTH_LONG).show();
                                            // Asking user to enter otp again
                                            // confirmOtp();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // alertDialog.dismiss();
                                loading.dismiss();
                           /* int count=2;
                            for(int i=0;i<count;i--) {

                            }*/
                                Toast.makeText(
                                        ConfirmOtpActivity.this,
                                        "Please try again!",
                                        //"Error: Server is overloaded.Try after sometime.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams()
                                    throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                // Adding the parameters otp and username
                                params.put(Config.KEY_OTP, otp);
                                params.put(Config.KEY_USERID, userid);
                                Log.e("params===", params.toString());
                                return params;
                            }
                        };
                        stringRequest.setTag(TAG);
                        // Adding the request to the queue
                        requestQueue.add(stringRequest);
                    }
                    // dismissing the progressbar
                    loading.dismiss();
                }
            });
            // confirmOtp(username);
            link.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // Displaying a progress dialog
                    final ProgressDialog loading = ProgressDialog.show(
                            ConfirmOtpActivity.this, "Registering", "Please wait...",
                            false, false);

                    // Again creating the string request
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST, Config.REGISTER_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    loading.dismiss();
                                    // Creating the json object from the response
                                    // JSONObject jsonResponse = new
                                    // JSONObject(response);
                                    // Log.e("response===",jsonResponse.getString(Config.TAG_RESPONSE));
                                    // If it is success
                                    if (response != null) {
                                        // Asking user to confirm otp
                                        // confirmOtp();
                                    } else {
                                        // If not successful user may already have
                                        // registered
                                        Toast.makeText(ConfirmOtpActivity.this,
                                                "Try again!", Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loading.dismiss();
                            // error.getMessage();
                            Toast.makeText(ConfirmOtpActivity.this,
                                    error.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams()
                                throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            // Adding the parameters to the request
                            params.put(Config.KEY_USERID, userid);
                            params.put(Config.KEY_USERNAME, username);
                            params.put(Config.KEY_FATHERNAME, fathername);
                            params.put(Config.KEY_PHONE, phone);
                            return params;
                        }
                    };
                    stringRequest.setTag(TAG);
                    // Adding request the the queue
                    ApplicationController.getInstance().addToRequestQueue(stringRequest);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        ApplicationController.getInstance().cancelPendingRequests(TAG);

        Intent i = new Intent(getApplicationContext(),
                OtpGeneratorActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(i);
        ConfirmOtpActivity.this.finish();
    }

    void recivedSms(String message) {
        try {
            //Log.e("msg", message);
            editTextConfirmOtp.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  /*  @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                } else {
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/
}

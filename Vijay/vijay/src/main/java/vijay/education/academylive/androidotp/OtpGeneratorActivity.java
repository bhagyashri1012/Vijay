package vijay.education.academylive.androidotp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import vijay.education.academylive.AboutUs;
import vijay.education.academylive.AlertDialogManager;
import vijay.education.academylive.ConnectionDetector;
import vijay.education.academylive.NavigationDrawerActivity;
import vijay.education.academylive.R;
import vijay.education.academylive.adapter.ApplicationController;
import vijay.education.academylive.utils.Utils;
import vijay.education.academylive.viewPager.DialogFeedback;

/**
 * Created by Bhagyashri Burade (bhagyashri_sahare@yahoo.com) .
 * Date : 22/02/2016.
 * Copyright (c)2016  Vijay Education Academy. All rights reserved.
 */

@SuppressLint({"NewApi", "SimpleDateFormat"})
public class OtpGeneratorActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_dividerPhone, txt_PhoneError, txt_FatherError, txt_dividerFather, txt_dividerUsername, txt_Division, txt_dividerStudentId, txt_StudentIdError, txt_UsernameError;
    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
    // Connection detector
    ConnectionDetector cd;
    //Creating views
    private EditText editTextUsername;
    private EditText editTextStudentId;
    private EditText editTextPhone;
    private EditText edittextViewFather;
    private Button buttonRegister;
    //Volley RequestQueue
   // private RequestQueue requestQueue;
    //String variables to hold username password and phone
    private String userid;
    private String username;
    private String fathername;
    private String phone;
    private String STUDENTID_URL;
    private RelativeLayout toolbarLayout;
    private String TAG="REQUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_view);
        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(this.getResources().getColor(R.color.primary));
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLiveView);
            toolbar.setTitle(R.string.app_name);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

        }catch (NullPointerException e)
        {

        }

        //Initializing Views
        editTextStudentId = (EditText) findViewById(R.id.editTextStudentId);
        txt_dividerStudentId = (TextView) findViewById(R.id.dividerStudentId);
        txt_StudentIdError = (TextView) findViewById(R.id.StudentIdError);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        txt_dividerUsername = (TextView) findViewById(R.id.dividerUsername);
        txt_UsernameError = (TextView) findViewById(R.id.UsernameError);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        txt_PhoneError = (TextView) findViewById(R.id.PhoneError);
        txt_dividerPhone = (TextView) findViewById(R.id.dividerPhone);
        edittextViewFather = (EditText) findViewById(R.id.textViewFather);
        txt_dividerFather = (TextView) findViewById(R.id.dividerFather);
        txt_FatherError = (TextView) findViewById(R.id.FatherError);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        //Adding a listener to button
        buttonRegister.setOnClickListener(this);

        cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(OtpGeneratorActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        try {
            //Initializing the RequestQueue
            //requestQueue = Volley.newRequestQueue(this);

            Intent intent = getIntent();
            String session = intent.getStringExtra("session");
            //Log.e("session====",session);
            if (session.equalsIgnoreCase("finish")) {
                final DialogFeedback dialogf = new DialogFeedback(
                        OtpGeneratorActivity.this,
                        "Feedback",
                        "");
                dialogf.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(NavigationDrawerActivity.this,
                        // "Click accept button", 1).show();
                        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                        String feedback = prefs.getString("feedback", "no feedback");//"No name defined" is the default value.
                        String studID = prefs.getString("userid", "0"); //0 is the default value.
                        //Log.e("session in if====",feedback+studID);
                        saveFeedback(feedback, studID);
                    }
                });
                dialogf.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        switch (id) {
            case R.id.action_exit:
                exit();
                break;
            case R.id.action_about:
                about_us();
                break;
            case android.R.id.home:
                Intent homeIntent = new Intent(this, NavigationDrawerActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                OtpGeneratorActivity.this.finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        //Calling register method on register button click
        //Getting user data
        userid = editTextStudentId.getText().toString().trim();
        username = editTextUsername.getText().toString().trim();
        fathername = edittextViewFather.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();
        if (!userid.equals("") && !username.equals("") && !fathername.equals("") && !phone.equals("")) {
            checkStudentId();

        } else {
            Utils validation = new Utils();
            Validate(validation);
        }
    }
    void Validate(Utils validation) {
        if (validation.isValidNull(userid)) {
            txt_StudentIdError.setVisibility(View.VISIBLE);
            txt_dividerStudentId.setVisibility(View.VISIBLE);
            txt_StudentIdError.setText("Please enter student id");
        }
        if (validation.isValidNull(username)) {
            txt_UsernameError.setVisibility(View.VISIBLE);
            txt_dividerUsername.setVisibility(View.VISIBLE);
            txt_UsernameError.setText("Please enter username");
        }

        if (validation.isValidNull(fathername)) {
            txt_FatherError.setVisibility(View.VISIBLE);
            txt_dividerFather.setVisibility(View.VISIBLE);
            txt_FatherError.setText("Please enter fathername");
        }
        if (validation.isValidNull(phone)) {
            txt_PhoneError.setVisibility(View.VISIBLE);
            txt_dividerPhone.setVisibility(View.VISIBLE);
            txt_PhoneError.setText("Please enter mobile number");
        }
    }

    //int res = 0;
    void checkStudentId() {
        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Checking Student Id..", "Please wait...", false, false);
        STUDENTID_URL = Config.STUDENTID_URL + userid;

        //Again creating the string request
         StringRequest stringRequest = new StringRequest(Request.Method.GET, STUDENTID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //If it is success
                        if (response.equals("0")) {
                            Toast.makeText(OtpGeneratorActivity.this, "This Student Id doesn't exist. Enter correct one & try again!.", Toast.LENGTH_LONG).show();
                            // res=Integer.parseInt(response);
                            clear();
                        } else {
                            register(response);
                            //res=Integer.parseInt(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        clear();
                        if (error != null) {

                            Toast.makeText(OtpGeneratorActivity.this, "Please enter correct student id & try again!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(OtpGeneratorActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
                        }
                        //Log.e("",error.getMessage());
                    }
                }) {
        };
        stringRequest.setTag(TAG);
        //Adding request the the queue
        ApplicationController.getInstance().addToRequestQueue(stringRequest);
    }

    //this method will generate otp
    void register(final String center_id) {
        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);
        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //If it is success
                        Log.e("OtpGenerationActivity:",response);
                        if (response.equals("ExceededRowCount")) {
                            Toast.makeText(OtpGeneratorActivity.this, "You have exceeded max otp generation limit.", Toast.LENGTH_LONG).show();
                        } else {
                            //Asking user to confirm otp
                            //confirmOtp();
                            Intent in = new Intent(OtpGeneratorActivity.this, ConfirmOtpActivity.class);
                            in.putExtra("userid", userid);
                            in.putExtra("username", username);
                            in.putExtra("fathername", fathername);
                            in.putExtra("phone", phone);
                            in.putExtra("center", center_id);
                            startActivity(in);
                            OtpGeneratorActivity.this.finish();
                            clear();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.e("OptGenerationActivity:", error.toString());
                        //com.android.volley.TimeoutError
                        clear();
                        if (error != null) {

                            Toast.makeText(OtpGeneratorActivity.this, "Please enter correct student id & try again!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(OtpGeneratorActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
                        }
                       // Toast.makeText(OtpGeneratorActivity.this, "Error: Server is overloaded.Try after sometime.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Adding the parameters to the request
                params.put(Config.KEY_USERID, userid);
                params.put(Config.KEY_USERNAME, username);
                params.put(Config.KEY_FATHERNAME, fathername);
                params.put(Config.KEY_PHONE, phone);
               return params;
            }
        };
        stringRequest.setTag(TAG);
        //Adding request the the queue
        ApplicationController.getInstance().addToRequestQueue(stringRequest);
    }

    void clear() {
        editTextStudentId.setText("");
        editTextUsername.setText("");
        edittextViewFather.setText("");
        editTextPhone.setText("");
    }

    //////////////////////////////save feedback//////////////////////////////////////////////
    // this method will register the user
    void saveFeedback(final String feedbk, final String studId) {

        // Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "",
                "Please wait...", false, false);
        // Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.SAVE_FEEDBACK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                // If it is success
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //error.getMessage();
                if (error != null) {

                    Toast.makeText(OtpGeneratorActivity.this, "Feedback not saved ,try again!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(OtpGeneratorActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
                }
                final DialogFeedback dialogf = new DialogFeedback(
                        OtpGeneratorActivity.this,
                        "Feedback",
                        "");
                dialogf.setOnAcceptButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(NavigationDrawerActivity.this,
                        // "Click accept button", 1).show();
                        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                        String feedback = prefs.getString("feedback", "no feedback");//"No name defined" is the default value.
                        String studID = prefs.getString("userid", "0"); //0 is the default value.
                        //Log.e("session in if====",feedback+studID);
                        saveFeedback(feedback, studID);
                    }
                });
                dialogf.show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // Adding the parameters to the request
                params.put(Config.KEY_USERID, studId);
                params.put(Config.KEY_FEEDBACK, feedbk);
                //Log.e("param==", params.toString());
                return params;
            }
        };
        stringRequest.setTag(TAG);
        // Adding request the the queue
        ApplicationController.getInstance().addToRequestQueue(stringRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // --------------------------------------About us
    // -------------------------------------------//
    private void about_us() {
        Intent in = new Intent(this,
                AboutUs.class);
        startActivity(in);
        OtpGeneratorActivity.this.finish();
    }

    // --------------------------------------Exit
    // -------------------------------------------//
    private void exit() {
        ApplicationController.getInstance().cancelPendingRequests(TAG);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ***Change Here***
        startActivity(intent);
        OtpGeneratorActivity.this.finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        ApplicationController.getInstance().cancelPendingRequests(TAG);

        Intent in = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(in);
        OtpGeneratorActivity.this.finish();
    }
}
package vijay.education.academylive.androidotp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import vijay.education.academylive.AlertDialogManager;
import vijay.education.academylive.ConnectionDetector;
import vijay.education.academylive.NavigationDrawerActivity;
import vijay.education.academylive.R;
import vijay.education.academylive.adapter.ApplicationController;
import vijay.education.academylive.sqlite.NotificatnDataRepository;
import vijay.education.academylive.utils.FirstTimePreference;
import vijay.education.academylive.utils.Installation;
import vijay.education.academylive.utils.Utils;

@SuppressLint("NewApi")
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {
    private EditText editTextUsername;
    private TextView txt_dividerUsername;
    private TextView txt_UsernameError;
    private EditText editTextPhone;
    private TextView txt_PhoneError;
    private TextView txt_dividerPhone;
    private Button buttonRegister;
    private RequestQueue requestQueue;
    private String username,phone,key;
    NotificatnDataRepository dbRepo;
    AlertDialogManager alert = new AlertDialogManager();
    // Connection detector
    ConnectionDetector cd;
    private String TAG="REQUESTLOGIN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                alert.showAlertDialog(LoginActivity.this,
                        "Internet Connection Error",
                        "Please connect to working Internet connection", false);
                // stop executing code by return
                return;
            }
            editTextUsername = (EditText) findViewById(R.id.editTextUsername1tim);
            txt_dividerUsername = (TextView) findViewById(R.id.dividerUsername);
            txt_UsernameError = (TextView) findViewById(R.id.UsernameError);
            editTextPhone = (EditText) findViewById(R.id.editTextPhone1tim);
            txt_PhoneError = (TextView) findViewById(R.id.PhoneError);
            txt_dividerPhone = (TextView) findViewById(R.id.dividerPhone);
            buttonRegister = (Button) findViewById(R.id.buttonRegister1tim);
            // Initializing the RequestQueue
            //requestQueue = Volley.newRequestQueue(this);
            // Adding a listener to button
            buttonRegister.setOnClickListener(this);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        username = editTextUsername.getText().toString().trim();
        phone = editTextPhone.getText().toString().trim();
        // TODO Auto-generated method stub
        if (!username.equals("") && !phone.equals("")) {
            register();
        } else {
            Utils validation = new Utils();
            Validate(validation);
        }
    }
    boolean req=false;
    boolean sms=false;
    // this method will register the user
    void register() {
        // Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering",
                "Please wait...", false, false);
        // Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.REGISTER_URLONETIME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                // If it is success
                if (response.equals("ExceededRowCount")) {
                    Toast.makeText(
                            LoginActivity.this,
                            "You are already registerd.",
                            Toast.LENGTH_LONG).show();
                    FirstTimePreference prefFirstTime = new FirstTimePreference(
                            getApplicationContext());
                    String id = Installation.id(getApplicationContext());
                    prefFirstTime.runTheFirstTime(id);

                    Intent in =new Intent(LoginActivity.this,NavigationDrawerActivity.class);
                    startActivity(in);
                    finish();
                } else {
                    req=true;
                    // Asking user to confirm otp
                    // confirmOtp();
                            /*String DATABASE_NAMEB = Environment
									.getExternalStorageDirectory() + "/vijay/vijayEduDB";
							File database=getApplicationContext().getDatabasePath(DATABASE_NAMEB);

							if (!database.exists()) {
								// Database does not exist so copy it from assets here
								Log.i("Database", "Not Found");
							} else {
								Log.i("Database", "Found");
								try {
									dbRepo = new NotificatnDataRepository(LoginActivity.this);

									dbRepo.deleteAllTbl2();
								}catch (SQLException e)
								{

								}
							}*/
                    Intent in = new Intent(LoginActivity.this,
                            LoginConfirmActivity.class);
                    in.putExtra("username", username);
                    in.putExtra("phone", phone);
                    startActivity(in);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                req=false;
                //show_snackbar();
                //Log.e("LoginActivity: error==",error.getMessage());
                Toast.makeText(
                        LoginActivity.this,
                        "Error: Server is overloaded.Try after sometime.",
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // Adding the parameters to the request
                params.put(Config.KEY_NAME, username);
                params.put(Config.KEY_PHONE, phone);
                //Log.e("param==", params.toString());
                return params;
            }
        };
        stringRequest.setTag(TAG);
        // Adding request the the queue
        ApplicationController.getInstance().addToRequestQueue(stringRequest);
    }

    void recivedSms(String messagel) {
        try {
            if(messagel!=null)
            {
                sms=true;
                if((sms&&!req))
                {
                    Intent in = new Intent(LoginActivity.this,
                            LoginConfirmActivity.class);
                    in.putExtra("username", username);
                    in.putExtra("phone", phone);
                    startActivity(in);
                    finish();
                }

            }
            else
            {

            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    void Validate(Utils validation) {
        if (validation.isValidNull(username)) {
            txt_UsernameError.setVisibility(View.VISIBLE);
            txt_dividerUsername.setVisibility(View.VISIBLE);
            txt_UsernameError.setText("Please enter username");
        }
        if (validation.isValidNull(phone)) {
            txt_PhoneError.setVisibility(View.VISIBLE);
            txt_dividerPhone.setVisibility(View.VISIBLE);
            txt_PhoneError.setText("Please enter mobile number");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ApplicationController.getInstance().cancelPendingRequests(TAG);
    }
}
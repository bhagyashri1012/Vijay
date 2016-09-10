package vijay.education.academylive.androidotp;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import vijay.education.academylive.NavigationDrawerActivity;
import vijay.education.academylive.R;
import vijay.education.academylive.adapter.ApplicationController;
import vijay.education.academylive.utils.FirstTimePreference;
import vijay.education.academylive.utils.Installation;
import vijay.education.academylive.viewPager.Button;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class LoginConfirmActivity extends Activity {
	static EditText editTextConfirmOtpLogin;
	private Button buttonConfirm;
	private TextView link;
	private RequestQueue requestQueue;
	private String username;
	private String phone;
	private String TAG="REQUESTCONFLOGIN";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_confirm);

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(this.getResources().getColor(
					R.color.primary));
		}
		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		phone = intent.getStringExtra("phone");
		buttonConfirm = (Button) findViewById(R.id.buttonConfirmlogin);
		editTextConfirmOtpLogin = (EditText) findViewById(R.id.editTextOtplogin);
		link = (TextView) findViewById(R.id.linklogin);
		// Initializing the RequestQueue
		//	requestQueue = Volley.newRequestQueue(this);
			buttonConfirm.setOnClickListener(new View.OnClickListener() {
				private String otp;
				@Override
				public void onClick(View v) {
					// Hiding the alert dialog
					// alertDialog.dismiss();
					// Displaying a progressbar
					final ProgressDialog loading = ProgressDialog.show(
							LoginConfirmActivity.this, "Authenticating",
							"Please wait while we check the entered code", false,
							false);
					// Getting the user entered otp from edittext
					otp = editTextConfirmOtpLogin.getText().toString().trim();
					// Creating an string request
					StringRequest stringRequest = new StringRequest(
							Request.Method.POST, Config.CONFIRM_URLONETIME,
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response) {
									// if the server response is success
									if (response.equalsIgnoreCase("success")) {
										// dismissing the progressbar
										loading.dismiss();
										// Toast.makeText(ConfirmOtp.this,"You have successfully verified your phone number",Toast.LENGTH_LONG).show();
										AlertDialog alertbox4 = new AlertDialog.Builder(
												LoginConfirmActivity.this).create();
										alertbox4
												.setMessage("You have successfully verified your phone number.");
										alertbox4
												.setButton(
														DialogInterface.BUTTON_POSITIVE,
														"Ok",
														new DialogInterface.OnClickListener() {

															public void onClick(
																	DialogInterface arg0,
																	int arg1) {
																FirstTimePreference prefFirstTime = new FirstTimePreference(
																		getApplicationContext());
																String id = Installation.id(getApplicationContext());
																prefFirstTime.runTheFirstTime(id);
																// Starting a new activity
																Intent in = new Intent(
																		LoginConfirmActivity.this,
																		NavigationDrawerActivity.class);
																startActivity(in);
																finish();
																arg0.dismiss();
															}
														});
										alertbox4.show();
									} else {
										loading.dismiss();

										// Displaying a toast if the otp entered is wrong
										Toast.makeText(LoginConfirmActivity.this,
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

									Toast.makeText(
											LoginConfirmActivity.this,
											"Try Again.",
											Toast.LENGTH_LONG).show();
								}
							}) {
						@Override
						protected Map<String, String> getParams()
								throws AuthFailureError {
							Map<String, String> params = new HashMap<String, String>();
							// Adding the parameters otp and username
							params.put(Config.KEY_PHONE, phone);
							params.put(Config.KEY_OTP, otp);
							Log.e("params===", params.toString());
							return params;
						}
					};
					stringRequest.setTag(TAG);
					// Adding the request to the queue
					ApplicationController.getInstance().addToRequestQueue(stringRequest);
				}
			});
			// confirmOtp(username);
			link.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Displaying a progress dialog
					final ProgressDialog loading = ProgressDialog.show(
							LoginConfirmActivity.this, "Registering", "Please wait...",
							false, false);
					// Again creating the string request
					StringRequest stringRequest = new StringRequest(
							Request.Method.POST, Config.REGISTER_URLONETIME,
							new Response.Listener<String>() {
								@Override
								public void onResponse(String response) {
									loading.dismiss();
										// If it is success
									if (response != null) {
										// Asking user to confirm otp confirmOtp();
									} else {
										// If not successful user may already have registered
										Toast.makeText(LoginConfirmActivity.this,
												"Try again!", Toast.LENGTH_LONG)
												.show();
									}
								}
							}, new Response.ErrorListener() {
								@Override
								public void onErrorResponse(VolleyError error) {
									loading.dismiss();
									FirstTimePreference prefFirstTime = new FirstTimePreference(
											getApplicationContext());
									String id = Installation.id(getApplicationContext());
									prefFirstTime.setTrue(id);
									//error.networkResponse.toString();
									//if(error.networkResponse)
									Toast.makeText(LoginConfirmActivity.this,
											error.networkResponse.toString(), Toast.LENGTH_LONG)
											.show();
								}
							}) {
						@Override
						protected Map<String, String> getParams()
								throws AuthFailureError {
							Map<String, String> params = new HashMap<String, String>();
							// Adding the parameters to the request
							params.put(Config.KEY_NAME, username);
							params.put(Config.KEY_PHONE, phone);
							return params;
						}
					};
					stringRequest.setTag(TAG);
					// Adding request the the queue
					ApplicationController.getInstance().addToRequestQueue(stringRequest);
				}
			});
		}

		 void recivedSms(String messagel) {
			try {
				//Log.e("msg", message);
				editTextConfirmOtpLogin.setText(messagel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();

			ApplicationController.getInstance().cancelPendingRequests(TAG);

			Intent in = new Intent(LoginConfirmActivity.this,
					LoginActivity.class);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			startActivity(in);
			finish();
		}
}

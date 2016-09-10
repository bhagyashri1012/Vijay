package vijay.education.academylive.androidotp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vijay.education.academylive.AlertDialogManager;
import vijay.education.academylive.CameraGridViewActivity;
import vijay.education.academylive.ConnectionDetector;
import vijay.education.academylive.NavigationDrawerActivity;
import vijay.education.academylive.R;
import vijay.education.academylive.adapter.ApplicationController;
import vijay.education.academylive.adapter.GridViewAdapter2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

@SuppressLint("NewApi")
public class ClassRoomViewActivity extends AppCompatActivity {
    /*public String[] mThumbIds = {
              "R1",  "R2",
              "R3",  "R4",
              "R5","R6","R7",
              "R8","R9"
      };*/
    private TextView timer;
    private static final String FORMAT = "%02d:%02d:%02d";
    private ArrayList<String> mThumbIds = new ArrayList<String>();
    // temporary string to show the parsed response
    private String jsonResponse;
    // Volley RequestQueue
    private RequestQueue requestQueue;
    private CountDownTimer ct;
    private String center;
    static public ProgressDialog dialog;
    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
    // Connection detector
    ConnectionDetector cd;
    private String TAG="REQUESTSUCCESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centerrooms);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primary));
        }
        cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(ClassRoomViewActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        // Initializing the RequestQueue
//        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        center = intent.getStringExtra("center");
        timer = (TextView) findViewById(R.id.timer);
        ct = new CountDownTimer(900000, 1000) { // 15min
            // timer

            public void onTick(
                    long millisUntilFinished) {
                timer.setText("SMS-OTP will expire in : "
                        + String.format(
                        FORMAT,
                        TimeUnit.MILLISECONDS
                                .toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS
                                .toMinutes(millisUntilFinished)
                                - TimeUnit.HOURS
                                .toMinutes(TimeUnit.MILLISECONDS
                                        .toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS
                                .toSeconds(millisUntilFinished)
                                - TimeUnit.MINUTES
                                .toSeconds(TimeUnit.MILLISECONDS
                                        .toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                  Intent i = new Intent(getApplicationContext(), OtpGeneratorActivity.class);
                startActivity(i);
                ClassRoomViewActivity.this.finish();
            }
        }.start();
        //////////////////////////////getdata cenetrwise//////////////////////////////////////////////
        dialog = ProgressDialog.show(ClassRoomViewActivity.this,
                "Please wait", "Page is loading..");
        StringRequest stringRequestcenter = new StringRequest(
                Request.Method.POST, Config.ROOMINFO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("cccccccccc", response.toString());
                        //msgResponse.setText(response.toString());
                        dialog.dismiss();
                        try {

                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";

                            JSONArray roominfo1 = new JSONArray(response);
                            for (int i = 0; i < roominfo1.length(); i++) {
                                JSONObject roominfo = new JSONObject(roominfo1.getString(i));
                                mThumbIds.add(roominfo.getString("room_id"));
                                roominfo.getString("center_id");
                                roominfo.getString("room_name");

                            }
                            // Log.e("makeJsonArrayRequest:::::",jsonResponse);
                            //Log.e("makeJsonArrayRequest====", mThumbIdsV.toString());
                            GridView gridView = (GridView) findViewById(R.id.grid_view);
                            gridView.setAdapter(new GridViewAdapter2(ClassRoomViewActivity.this, mThumbIds));
                            gridView.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    // TODO Auto-generated method stub
                                    // Sending image id to FullScreenActivity
                                    dialog.dismiss();
                                    Intent i = new Intent(getApplicationContext(), CameraGridViewActivity.class);
                                    // passing array index
                                    i.putExtra("room_id", mThumbIds.get(position));
                                    i.putExtra("timer", timer + "");
                                    i.putExtra("center", center);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    ClassRoomViewActivity.this.finish();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            centerAlert();
                        } catch (Exception e) {
                            // TODO: handle exception
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            Intent in = new Intent(ClassRoomViewActivity.this,
                                    NavigationDrawerActivity.class);
                            startActivity(in);
                            ClassRoomViewActivity.this.finish();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("", "Error: " + error.getMessage());
                dialog.dismiss();
             //   Toast.makeText(ClassRoomViewActivity.this, "Error: Server is overloaded.Try after sometime.", Toast.LENGTH_LONG).show();
                Intent in = new Intent(ClassRoomViewActivity.this,
                        ClassRoomViewActivity.class);
                in.putExtra("timer", timer + "");
                in.putExtra("center", center);
                startActivity(in);
                ClassRoomViewActivity.this.finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("center", center);
                return params;
            }
        };
        stringRequestcenter.setTag(TAG);
        ApplicationController.getInstance().addToRequestQueue(stringRequestcenter);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        ApplicationController.getInstance().cancelPendingRequests(TAG);
        sessionExpireAlert();
    }

    void sessionExpireAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ClassRoomViewActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Vijay");
        // Setting Dialog Message
        alertDialog
                .setMessage("Your 15 minute session will end! Do you want to end session?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.fail);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ct.onFinish();

//                        Intent i = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
//                        i.putExtra("session","finish");
//                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                        startActivity(i);
//                        ClassRoomViewActivity.this.finish();
                        dialog.dismiss();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }
    void centerAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                ClassRoomViewActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Vijay");
        // Setting Dialog Message
        alertDialog
                .setMessage("This facility is  not available for selected center. We are working on it.");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.fail);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ct.onFinish();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }
   /* public void feedback(final String feedback, final String studId) {
        final DialogFeedback dialogf = new DialogFeedback(
                Success.this,
                "Feedback",
                "");

        dialogf.setOnAcceptButtonClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Toast.makeText(NavigationDrawerActivity.this,
                // "Click accept button", 1).show();
              //  saveFeedback(feedback, studId);
            }
        });
        dialogf.show();
    }*/
   @Override
   protected void onDestroy() {
       super.onDestroy();
       finish();
   }
}

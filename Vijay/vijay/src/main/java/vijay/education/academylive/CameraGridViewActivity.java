package vijay.education.academylive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import vijay.education.academylive.adapter.ApplicationController;
import vijay.education.academylive.adapter.GridViewAdapter;
import vijay.education.academylive.androidotp.Config;
import vijay.education.academylive.androidotp.OtpGeneratorActivity;
import vijay.education.academylive.androidotp.ClassRoomViewActivity;
import vijay.education.academylive.vedio.VideoViewActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

@SuppressLint("NewApi")
public class CameraGridViewActivity extends AppCompatActivity {
    public ArrayList<String> mThumbIds = new ArrayList<String>();
    public ArrayList<String> mThumbIdsV = new ArrayList<String>();
    /*public String[] mThumbIdsV = { "rtsp://admin:admin123@122.168.198.129:554/streaming/channels/101",
            "rtsp://media.smart-streaming.com/mytest/mp4:sample.mp4",
            "rtsp://media.smart-streaming.com/mytest/mp4:BigBuckBunny_175k.mov",
            "rtsp://admin:admin123@122.168.198.129:554/streaming/channels/101"
            };*/
    // temporary string to show the parsed response
    private String jsonResponse;
    // Volley RequestQueue
    private RequestQueue requestQueue;
    String room_id;
    private String timer;
    private String center;
    static public ProgressDialog dialog;
    private String TAG="REQUEST_CameraGrid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_cameras);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
            // Initializing the RequestQueue
            requestQueue = Volley.newRequestQueue(this);

            Intent intent = getIntent();
            room_id = intent.getStringExtra("room_id");
            center = intent.getStringExtra("center");
            timer = intent.getStringExtra("timer");
            // Displaying a progressbar
            /*final ProgressDialog hideProgressDialog = ProgressDialog.show(
                    CameraGridViewActivity.this, "Authenticating",
					"Please wait while we check the entered code", false,
					false);
            */
            if (!timer.equalsIgnoreCase("00:00:00")) {
                dialog = ProgressDialog.show(CameraGridViewActivity.this,
                        "Please wait", "Page is loading..");
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, Config.CAMERAINFO_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Log.d("cccccccccc", response.toString());
                                //msgResponse.setText(response.toString());
                                dialog.dismiss();
                                try {
                                    // Parsing json array response
                                    // loop through each json object
                                    jsonResponse = "";
                                    JSONArray roominfo1 = new JSONArray(response);
                                    for (int i = 0; i < roominfo1.length(); i++) {
                                        JSONObject roominfo = new JSONObject(roominfo1.getString(i));
                                        mThumbIds.add(roominfo.getString("camera_id"));
                                        mThumbIdsV.add(roominfo.getString("camera_Ip"));
                                        roominfo.getString("camera_name");
                                    }
                                    // Log.e("makeJsonArrayRequest:::::",jsonResponse);
                                    //Log.e("makeJsonArrayRequest====", mThumbIdsV.toString());
                                    GridView gridView = (GridView) findViewById(R.id.grid_view_camera);
                                    gridView.setAdapter(new GridViewAdapter(CameraGridViewActivity.this, mThumbIds));

                                    gridView.setOnItemClickListener(new OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {
                                            // TODO Auto-generated method stub
                                            dialog.dismiss();
                                            Intent in = new Intent(CameraGridViewActivity.this, VideoViewActivity.class);
                                            //Log.e("cameraurl=====", mThumbIdsV.get(position));
                                            in.putExtra("cameraurl", mThumbIdsV.get(position));
                                            in.putExtra("timer", timer + "");
                                            in.putExtra("center", center);
                                            startActivity(in);
                                            finish();
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "No internet connection,Try again! ",
                                            Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                    Intent in = new Intent(CameraGridViewActivity.this,
                                            CameraGridViewActivity.class);
                                    startActivity(in);
                                    finish();
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(),
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    Intent in = new Intent(CameraGridViewActivity.this,
                                            NavigationDrawerActivity.class);
                                    startActivity(in);
                                    finish();
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        //Toast.makeText(CameraGridViewActivity.this, "Error: Server is overloaded.Try after sometime.", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(CameraGridViewActivity.this,
                                CameraGridViewActivity.class);
                        in.putExtra("timer", timer + "");
                        startActivity(in);
                        finish();
                    }

                }) {
                    @Override
                    protected Map<String, String> getParams()
                            throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        // Adding the parameters otp and username
                        params.put("room_id", room_id);
                        params.put("center", center);
                        //Log.e("params===", params.toString());
                        return params;
                    }
                };
                stringRequest.setTag(TAG);
                // Adding request to request queue
                ApplicationController.getInstance().addToRequestQueue(stringRequest);


            } else {
                AlertDialog alertbox4 = new AlertDialog.Builder(CameraGridViewActivity.this)
                        .create();

                alertbox4
                        .setMessage("SMS-OTP expired!Generate new OTP");

                alertbox4.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent i = new Intent(getApplicationContext(), OtpGeneratorActivity.class);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                startActivity(i);
                                finish();
                                arg0.dismiss();
                            }
                        });

                alertbox4.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        dialog.dismiss();
        ApplicationController.getInstance().cancelPendingRequests(TAG);
        Intent i = new Intent(getApplicationContext(), ClassRoomViewActivity.class);
        i.putExtra("timer", timer + "");
        i.putExtra("center", center);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(i);
        finish();
    }

}

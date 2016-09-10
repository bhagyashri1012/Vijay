package vijay.education.academylive;

/**
 * @author Bhagyashri Burade: 03/04/2016
 */

import vijay.education.academylive.androidotp.ClassRoomViewActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

@SuppressLint("NewApi")
public class AndroidVideoView extends Activity {

    DisplayMetrics dm;
    SurfaceView sur_View;


    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private String timer;
    AlertDialogManager alert = new AlertDialogManager();
    // Connection detector
    ConnectionDetector cd;
    private String center;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
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
        //cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!isInternetOn()) {
            // Internet Connection is not present
            alert.showAlertDialog(AndroidVideoView.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
        }
        // Get the layout from video_main.xml

        Intent intent = getIntent();
        String url = intent.getStringExtra("cameraurl");
        timer = intent.getStringExtra("timer");
        center = intent.getStringExtra("center");

        if (mediaControls == null) {
            mediaControls = new MediaController(AndroidVideoView.this);
        }

        // Find your VideoView in your video_main.xml layout
        myVideoView = (VideoView) findViewById(R.id.video_view);

        // Create a progressbar
        progressDialog = new ProgressDialog(AndroidVideoView.this);
        // Set progressbar title
        progressDialog.setTitle("Video");
        // Set progressbar message
        progressDialog.setMessage("Loading...");


        try {

            dm = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int height = dm.heightPixels;
            int width = dm.widthPixels;
            myVideoView.setMinimumWidth(width);
            myVideoView.setMinimumHeight(height);

            mediaControls.setAnchorView(myVideoView);
            myVideoView.setMediaController(mediaControls);//rtsp://admin:admin123@192.168.1.201/ch1/main/av_stream
            myVideoView.setVideoURI(Uri.parse(url));
            /*mediaURL = "rtsp://192.168.0.119/Bolt.ts"
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mediaURL));
					startActivity(intent); */

            progressDialog.show();
            myVideoView.requestFocus();
            myVideoView.setOnPreparedListener(new OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    progressDialog.show();
                    myVideoView.seekTo(position);
                    if (position == 0) {
                        // Show progressbar
                        progressDialog.show();
                        myVideoView.start();
                    } else {
                        myVideoView.pause();
                    }
                    progressDialog.dismiss();
                }
            });
            myVideoView.setOnErrorListener(new OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // TODO Auto-generated method stub
                    Log.e("e l===",extra+"");
                    AlertDialog alertbox4 = new AlertDialog.Builder(AndroidVideoView.this)
                            .create();

                    alertbox4
                            .setMessage("Cann't play this video. ,try again!");

                    alertbox4.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent i = new Intent(getApplicationContext(), ClassRoomViewActivity.class);
                                    i.putExtra("timer", timer + "");
                                    startActivity(i);
                                    finish();

                                    arg0.dismiss();
                                }
                            });

                    alertbox4.show();
                    return true;
                }
            });
        } catch (Exception e) {

            Log.e("Error", e.getMessage() + "ccc=" + e.getCause());
            e.printStackTrace();
            progressDialog.dismiss();
            myVideoView.stopPlayback();
            Intent i = new Intent(getApplicationContext(), ClassRoomViewActivity.class);
            i.putExtra("timer", timer + "");
            startActivity(i);
            finish();
        }

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        progressDialog.dismiss();
        Intent i = new Intent(AndroidVideoView.this, ClassRoomViewActivity.class);
        i.putExtra("timer", timer + "");
        i.putExtra("center", center);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(i);
        finish();
    }
    public final boolean isInternetOn() {
        // get Connectivity Manager object to check connection
        @SuppressWarnings("static-access")
        ConnectivityManager connec = (ConnectivityManager) getSystemService(this
                .getBaseContext().CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            // if connected with internet
            // Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            // Toast.makeText(this, " Not Connected ",
            // Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}

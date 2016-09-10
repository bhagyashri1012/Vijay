package vijay.education.academylive.vedio;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import vijay.education.academylive.R;
import vijay.education.academylive.androidotp.ClassRoomViewActivity;

/**
 * Created by Bhagya on 09-09-2016.
 */
public class VideoViewActivity extends AppCompatActivity {

    FullscreenVideoLayout videoLayout;
    private String timer,center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview2);

        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);
        videoLayout.setShouldAutoplay(false);

        Uri videoUri = Uri.parse("rtsp://tv.hindiworldtv.com:1935/live/getpun");
        //Uri videoUri = Uri.parse("rtsp://admin:12345@122.168.194.170:82/streaming/channels/101");

        /*Intent intent = getIntent();
        String url = intent.getStringExtra("cameraurl");
        //Uri videoUri = Uri.parse(url);
        timer = intent.getStringExtra("timer");
        center = intent.getStringExtra("center");*/

        try {
            videoLayout.setVideoURI(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        videoLayout.resize();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
       /* Intent i = new Intent(VideoViewActivity.this, ClassRoomViewActivity.class);
        i.putExtra("timer", timer + "");
        i.putExtra("center", center);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(i);
        finish();*/
    }
}
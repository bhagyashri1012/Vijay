package vijay.education.academylive;


import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

@SuppressLint({"SetJavaScriptEnabled", "NewApi"})
public class Admission extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */
    static public ProgressDialog dialog;
    private WebView web;
    private RelativeLayout toolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admission);
        try {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(this.getResources().getColor(R.color.primary));
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarWebview);
            toolbar.setTitle(R.string.app_name);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

        } catch (NullPointerException e) {

        }
        dialog = ProgressDialog.show(Admission.this, "Please wait", "Page is loading..");
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        web = (WebView) findViewById(R.id.webview01);
        web.setFocusableInTouchMode(true);
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(url);

    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        dialog.dismiss();
        web.stopLoading();
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (web.canGoBack()) {
                        web.goBack();
                        Intent in = new Intent(Admission.this, NavigationDrawerActivity.class);
                        //in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                    } else {
                        //	Log.e("in bk", "elseeeeeeeeeeeeeeeeeeee");
                        Intent in = new Intent(Admission.this, NavigationDrawerActivity.class);
                       // in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        web.stopLoading();
        dialog.dismiss();
        // Log.e("in bp", "bbbbbbbbbbbbbbbkkkkkkkkkkkkkkkkkkkkkk");
        // TODO Auto-generated method stub
        if (web.canGoBack()) {
            web.goBack();
            Intent in = new Intent(Admission.this, NavigationDrawerActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            finish();
        } else {
            //Log.e("in bp", "in elseeeeeeeeeeeeeeeee");
            Intent in = new Intent(Admission.this, NavigationDrawerActivity.class);
            startActivity(in);
            finish();
            super.onBackPressed();
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
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    // --------------------------------------About us
    // -------------------------------------------//
    private void about_us() {
        Intent in = new Intent(this,
                AboutUs.class);
        startActivity(in);
        finish();
    }

    // --------------------------------------Exit
    // -------------------------------------------//
    private void exit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            dialog.dismiss();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
             view.loadUrl(url);
            return true;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
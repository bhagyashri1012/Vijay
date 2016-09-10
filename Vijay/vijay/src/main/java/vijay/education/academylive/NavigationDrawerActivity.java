package vijay.education.academylive;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseApp;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vijay.education.academylive.androidotp.OtpGeneratorActivity;
import vijay.education.academylive.model.VijayNotificationData;
import vijay.education.academylive.sqlite.NotificatnDataRepository;
import vijay.education.academylive.utils.CheckPermissions;
import vijay.education.academylive.utils.UsersEmailIdFetcherUtil;
import vijay.education.academylive.viewPager.Dialog;
import vijay.education.academylive.viewPager.ViewPagerFragmentAdapter;

import static vijay.education.academylive.CommonUtilities.ADDMISSION_URL;
import static vijay.education.academylive.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static vijay.education.academylive.CommonUtilities.PDF_URL;
import static vijay.education.academylive.CommonUtilities.SENDER_ID;
import static vijay.education.academylive.CommonUtilities.SERVER_URL;
import static vijay.education.academylive.CommonUtilities.VIDEO_URL;
import static vijay.education.academylive.CommonUtilities.WEBSITE_URL;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPagerFragmentAdapter mAdapter1;
    private ViewPager mPager;
    private PageIndicator mIndicator;
    private RelativeLayout toolbarLayout;
    private ArrayList<VijayNotificationData> feedData = new ArrayList<VijayNotificationData>();
    private ArrayList<VijayNotificationData> feedDataCount = new ArrayList<VijayNotificationData>();
    ArrayList<String> projectnm = new ArrayList<String>();
    ArrayList<String> projectdt = new ArrayList<String>();
    private TextView notifCount;
    static int mNotifCount = 0, countNoti;
    protected String newMessage = "";
    NotificatnDataRepository dbRepo;
    CoordinatorLayout coordinatorLayout;
    // Asyntask
    private AsyncTask<Void, Void, Void> mRegisterTask;
    // Alert dialog_rate_us manager
    AlertDialogManager alert = new AlertDialogManager();
    static String name, email;

    private GoogleApiClient client;
    private MySharedPreferences mySharedPreference;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES=5;
    private static final int MY_PERMISSIONS_REQUEST_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        CheckPermissions.checkReadPhoneStatePermission(this,1);
        CheckPermissions.checkWakeLockPermission(this,2);
        CheckPermissions.checkStoragePermission(this,3);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        //  Firebase.setAndroidContext(this);

        //  checkPlayServices();
      /*  mySharedPreference = new MySharedPreferences(this);
//        notificationCheckbox = (CheckBox)findViewById(R.id.subscribe);
        //final View coordinatorLayoutView = findViewById(R.id.snackbarPosition);
        boolean sentToken = mySharedPreference.hasUserSubscribeToNotification();
        if (sentToken) {
//            notificationCheckbox.setChecked(true);
//            notificationCheckbox.setEnabled(false);
//            notificationCheckbox.setText(getString(R.string.registered));
//            .make(coordinatorLayoutView, getString(R.string.registered), Snackbar.LENGTH_LONG).show();
        } else {
//            notificationCheckbox.setChecked(false);
//            notificationCheckbox.setEnabled(false);
//            notificationCheckbox.setText(getString(R.string.registered));
//            Snackbar.make(coordinatorLayoutView, getString(R.string.un_register), Snackbar.LENGTH_LONG).show();
        }
*/
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(this.getResources().getColor(
                        R.color.primary));
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("VijayEduLive");
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            mAdapter1 = new ViewPagerFragmentAdapter(getSupportFragmentManager());
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(mAdapter1);
            mPager.setOffscreenPageLimit(1);

            CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
            mIndicator = indicator;
            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;
            indicator.setBackgroundColor(0xFFFFFF);
            indicator.setRadius(6 * density);
            indicator.setPageColor(0xF44336);
            // indicator.setFillColor(0x880000FF);
            // indicator.setStrokeColor(0xFF000000);
            indicator.setStrokeWidth(2 * density);
            //FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            if (isInternetOn()) {
                // Check if GCM configuration is set
                if (SERVER_URL == null || SENDER_ID == null
                        || SERVER_URL.length() == 0 || SENDER_ID.length() == 0) {
                    // GCM sernder id / server url is missing
                    alert.showAlertDialog(NavigationDrawerActivity.this, "Configuration Error!",
                            "Please set your Server URL and GCM Sender ID", false);
                    // stop executing code by return
                    return;
                }
                // Make sure the device has the proper dependencies.
                GCMRegistrar.checkDevice(this);
                // Make sure the manifest was properly set - comment out this line
                // while developing the app, then uncomment it when it's ready.
                GCMRegistrar.checkManifest(this);
                if (GCMRegistrar.isRegistered(this)
                        ) {
                } else {
                    GCMRegistrar.register(this, SENDER_ID);
                }
                registerReceiver(mHandleMessageReceiver, new IntentFilter(
                        DISPLAY_MESSAGE_ACTION));

                // Get GCM registration id
                final String regId = GCMRegistrar.getRegistrationId(this);

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.GET_ACCOUNTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.GET_ACCOUNTS)) {

                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.GET_ACCOUNTS},
                                MY_PERMISSIONS_REQUEST_PHONE_STATE);
                    }
                } else {

                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    name = telephonyManager.getDeviceId();
                    email = UsersEmailIdFetcherUtil.getEmail(getApplicationContext());
                    Log.e("TelephonyManager", name + email);

                }

                // Check if regid already presents
                if (regId.equals("")) {
                    // Registration is not present, register now with GCM
                    GCMRegistrar.register(this, SENDER_ID);
                } else {
                    // Device is already registered on GCM
                    if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
                        // Skips registration.
                        // Toast.makeText(getApplicationContext(),
                        // "Already registered with GCM", Toast.LENGTH_LONG).show();
                    } else {
                        // Try to register again, but not in the UI thread.
                        // It's also necessary to cancel the thread onDestroy(),
                        // hence the use of AsyncTask instead of a raw thread.
                        final Context context = this;
                        mRegisterTask = new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                // Register on our server
                                // On server creates a new user
                                ServerUtilities.register(context, name, email,
                                        regId);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                mRegisterTask = null;
                            }

                        };
                        mRegisterTask.execute(null, null, null);
                    }
                }

            }
            try {
                dbRepo = new NotificatnDataRepository(this);
                feedData = dbRepo.getAllData();
                countNoti = dbRepo.getCOUNT();
                //Log.e("countNoti=", countNoti+"");

            } catch (NullPointerException e) {
                countNoti = 0;
                //Log.e("(NullPointerException", countNoti+"");
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab1);
            if (fabButton != null) {
                fabButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    /*
                    Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                    rateus
                    */
                        rateus(NavigationDrawerActivity.this);
                    }
                });
            }
            checkPlayServices();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (exit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ***Change Here***
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                System.exit(0);
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_my_main_drawer, menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item1 = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(item1,
                R.layout.notification_update_count_layout);
        View count = menu.findItem(R.id.actionbar_item).getActionView();
        notifCount = (TextView) count.findViewById(R.id.badge_notification_3);
        if (isInternetOn()) {
            // Internet Connection is not present
            mNotifCount = countNoti;
            // stop executing code by return
        } else {
            mNotifCount = 0;
            notifCount.setVisibility(View.GONE);
        }
        notifCount.setText(String.valueOf(mNotifCount));

        toolbarLayout = (RelativeLayout) MenuItemCompat.getActionView(item1);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {

            case R.id.action_exit:
                exit();
                break;
            case R.id.action_about:
                about_us();
                break;

            case R.id.actionbar_item:
                TextView bn = (TextView) findViewById(R.id.badge_notification_3);
                bn.setText("");

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_admission) {
            // addmission
            if (isInternetOn()) {
                Intent in = new Intent(NavigationDrawerActivity.this,
                        Admission.class);
                in.putExtra("url", ADDMISSION_URL);
                startActivity(in);
                finish();
            } else {
                show_snackbar();
            }

        } else if (id == R.id.nav_download) {
            // pdf
            String extStorageDirectory = Environment
                    .getExternalStorageDirectory()
                    .toString();
            File folder = new File(extStorageDirectory,
                    "Vijay");
            folder.mkdir();
            File file = new File(folder, "Brochure.pdf");
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (isInternetOn()) {
                if (Build.VERSION.SDK_INT < 9) {
                    new DownloadAndOpen().execute(file);
                }
                if (Build.VERSION.SDK_INT >= 9) {
                    Downloader.file_download(PDF_URL,
                            folder, NavigationDrawerActivity.this);
                }
            } else {
                show_snackbar();
            }
        } else if (id == R.id.nav_livevw) {
            if (isInternetOn()) {
                // live view
                Intent in = new Intent(NavigationDrawerActivity.this,
                        OtpGeneratorActivity.class);

                startActivity(in);
                finish();
            } else {
                show_snackbar();

            }
        } else if (id == R.id.nav_student) {
            if (isInternetOn()) {
                Intent in = new Intent(NavigationDrawerActivity.this,
                        AdvanceWebActivity.class);
                in.putExtra("url", CommonUtilities.STUDENT_URL);
                startActivity(in);
                finish();
            } else {
                show_snackbar();

            }
        } else if (id == R.id.nav_video) {
            // video
            if (isInternetOn()) {
                Intent in = new Intent(NavigationDrawerActivity.this,
                        Admission.class);
                in.putExtra("url", VIDEO_URL);
                startActivity(in);
                finish();
            } else {
                show_snackbar();

            }
        } else if (id == R.id.nav_web) {
            // website
            if (isInternetOn()) {
                // new
                Intent in = new Intent(NavigationDrawerActivity.this,
                        Admission.class);
                in.putExtra("url", WEBSITE_URL);
                startActivity(in);
                finish();
            } else {
                show_snackbar();

            }
        } else if (id == R.id.nav_rateus) {
            // rateus
            final Dialog dialog = new Dialog(
                    NavigationDrawerActivity.this,
                    "Rate this app",
                    "If you enjoy using this app, would you mind taking a moment to rate it? It won't take more than a minute. Thank you for your support!");
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Toast.makeText(NavigationDrawerActivity.this,
                    // "Click accept button", 1).show();
                    rateus(NavigationDrawerActivity.this);
                }
            });
            dialog.setOnCancelButtonClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Toast.makeText(NavigationDrawerActivity.this,
                    // "Click cancel button", 1).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (id == R.id.nav_exit) {
            // exit
            exit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * rateus
     *
     * @param context
     */
    private void rateus(final Context context) {
        // TODO Auto-generated method stub
        Uri uri = Uri.parse("market://details?id=" + "vijay.education.academylive");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=vijay.education.academylive")));
        }
    }

    // --------------------------------------About us
    // -------------------------------------------//
    private void about_us() {
        mPager.destroyDrawingCache();
        getSupportFragmentManager().popBackStackImmediate();
        Intent in = new Intent(NavigationDrawerActivity.this, AboutUs.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    /**
     *
     */
    private void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                NavigationDrawerActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Exit ?");
        // Setting Dialog Message
        alertDialog.setMessage("Would you like to exit the application?");
        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.error);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// ***Change
                        // Here***
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                });

        // / Setting Negative "NO" Button
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

    /**
     * show downloaded broucher
     */
    private void showPdf() {
        File file = new File(Environment.getExternalStorageDirectory()
                + "/Vijay/Brochure.pdf");
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
           // client = new GoogleApiClient.Builder(this)

             //       .build();
    //          client.connect();
            //  client.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {

//              client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Download Broucher
     */
    public class DownloadAndOpen extends AsyncTask<File, String, Integer> {
        public ProgressDialog dialog;
        int length;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(NavigationDrawerActivity.this, "",
                    "Downloading...Please wait...");
        }

        @Override
        protected Integer doInBackground(File... params) {
            // TODO Auto-generated method stub

            length = Downloader.DownloadFile(PDF_URL, params[0]);

            return length;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            if (result == 0) {
                AlertDialog alertbox4 = new AlertDialog.Builder(NavigationDrawerActivity.this)
                        .create();

                alertbox4.setMessage("Cann't download pdf ,try again!");

                alertbox4.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                            }
                        });

                alertbox4.show();
            } else {
                showPdf();
            }
        }
    }

    /**
     * Check Internet Connection
     */

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

    private void show_snackbar() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection.Try again!", Snackbar.LENGTH_LONG);
                /*.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    /**
     * Click on notification bell icon
     *
     * @param view
     */

    public void bellClick(View view) {
        if (view == findViewById(R.id.button3)) {
            // NotificationCountBadgeAndroidActionBar.class);
            try {
                for (int i = 0; i < feedData.size(); i++) {
                    projectnm.add(feedData.get(i).getNotificatn_my());
                    projectdt.add(feedData.get(i).getNotificatn_date());
                    //   Log.e("time dashboard==", feedData.get(i)
                    //         .getNotificatn_date());
                }
                Intent in = new Intent(NavigationDrawerActivity.this,
                        NotificationCountBadgeAndroidActionBar.class);
                in.putStringArrayListExtra("msg", projectnm);
                in.putStringArrayListExtra("time", projectdt);
                in.putExtra("count", countNoti);
                startActivity(in);
                finish();
            } catch (NullPointerException e) {
                Intent in = new Intent(NavigationDrawerActivity.this,
                        NotificationCountBadgeAndroidActionBar.class);
                startActivity(in);
                finish();
            }
        }
    }

    //*
    // * Receiving push messages
    // *
    final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            newMessage = intent.getExtras().getString("msg");

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        Log.e(TAG, "resultCode");
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else if (apiAvailability.isUserResolvableError(resultCode) &&
                    apiAvailability.showErrorDialogFragment(this, resultCode, REQUEST_GOOGLE_PLAY_SERVICES)) {
                // wait for onActivityResult call (see below)
            } else {
                Toast.makeText(this, apiAvailability.getErrorString(resultCode), Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    name = telephonyManager.getDeviceId();
                    email = UsersEmailIdFetcherUtil.getEmail(getApplicationContext());
                    Log.e("TelephonyManager", name + email);

                } else {
                    name = "";
                    email = "";
                }
                break;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    client = new GoogleApiClient.Builder(this).setAccountName(email).build();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
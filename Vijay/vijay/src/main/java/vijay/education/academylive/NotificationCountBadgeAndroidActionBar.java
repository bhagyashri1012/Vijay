package vijay.education.academylive;

// Adding Badge (Notification Count) to Android ActionBar Items Icon Using XMl and Java Code

/**
 * @author Bhagyashri Burade: 03/04/2016
 *
 */

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

import java.util.ArrayList;
import java.util.Collections;

import vijay.education.academylive.adapter.CustomListAdaptor;
import vijay.education.academylive.model.VijayNotificationData;
import vijay.education.academylive.sqlite.NotificatnDataRepository;
import static vijay.education.academylive.CommonUtilities.EXTRA_MESSAGE;
import static vijay.education.academylive.CommonUtilities.EXTRA_MESSAGED;
@SuppressLint("NewApi")
public class NotificationCountBadgeAndroidActionBar extends ActionBarActivity {

	public static String name;
	public static String email;
	static int mNotifCount = 0;
	protected String newMessage = "";
	protected String newMessageD = "";
	// label to display gcm messages
	TextView lblMessage;
		// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	// Connection detector
	ConnectionDetector cd;
	NotificatnDataRepository dbRepo;
	private RelativeLayout notificationCount1, toolbarLayout;
	private Toolbar toolbar;
	private DrawerLayout Drawer;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView listview;
	private CustomListAdaptor candiArrayAdapter;
	private ArrayList<VijayNotificationData> feedData2 = new ArrayList<VijayNotificationData>();
	private ArrayList<String> feedData = new ArrayList<String>();
	private ArrayList<String> feedDataDt = new ArrayList<String>();
	private TextView notifCount;
	private int countNoti;
	private TextView lbltime;
	private long time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_count_badge_in_actionbar);

		notificationCount1 = (RelativeLayout) findViewById(R.id.relative_layout1);

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(this.getResources().getColor(
					R.color.primary));
		}

		toolbarLayout = (RelativeLayout) findViewById(R.id.tootbarLayoutNoti);
		toolbar = (Toolbar) findViewById(R.id.tool_bar2);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("NOTIFICATIONS");
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		lblMessage = (TextView) findViewById(R.id.textviewNoti);
		lbltime = (TextView) findViewById(R.id.textviewNotiTitle);
		cd = new ConnectionDetector(getApplicationContext());

		/*// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(NotificationCountBadgeAndroidActionBar.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}*/
		Intent intent = getIntent();
		
		try{
			if(intent != null)
			{
		feedData = intent.getStringArrayListExtra("msg");
		feedDataDt = intent.getStringArrayListExtra("time");
		countNoti = intent.getIntExtra("count",0);
		//Log.e("intent != null===",countNoti+"");
		//countNoti=feedData.size();
			}
		if (!feedData.isEmpty()) {
			Collections.reverse(feedData);
			Collections.reverse(feedDataDt);
		candiArrayAdapter = new CustomListAdaptor(
				NotificationCountBadgeAndroidActionBar.this,
				R.layout.notification_item, feedData,feedDataDt);
		listview = (ListView) findViewById(R.id.listView1);
		listview.setItemsCanFocus(false);
		listview.setAdapter(candiArrayAdapter);
		//Log.e("!feedData.isEmpty()===",countNoti+"");
		}else
		{
			try {
				ArrayList<VijayNotificationData> feedDataVijay = new ArrayList<VijayNotificationData>();
				dbRepo = new NotificatnDataRepository(this);
				feedDataVijay = dbRepo.getAllData();
				countNoti = feedDataVijay.size();
				for (int i = 0; i < feedDataVijay.size(); i++) {
					feedData.add(feedDataVijay.get(i).getNotificatn_my());
					feedDataDt.add(feedDataVijay.get(i).getNotificatn_date());

				}
				Collections.reverse(feedData);
				Collections.reverse(feedDataDt);
				candiArrayAdapter = new CustomListAdaptor(
						NotificationCountBadgeAndroidActionBar.this,
						R.layout.notification_item, feedData,feedDataDt);
				listview = (ListView) findViewById(R.id.listView1);
				listview.setItemsCanFocus(false);
				listview.setAdapter(candiArrayAdapter);

			} catch (NullPointerException e) {
				countNoti = 0;

			} catch (java.sql.SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		}catch(NullPointerException e)
		{
			countNoti=0;
			//Log.e("NullPointerException===",countNoti+"");
		}
	//	registerReceiver(mHandleMessageReceiver, new IntentFilter(
	//			DISPLAY_MESSAGE_ACTION));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		MenuItem item1 = menu.findItem(R.id.actionbar_item);
		MenuItemCompat.setActionView(item1,
				R.layout.notification_update_count_layout);

		View count = menu.findItem(R.id.actionbar_item).getActionView();
		notifCount = (TextView) count.findViewById(R.id.badge_notification_3);
		// Check if Internet present
		if (cd.isConnectingToInternet()) {
			// Internet Connection is not present

			mNotifCount = countNoti;
			// stop executing code by return
		}else
		{
			mNotifCount=0;
			notifCount.setVisibility(View.GONE);
		}
		notifCount.setText(String.valueOf(mNotifCount));
		toolbarLayout = (RelativeLayout) MenuItemCompat.getActionView(item1);
		return super.onCreateOptionsMenu(menu);
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
		Intent in = new Intent(NotificationCountBadgeAndroidActionBar.this,
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

	public void bellClick(View view) {
		if (view == findViewById(R.id.button3)) {
			Intent in = new Intent(NotificationCountBadgeAndroidActionBar.this,
					NotificationCountBadgeAndroidActionBar.class);
			startActivity(in);
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(NotificationCountBadgeAndroidActionBar.this, NavigationDrawerActivity.class);
		
		overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
		startActivity(i);
		finish();
	}
	/**
	 * Receiving push messages
	/* * */
	final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			newMessageD = intent.getExtras().getString(EXTRA_MESSAGED);
			Collections.reverse(feedData);
			Collections.reverse(feedDataDt);
			//Log.e("newMessage===",newMessage+"");
			feedData.add(newMessage);
			feedDataDt.add(newMessageD);

			Intent in = new Intent(NotificationCountBadgeAndroidActionBar.this,
					NotificationCountBadgeAndroidActionBar.class);
			in.putStringArrayListExtra("msg", feedData);
			in.putStringArrayListExtra("time", feedDataDt);
			in.putExtra("count",countNoti+1);
			startActivity(in);
			finish();
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	@Override
	protected void onDestroy() {
		
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
		//	Log.e("UnRegisterReciver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
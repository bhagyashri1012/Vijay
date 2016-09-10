package vijay.education.academylive;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import vijay.education.academylive.model.VijayNotificationData;
import vijay.education.academylive.sqlite.NotificatnDataRepository;

@SuppressLint("NewApi")
public class NotificationDetails extends AppCompatActivity {

	static int mNotifCount = 0;
	ArrayList<String> projectnm = new ArrayList<String>();
	ArrayList<String> projectdt = new ArrayList<String>();
	private TextView lblMessage;
	private String notification;
	private ArrayList<VijayNotificationData> feedData = new ArrayList<VijayNotificationData>();
	private int countNoti;
	private TextView lblDate;
	private String notificationdt;
	private NotificatnDataRepository dbRepo;
	private  VijayNotificationData noti=new VijayNotificationData();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_details);

		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = this.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(this.getResources().getColor(
					R.color.primary));
		}

		lblMessage = (TextView) findViewById(R.id.textviewNotiDetails);
		lblDate = (TextView) findViewById(R.id.textviewNotiDetailsTitle);
		Intent intent = getIntent();
		notification = intent.getStringExtra("notification");
		lblMessage.setText(notification);
		notificationdt = intent.getStringExtra("notificationdt");
		lblDate.setText(notificationdt);
		Log.e("notification===",notification+"");
			
			try {
				dbRepo = new NotificatnDataRepository(this);
				feedData = dbRepo.getAllData();
				countNoti = feedData.size();
				
			} catch (NullPointerException e) {
				countNoti = 0;

			} catch (java.sql.SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			for (int i = 0; i < feedData.size(); i++) {
				projectnm.add(feedData.get(i).getNotificatn_my());
				projectdt.add(feedData.get(i).getNotificatn_date());
				
			}
			noti.setNotificatn_my(notification);
				dbRepo.updateDataTableNotification(noti);
				countNoti=dbRepo.getCOUNT();
				//Log.e("countNoti===",countNoti+"");
			Intent in = new Intent(NotificationDetails.this,
					NotificationCountBadgeAndroidActionBar.class);
			in.putStringArrayListExtra("msg", projectnm);
			in.putStringArrayListExtra("time", projectdt);
			in.putExtra("count", countNoti);
			startActivity(in);
			finish();
		} catch (NullPointerException e) {
			Intent i = new Intent(NotificationDetails.this,
					NotificationCountBadgeAndroidActionBar.class);
			overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
			startActivity(i);
			finish();
		}

	}

	public final boolean isInternetOn() {
		// get Connectivity Manager object to check connection
		@SuppressWarnings("static-access")
		ConnectivityManager connec = (ConnectivityManager) getSystemService(this
				.getBaseContext().CONNECTIVITY_SERVICE);
		// Check for network connections
		if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
			// if connected with internet
			// Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
			return true;
		} else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
			// Toast.makeText(this, " Not Connected ",
			// Toast.LENGTH_LONG).show();
			return false;
		}
		return false;
	}
}

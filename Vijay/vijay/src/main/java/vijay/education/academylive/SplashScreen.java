package vijay.education.academylive;

/**
 * @author Bhagyashri Burade: 05/05/2015
 *
 */
import java.util.Calendar;




import vijay.education.academylive.androidotp.LoginActivity;
import vijay.education.academylive.utils.FirstTimePreference;
import vijay.education.academylive.utils.Installation;
import vijay.education.academylive.vedio.VideoViewActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class SplashScreen extends Activity {
	private static final int SPLASH_SHOW_TIME = 4500;
	AlarmManager alarmManager;
	Intent alarmIntent;
	PendingIntent pendingIntent;
	Calendar alarmStartTime;
	
	//loginsession session0;

	@Override
	public void onBackPressed() {
		return;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		if (android.os.Build.VERSION.SDK_INT >= 21) {
		Window window = this.getWindow();

		// clear FLAG_TRANSLUCENT_STATUS flag:
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

		// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

		// finally change the color
		window.setStatusBarColor(this.getResources().getColor(R.color.primary));
		}
		new BackgroundSplashTask().execute();
	}

	private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			//session0 = new loginsession(getApplicationContext());
			//notificationDaily();
			if (getIntent().getBooleanExtra("EXIT", false)) {
				finish();
			}
			//session0.clearSession();
			try {
				Thread.sleep(SPLASH_SHOW_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			FirstTimePreference prefFirstTime = new FirstTimePreference(
					getApplicationContext());
			// Toast.makeText(getApplicationContext(),
			// "Test myKey & coutdown: "+
			// prefFirstTime.getCountDown("myKey"),Toast.LENGTH_LONG).show();
			String id = Installation.id(getApplicationContext());
			
			//setAlarm();
			if (prefFirstTime.getCountDown(id)!=0) {
				Log.e("SSSSSSSSSSSS","prefFirstTime.runTheFirstTime(id)");
				 Intent in =new Intent(SplashScreen.this,LoginActivity.class);
				super.onPostExecute(result);
				startActivity(in);
				finish();


			} else {
				/*SharedPreferences preferences = PreferenceManager
			            .getDefaultSharedPreferences(getApplicationContext());
			    SharedPreferences.Editor editor = preferences.edit();
			    int i = preferences.getInt("numberoflaunches", 1);
			//    i=1;
			    if (i < 2) {
			        i++;
			        editor.putInt("numberoflaunches", i);
			        editor.commit();*/
			    
			    Intent in =new Intent(SplashScreen.this,VideoViewActivity.class);
				super.onPostExecute(result);
				startActivity(in);
				finish();
			}
		}
			
	}
}

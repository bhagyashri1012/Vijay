package vijay.education.academylive.viewPager;

import vijay.education.academylive.R;
import vijay.education.academylive.MainActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public final class ViewPagerFragment extends Fragment {
    private static final String KEY_CONTENT = "ViewPagerFragment:Content";

    static String titleText;
    int imageSource;
	int imageName;
	String buttonnm = null;
	private ImageView imgView;
	private TextView title;
	ProgressBar pg;
private static final int SPLASH_SHOW_TIME = 4500;
	
	
	
	public ViewPagerFragment(int imageSource,int imageName) {
		this.imageSource = imageSource;
		this.imageName = imageName;

	}
	public ViewPagerFragment() {
		
	}
   


    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
        	imageSource = savedInstanceState.getInt(KEY_CONTENT);
        	imageName = savedInstanceState.getInt(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewGroup root = (ViewGroup) inflater.inflate(R.layout.page, null);
    	pg = (ProgressBar)root.findViewById(R.id.progressBar1);
    	title=(TextView)root.findViewById(R.id.title);
		ImageView image = (ImageView)root.findViewById(R.id.slider_image);
		imgView=(ImageView)root.findViewById(R.id.buttonT1);
		image.setImageResource(imageSource);
		title.setText(imageName);
	
		setRetainInstance(true);
		
		
		imgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectFrag(v);
			}
		});
		
		
        return root;
    }
    public void selectFrag(View view) {

	//	TextView title = (TextView) view.findViewById(R.id.title);

		if (view == imgView) {
			titleText=title.getText().toString();
			
			if (titleText.equals("Geeta Bhawan Campus")) {
				
			buttonnm = "button1";
				Log.e("Corporate office gita bhawan===", buttonnm);
			} 
			if (titleText.equals(
					"Ranjeet Hanuman Campus")) {
				buttonnm = "button2";
				Log.e("ranjeet===", buttonnm);
			} 
			if (titleText.equals("Mhow Campus")) {
				buttonnm = "button3";
				Log.e("mhow===", buttonnm);
			}

		}
		/*
		 * if (view == findViewById(R.id.button2)) {2 buttonnm = "button2";
		 * Log.e("butn2",buttonnm);
		 * 
		 * } else if (view == findViewById(R.id.buttonT1)) { buttonnm =
		 * "button1";
		 * 
		 * } else if (view == findViewById(R.id.button3)) { buttonnm =
		 * "button3";
		 * 
		 * }
		 *//*
			 * else if (view == findViewById(R.id.button4)) { buttonnm =
			 * "button4"; }
			 */
		if (isInternetOn()) {

			LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			if (!locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				buildAlertMessageNoGps();
			} else {
				new BackgroundSplashTask().execute();

			}
		} else {
			AlertDialog alertbox4 = new AlertDialog.Builder(getActivity())
					.create();

			alertbox4
					.setMessage("No internet  Connection available ,try again!");

			alertbox4.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							arg0.dismiss();
						}
					});

			alertbox4.show();
		}
	}

	private void buildAlertMessageNoGps() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("GPS  settings");
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

		// Setting Dialog Message
		alertDialog
				.setMessage("Please allow  to access your location. Turn it ON from Location services.");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivityForResult(new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
						dialog.dismiss();

					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();// Cancel the Settings dialog
						getActivity().finish();
					}
				});

		alertDialog.show();
	}

    public static String getTitle() {
        return titleText;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, imageSource);
        outState.getInt(KEY_CONTENT, imageName);
    }
    
    public class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pg.setVisibility(View.VISIBLE);

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// session0 = new loginsession(getApplicationContext());
			// notificationDaily();
			if (getActivity().getIntent().getBooleanExtra("EXIT", false)) {
				getActivity().finish();
			}
			// session0.clearSession();
			try {
				Thread.sleep(SPLASH_SHOW_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pg.setVisibility(View.GONE);
			Intent in = new Intent(getContext(), MainActivity.class);
			in.putExtra("buttonname", buttonnm);
			startActivity(in);
			getActivity().finish();
		}
	}
	public final boolean isInternetOn() {
		// get Connectivity Manager object to check connection
		@SuppressWarnings("static-access")
		ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);
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
			Toast.makeText(getActivity(), " Not Connected ", Toast.LENGTH_LONG).show();
			return false;
		}
		return false;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

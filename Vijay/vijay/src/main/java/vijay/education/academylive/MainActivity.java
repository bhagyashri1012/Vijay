package vijay.education.academylive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements LocationListener
		 {

	//GoogleMap mGoogleMap;
	ArrayList<LatLng> mMarkerPoints;
	double mLatitude = 0;
	double mLongitude = 0;
	private Marker myLocationMarker;
	private Location loc;
	Uri gmmIntentUri;
	String locname;
	Fragment fr = null;
	@SuppressLint("NewApi")
	FragmentManager fm = getFragmentManager();
	@SuppressLint({ "NewApi", "CommitTransaction" })
	FragmentTransaction fragmentTransaction = fm.beginTransaction();
			 private static final int PERMISSION_REQUEST_CODE = 1;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

		Bundle extras;
		String newString = null;
		if (savedInstanceState == null) {
			// fetching extra data passed with intents in a Bundle type variable
			extras = getIntent().getExtras();
			if (extras == null) {
				newString = null;
			} else { /* fetching the string passed with intent using extras */
				try {
					newString = extras.getString("buttonname");
					if (newString.equals("button2")) {
						mLatitude = 22.7011034;
						mLongitude = 75.837634;
						locname="Ranjeet Hanumaan, Indore, Madhya Pradesh";
					} else if (newString.equals("button1")) {
						mLatitude = 22.7180249;
						mLongitude = 75.8830783;
						locname="Gita Bhawan, Indore, Madhya Pradesh";
					} else if (newString.equals("button3")) {
						mLatitude =22.5614925;
						mLongitude =75.6929443;
						locname="vijay education academy ,132, Main Street, Mhow, Madhya Pradesh";
					} else if (newString.equals("button4")) {
						// Log.e("butn4", newString);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		getLoc();

	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception dwnldin url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/** A class to download data from Google Directions URL */
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}

			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			//ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			//parserTask.execute(result);

		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void drawMarker(LatLng point) {
		mMarkerPoints.add(point);

		// Creating MarkerOptions
		MarkerOptions options = new MarkerOptions();

		// Setting the position of the marker
		options.position(point);

		/**
		 * For the start location, the color of marker is GREEN and for the end
		 * location, the color of marker is RED.
		 */
		if (mMarkerPoints.size() == 1) {
			options.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		} else if (mMarkerPoints.size() > 1) {
			myLocationMarker.remove();
			mMarkerPoints.clear();
		}

		// Add new marker to the Google Map Android API V2
		//mGoogleMap.addMarker(options);
	}

	@Override
	public void onLocationChanged(Location location) {
		// Draw the marker, if destination location is not set
		myLocationMarker.remove();
		if (mMarkerPoints.size() < 1) {

			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			LatLng point = new LatLng(mLatitude, mLongitude);

			//mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
			//mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

			drawMarker(point);
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		  Log.e("in result","onProviderEnabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		  Log.e("in result","onStatusChanged"+status);
	}

	/*@Override
	public void onMyLocationChange(Location location) {
		// TODO Auto-generated method stub
		loc = location;
		Log.d("location changed 11111", "location: " + location);
		// myLocationMarker.remove();
		if (myLocationMarker != null) {
			myLocationMarker.remove();
		}

		// Add a new marker object at the new (My Location dot) location
		myLocationMarker = mGoogleMap.addMarker(new MarkerOptions()
				.position(new LatLng(location.getLatitude(), location
						.getLongitude())));
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location
						.getLongitude())).zoom(12).build();
		if (mMarkerPoints.size() < 1) {

			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			LatLng point = new LatLng(mLatitude, mLongitude);

			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

			drawMarker(point);
		} else if (mMarkerPoints.size() > 1) {
			myLocationMarker.remove();
			mMarkerPoints.clear();
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			LatLng point = new LatLng(mLatitude, mLongitude);

			mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
			mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

			drawMarker(point);

		}
	}
*/
	private void buildAlertMessageNoGps() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS  settings");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);

        // Setting Dialog Message
        alertDialog.setMessage("Please allow  to access your location. Turn it ON from Location services.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
            	 startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),1);
            	 dialog.dismiss();

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();//Cancel the Settings dialog
                finish();
            }
        });

        alertDialog.show();
	/*	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
							//	startActivity(new Intent(
							//.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();*/

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//Log.e("on onBackPressed","dashhhbord");
		Intent in = new Intent(MainActivity.this, NavigationDrawerActivity.class);
		//in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(in);
		finish();
	}



	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);

	        if (resultCode == 0) {
	           switch (requestCode) {
	              case 1:
	                //onRestart();
					  Log.e("on activity result","dashhhbord");
	                startActivity(new Intent(MainActivity.this,NavigationDrawerActivity.class));
	                break;
	            }
	         }
	     }

	    @Override
	    protected void onResume() {
	    	// TODO Auto-generated method stub
	    	super.onResume();
	    	// LOCATION_SERVICE
	    				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    	if (!locationManager

					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				buildAlertMessageNoGps();
				//getLoc();
			}

			}

	    @Override
	    protected void onPause() {
	    	// TODO Auto-generated method stub
			Log.e("on ps","dashhhbord");
	    	super.onPause();

	    }

			 @Override
			 protected void onStop() {
				 super.onStop();
				 Log.e("on stp","dashhhbord");

			 }

			 @SuppressLint("NewApi")
			public void getLoc()
	    	{
	    		// Getting Google Play availability status
	    		int status =  GoogleApiAvailability.getInstance()
	    				.isGooglePlayServicesAvailable(getBaseContext());

	    		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
	    													// not available
	    			int requestCode = 10;
	    			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
	    					requestCode);
	    			dialog.show();

	    		} else { // Google Play Services are available
	    			// Initializing
	    			mMarkerPoints = new ArrayList<LatLng>();
	    			// Getting reference to SupportMapFragment of the activity_main
	    			// SupportMapFragment fm = (SupportMapFragment)
	    			// getSupportFragmentManager()
	    			// .findFragmentById(R.id.map);
	    			// Getting Map for the SupportMapFragment
	    			/*mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
	    					R.id.map)).getMap();
	    			mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
	    			mGoogleMap.getUiSettings().setCompassEnabled(true);
	    			mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
	    			mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
	    			mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
	    			mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
	    			mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
	    			mGoogleMap.setMyLocationEnabled(true);
	    			// mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
	    			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	    			// Enable MyLocation Button in the Map
	    			mGoogleMap.setMyLocationEnabled(true);
	    			*/// Getting LocationManager object from System Service
	    			// LOCATION_SERVICE
	    			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    			if (!locationManager
	    					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	    				buildAlertMessageNoGps();

	    			} else {
	    				//GPSTracker gps = new GPSTracker(MainActivity.this);
	    				final Location loc = getLocation();
	    				if (loc == null) {

	    					/*
	    					 * // Creating a criteria object to retrieve provider
	    					 * Criteria criteria = new Criteria(); // Getting the name
	    					 * of the best provider String provider =
	    					 * locationManager.getBestProvider(criteria, true); //
	    					 * Getting Current Location From GPS Location location =
	    					 * locationManager .getLastKnownLocation(provider);
	    					 */
	    					//mGoogleMap.setOnMyLocationChangeListener(this);
	    					// locationManager.requestLocationUpdates(provider, 20000,
	    					// 0,
	    					// this);
	    					// mGoogleMap.setOnMapClickListener(Main);
	    				} else {
	    					if (myLocationMarker != null) {
	    						myLocationMarker.remove();
	    					}
	    					// Add a new marker object at the new (My Location dot)
	    					// location
	    					//myLocationMarker = mGoogleMap.addMarker(new MarkerOptions()
	    						//	.position(new LatLng(loc.getLatitude(), loc
	    							//		.getLongitude())));
	    					CameraPosition cameraPosition = new CameraPosition.Builder()
	    							.target(new LatLng(loc.getLatitude(), loc
	    									.getLongitude())).zoom(12).build();

	    					//mGoogleMap.animateCamera(CameraUpdateFactory
	    						//	.newCameraPosition(cameraPosition));

	    					// Setting onclick event listener for the map
	    					// mGoogleMap.setOnMapClickListener(new OnMapClickListener()
	    					// {

	    					// @Override
	    					// public void onMapClick(LatLng point) {

	    					// Already map contain destination location
	    					/*
	    					 * if (mMarkerPoints.size() > 1) {
	    					 * //myLocationMarker.remove(); //mMarkerPoints.clear();
	    					 * //mGoogleMap.clear(); LatLng startPoint = new
	    					 * LatLng(loc.getLatitude(), loc.getLongitude()); //LatLng
	    					 * startPoint = new LatLng(mLatitude, // mLongitude);
	    					 * drawMarker(startPoint); }
	    					 *
	    					 * drawMarker(startPoint);
	    					 */
	    					// Checks, whether start and end locations are
	    					// captured
	    					if (mMarkerPoints.size() <= 1) {
	    						Log.e("mMa <= 1", "innnnnnnn");
	    						// LatLng origin = mMarkerPoints.get(0);
	    						LatLng startPoint = new LatLng(loc.getLatitude(),
	    								loc.getLongitude());
	    						LatLng dest = new LatLng(mLatitude, mLongitude);
	    						// mMarkerPoints.get(0);

	    						// Getting URL to the Google Directions API
	    						String url = getDirectionsUrl(startPoint, dest);

	    						DownloadTask downloadTask = new DownloadTask();

	    						// Start downloading json data from Google
	    						// Directions
	    						// API
	    						downloadTask.execute(url);
	    						Log.e("latlongUri===", url);

	    						 gmmIntentUri = Uri.parse("google.navigation:q="
	    								+ dest.latitude + "," + dest.longitude+"("+locname+")");
	    						Intent mapIntent = new Intent(Intent.ACTION_VIEW,
	    								gmmIntentUri);
	    						mapIntent.setPackage("com.google.android.apps.maps");
								startActivityForResult(mapIntent,1);
	    						//startActivity(mapIntent);
	    					//	mGoogleMap.addTileOverlay(new TileOverlayOptions()
	    					//			.tileProvider(new CustomMapTileProvider(
	    					//					getResources().getAssets())));
	    					}

	    					// }
	    					// });

	    				}
	    			}

	    		}
	    	}


			 public void requestPermission(){

				 if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){

					// Toast.makeText(this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

				 } else {

					 ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
				 }
			 }

			 @Override
			 public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
				 switch (requestCode) {
					 case PERMISSION_REQUEST_CODE:
						 PackageManager pm = this.getPackageManager();

						 if (grantResults.length > 0 && grantResults[0] == pm.PERMISSION_GRANTED) {
					//		 Toast.makeText(MainActivity.this,"Permission Granted, Now you can access location data.",Toast.LENGTH_LONG).show();

						 } else {
					//		 Toast.makeText(MainActivity.this,"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

						 }
						 break;
				 }
			 }

			 // flag for GPS status
			 boolean isGPSEnabled = false;

			 // flag for network status
			 boolean isNetworkEnabled = false;

			 // flag for GPS status
			 boolean canGetLocation = false;

			 Location location; // location
			 double latitude; // latitude
			 double longitude; // longitude

			 // The minimum distance to change Updates in meters
			 private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

			 // The minimum time between updates in milliseconds
			 private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

			 // Declaring a Location Manager
			 protected LocationManager locationManager;

			 public Location getLocation() {
				 try {
					 if (!checkPermission()) {
						 requestPermission();
					 }
					 else
					 {
					 }

					 locationManager = (LocationManager) this
							 .getSystemService(LOCATION_SERVICE);

					 // getting GPS status
					 isGPSEnabled = locationManager
							 .isProviderEnabled(LocationManager.GPS_PROVIDER);

					 // getting network status
					 isNetworkEnabled = locationManager
							 .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

					 if (!isGPSEnabled && !isNetworkEnabled) {
						 // no network provider is enabled

					 } else {
						 this.canGetLocation = true;
						 // First get location from Network Provider
						 if (isNetworkEnabled) {
							 if (!checkPermission()) {
								 requestPermission();
							 }
							 else
							 {
							 }

							 locationManager.requestLocationUpdates(
									 LocationManager.NETWORK_PROVIDER,
									 MIN_TIME_BW_UPDATES,
									 MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
							 Log.d("Network", "Network");
							 if (locationManager != null) {
								 location = locationManager
										 .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								 if (location != null) {
									 latitude = location.getLatitude();
									 longitude = location.getLongitude();
								 }
							 }
						 }
						 // if GPS Enabled get lat/long using GPS Services
						 if (isGPSEnabled) {
							 if (location == null) {
								 locationManager.requestLocationUpdates(
										 LocationManager.GPS_PROVIDER,
										 MIN_TIME_BW_UPDATES,
										 MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
								 Log.d("GPS Enabled", "GPS Enabled");
								 if (locationManager != null) {
									 location = locationManager
											 .getLastKnownLocation(LocationManager.GPS_PROVIDER);
									 if (location != null) {
										 latitude = location.getLatitude();
										 longitude = location.getLongitude();
									 }
								 }
							 }
						 }
					 }

				 } catch (Exception e) {
					 e.printStackTrace();
				 }

				 return location;
			 }
			 private boolean checkPermission(){
				 PackageManager pm = this.getPackageManager();

				 int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
				 if (result == pm.PERMISSION_GRANTED){

					 return true;

				 } else {

					 return false;

				 }
			 }


		 }
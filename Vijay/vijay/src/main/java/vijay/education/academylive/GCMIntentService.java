package vijay.education.academylive;

/**
 * @author Bhagyashri Burade: 03/04/2016
 *
 */
import static vijay.education.academylive.CommonUtilities.SENDER_ID;
import static vijay.education.academylive.CommonUtilities.displayMessage;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import vijay.education.academylive.model.VijayNotificationData;
import vijay.education.academylive.sqlite.NotificatnDataRepository;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

@SuppressLint("NewApi") public class GCMIntentService extends GCMBaseIntentService {
	static NotificationManager manager;
	private static final String TAG = "GCMIntentService";
	static int mNotifCount = 0;
	private static ArrayList<VijayNotificationData> feedData = new ArrayList<VijayNotificationData>();

	static ArrayList<String> projectnm= new ArrayList<String>();
	static ArrayList<String> projectndt = new ArrayList<String>();
	private long rowinserted;
	
	public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM","");
        Log.d("NAME", NotificationCountBadgeAndroidActionBar.name);
        ServerUtilities.register(context, NavigationDrawerActivity.name, NavigationDrawerActivity.email, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered),"");
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        int numMessages=0;
        String message = intent.getExtras().getString("price");
        
        if(message!=null)
        {
        long yourmilliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy  HH:mm");    
        Date resultdate = new Date(yourmilliseconds);
       // Log.e("time==",sdf.format(resultdate));
        VijayNotificationData noti=new VijayNotificationData();
        noti.setNotificatn_date(sdf.format(resultdate)+"");
        noti.setNotificatn_my(message);
        noti.setNotificatn_count("0");
        feedData.add(noti);
       
		 mNotifCount=feedData.size();
		 intent.putExtra("msg", message);
		 intent.putExtra("time", sdf.format(resultdate));
        displayMessage(context, message,sdf.format(resultdate));
        NotificatnDataRepository dbRepo;
		try {
		//	TblWellObservatnData ttt=dbRepo.getDataTable2Record_Count();
			// inserting data
			dbRepo = new NotificatnDataRepository(context);
		//dbRepo.deleteAllTbl2();
			//for (int i = 0; i < mNotifCount; i++) {
			rowinserted=dbRepo.insertDataTableNotification(noti);
			 ArrayList<VijayNotificationData> projectData = new ArrayList<VijayNotificationData>();
			projectData=dbRepo.getAllData();
			for (VijayNotificationData nm : projectData) {
				projectnm.add(nm.getNotificatn_my());
				projectndt.add(nm.getNotificatn_date());
			}
			//}
			if (rowinserted>0) {
				
			}
		//	Log.e("count=============", ttt.getRecordCount()+"");
		//	NotificatnDataRepository.insertDataTable2RecordCount(ttt.getRecordCount());
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // notifies user
        generateNotification(context, message,numMessages,sdf.format(resultdate));
        }
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        //Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message,"");
        // notifies user
        generateNotification(context, message,0,"");
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        //Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId),"");
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
       // Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId),"");
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     * @param numMessages 
     */
   private static void generateNotification(Context context, String message, int numMessages,String date) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        Intent notificationIntent = new Intent(context, NotificationDetails.class);
        // set intent so it does not start a new activity
        //projectnm.add(message);
       // projectndt.add(date);
        manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
       // notificationIntent.putStringArrayListExtra("msg", projectnm);
       // notificationIntent.putStringArrayListExtra("time", projectndt);
       // notificationIntent.putExtra("count", projectnm.size());
        notificationIntent.putExtra("notification", message);
        notificationIntent.putExtra("notificationdt", date);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);
        builder.setAutoCancel(true);
        builder.setTicker("Vijay");
        builder.setContentTitle("Vijay");               
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.valogo1);
        builder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.valogo1));
        //builder.setSmallIcon(R.drawable.valogo1);
        builder.setWhen(System.currentTimeMillis());
        builder.setOngoing(true);
        //builder.setSubText("click here to view details");   //API level 16
        //builder.setNumber(100);
        builder.setContentIntent(pendingIntent);
       
        Notification n = builder.build();
        n.number=numMessages++;	
        //myNotication = builder.getNotification();
        //myNotication.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
       // myNotication.defaults |= Notification.DEFAULT_SOUND;
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        // Vibrate if vibrate is enabled
       // myNotication.defaults |= Notification.DEFAULT_VIBRATE;
        manager.notify(0, n);
        	
     /*   NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        
        String title = context.getString(R.string.app_name);
        
        Intent notificationIntent = new Intent(context, NotificationCountBadgeAndroidActionBar.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      */
      
    }
}

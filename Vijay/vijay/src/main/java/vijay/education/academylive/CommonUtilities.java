package vijay.education.academylive;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	public static String PDF_URL="http://appapi.vijayquick.com/Vijay_Education_Academy.pdf";
	public static String IMG_URL="http://fzs.sve-mo.ba/sites/default/files/dokumenti-vijesti/sample.pdf";
	public static String WEBSITE_URL="http://www.vijayeducationacademy.com";
	public static String ADDMISSION_URL="http://admission.vijayacademy.com/";
    public static String STUDENT_URL="http://academyms.vijayacademy.com/student";
	public static String VIDEO_URL="https://www.youtube.com/channel/UCK2rV3DzCH_GH0zZ2G4YkeQ";
	public static String NOTIFICATION_URL="http://appapi.vijayquick.com/gcm_server/notification.php";
    //public static final String FIREBASE_SERVER_URL="http://appapi.vijayquick.com/server_firebase/admin.php";
	// give your server registration url here
	public static final String SERVER_URL = "http://appapi.vijayquick.com/gcm_server/register.php";
	// Google project id
	public static final String SENDER_ID = "979202588794";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "Vijay";

	public static final String DISPLAY_MESSAGE_ACTION =
			"vijay.education.academylive.DISPLAY_MESSAGE";

	public static final String EXTRA_MESSAGE = "message";
	public static final String EXTRA_MESSAGED = "date";
	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by
	 * the UI and the background service.
	 *
	 * @param context application's context.
	 * @param message message to be displayed.
	 */
	static void displayMessage(Context context, String message, String date) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		intent.putExtra(EXTRA_MESSAGED, date);
		context.sendBroadcast(intent);
	}
}

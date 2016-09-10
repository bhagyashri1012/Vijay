package vijay.education.academylive.sqlite;

/**
 * @author Bhagyashri Burade: 03/04/2016
 *
 */
import java.io.File;

import vijay.education.academylive.model.VijayNotificationData;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "vijayEduDB";
	String DB_FILEPATH;
	 static final String DATABASE_NAMEB = Environment
			.getExternalStorageDirectory() + "/vijay";
	private static SQLiteHelper instance;

	/*
	 * public void backupDatabase() throws IOException {
	 * 
	 * if (isSDCardWriteable()) { // Open your local db as the input stream
	 * String inFileName = DB_FILEPATH; File dbFile = new File(inFileName);
	 * FileInputStream fis = new FileInputStream(dbFile);
	 * 
	 * String outFileName = Environment.getExternalStorageDirectory() +
	 * "/wellDataBackup"; // Open the empty db as the output stream //
	 * Log.i("newPath:",outFileName); OutputStream output = new
	 * FileOutputStream(outFileName); // transfer bytes from the inputfile to
	 * the outputfile byte[] buffer = new byte[1024]; int length; while ((length
	 * = fis.read(buffer)) > 0) { output.write(buffer, 0, length); } // Close
	 * the streams output.flush(); output.close(); fis.close(); } } private
	 * boolean isSDCardWriteable() { boolean rc = false; String state =
	 * Environment.getExternalStorageState(); if
	 * (Environment.MEDIA_MOUNTED.equals(state)) { rc = true; } return rc; }
	 */

	public SQLiteHelper(Context context) {
		// TODO Auto-generated method stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		final String packageName = context.getPackageName();
		DB_FILEPATH = "/data/data/" + packageName + "/databases/"
				+ DATABASE_NAME;
		File path = context.getDatabasePath(DATABASE_NAME);
		String db_path = path.getAbsolutePath();
		Log.i("Path:", db_path);
	}

	public static synchronized SQLiteHelper getHelper(Context context) {
		if (instance == null)
			instance = new SQLiteHelper(context);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// SQL statement to create book table
		String CREATE_TABLE_TBLNOTIFICATION = "CREATE TABLE "
				+ VijayNotificationData.TABLE + "("
				+ VijayNotificationData.col_id +" INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ VijayNotificationData.col_NotificationDate + " TEXT  NOT NULL,"
				+ VijayNotificationData.col_Notification + " TEXT NOT NULL,"
					+ VijayNotificationData.col_NotificationCount + " INTEGER  NOT NULL)";

		// create books table
		db.execSQL(CREATE_TABLE_TBLNOTIFICATION);
		Log.e("in SqlliteHelper", "table Notification_data created");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older table if existed, all data will be gone!!!
		db.execSQL("DROP TABLE IF EXISTS " + VijayNotificationData.TABLE);
		// Create tables again
		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

}

package vijay.education.academylive.sqlite;

/**
 * @author Bhagyashri Burade: 25/11/2015
 */

import java.sql.SQLException;
import java.util.ArrayList;

import vijay.education.academylive.model.VijayNotificationData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NotificatnDataRepository {
    private static SQLiteHelper dbHelper;
    private SQLiteDatabase dbase;
    private Context mContext;

    public NotificatnDataRepository(Context context) throws SQLException {
        dbHelper = SQLiteHelper.getHelper(context);
        open();
    }

    public void open() throws SQLException {
        if (dbHelper == null)
            dbHelper = SQLiteHelper.getHelper(mContext);
        dbase = dbHelper.getWritableDatabase();
    }

    // Adding new row
    public long insertDataTableNotification(VijayNotificationData NotiDataMain) {

        // Log.d("addBook", feedDataMain.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(VijayNotificationData.col_NotificationDate,
                NotiDataMain.getNotificatn_date()); // get
        values.put(VijayNotificationData.col_Notification,
                NotiDataMain.getNotificatn_my());
        values.put(VijayNotificationData.col_NotificationCount,
                NotiDataMain.getNotificatn_count());
        try {
            // 3. insert
            long row = db.insert(VijayNotificationData.TABLE, null, values);
            // 4. close
            Log.e("in Repository", "data inserted");
            db.close();
            return row;
        } catch (Exception e) {
            return 0;
        }
    }

    public long updateDataTableNotification(VijayNotificationData NotiDataMain) {

        // Log.d("addBook", feedDataMain.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put(VijayNotificationData.col_NotificationCount,
                "1");
        try {
            String[] args = new String[]{NotiDataMain.getNotificatn_my()};
            Log.e("in Repository", "data =" + NotiDataMain.getNotificatn_my());
            // 3. update
            long row = db.update(VijayNotificationData.TABLE, values, " notificatn_my =? ", args);
            // 4. close
            Log.e("in Repository", "data updated");
            db.close();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getCOUNT() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + VijayNotificationData.TABLE + " Where notificatn_count = 0";
        dbase = dbHelper.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
      //  Log.e("cursor welldatarepository", cursor.getCount() + "");
        // return quest list
        return cursor.getCount();
    }

    public String getUnreadNoti(String msg) {
        VijayNotificationData quest = new VijayNotificationData();
        // Select All Query
        String selectQuery = "SELECT notificatn_count FROM " + VijayNotificationData.TABLE + " Where notificatn_my = '" + msg + "'";
        dbase = dbHelper.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
      //  Log.e("cursor welldatarepository", cursor.getCount() + "");
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {

                    quest.setNotificatn_count(cursor.getString(cursor
                            .getColumnIndex("notificatn_count")) + "");

                } while (cursor.moveToNext());
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // return quest list
        return quest.getNotificatn_count();
    }

    public ArrayList<VijayNotificationData> getAllData() {
        ArrayList<VijayNotificationData> quesList = new ArrayList<VijayNotificationData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + VijayNotificationData.TABLE;
        dbase = dbHelper.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
      //  Log.e("cursor welldatarepository", cursor.getCount() + "");
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    VijayNotificationData quest = new VijayNotificationData();
                    quest.setNotificatn_date(cursor.getString(cursor
                            .getColumnIndex("notificatn_date")) + "");
                    quest.setNotificatn_my(cursor.getString(cursor
                            .getColumnIndex("notificatn_my")));

                    quesList.add(quest);
                } while (cursor.moveToNext());
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // return quest list
        return quesList;
    }

    public Cursor rowcolcount(String dyno) {
        // int row = 0;
        // Log.e("dyno welldatarepository", dyno);
        String selectQuery = "SELECT  * FROM " + VijayNotificationData.TABLE;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // row = cursor.getCount();

        return cursor;
    }

    public int deleteAllTbl2() {

                return dbase.delete(VijayNotificationData.TABLE, null, null);


    }


}

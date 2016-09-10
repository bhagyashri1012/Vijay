package vijay.education.academylive;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Bhagyashri Burade (bhagyashri_sahare@yahoo.com) .
 * Date : 22/02/2016.
 * Copyright (c)2016  Vijay Education Academy. All rights reserved.
 */
public class MySharedPreferences {
    private SharedPreferences prefs;
    private Context context;
    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String SET_NOTIFY = "set_notify";
    public MySharedPreferences(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(FIREBASE_CLOUD_MESSAGING, Context.MODE_PRIVATE);
    }
    public void saveNotificationSubscription(boolean value){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putBoolean(SET_NOTIFY, value);
        edits.apply();
    }
    public boolean hasUserSubscribeToNotification(){
        return prefs.getBoolean(SET_NOTIFY, false);
    }
    public void clearAllSubscriptions(){
        prefs.edit().clear().apply();
    }
}

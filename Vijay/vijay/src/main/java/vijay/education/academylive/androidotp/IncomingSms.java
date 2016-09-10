package vijay.education.academylive.androidotp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


public class IncomingSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    @SuppressWarnings("deprecation")
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Log.e("msg", message);
                    try {
                        if (senderNum.equals("DZ-VIJAYE")) {
                            ConfirmOtpActivity Sms = new ConfirmOtpActivity();
                            Sms.recivedSms(message.substring(35, 41).toString());
                                if(Sms==null) {
                                    LoginActivity login = new LoginActivity();
                                    login.recivedSms(message.substring(35, 41).toString());
                                    LoginConfirmActivity Sms1 = new LoginConfirmActivity();
                                    Sms1.recivedSms(message.substring(35, 41).toString());
                                }
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
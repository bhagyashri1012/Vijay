package vijay.education.academylive.androidotp;

/**
 * Created by Bhagyashri on 15/03/2016
 */
public class Config {
    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://appapi.vijayquick.com/smsverification/register.php";
    public static final String CONFIRM_URL = "http://appapi.vijayquick.com/smsverification/confirm.php";
    public static final String ROOMINFO_URL = "http://appapi.vijayquick.com/webservice/roominfo.php";
    public static final String CAMERAINFO_URL = "http://appapi.vijayquick.com/webservice/camerainfo.php";
    public static final String REGISTER_URLONETIME = "http://appapi.vijayquick.com/smsverification/onetimeregister.php";
    public static final String CONFIRM_URLONETIME = "http://appapi.vijayquick.com/smsverification/onetimeconfirmotp.php";
    public static String STUDENTID_URL = "http://vijayeducationacademy.com/app/index?StudentId=";
    public static String SAVE_FEEDBACK = "http://appapi.vijayquick.com/webservice/feedback.php";
    //Keys to send username, password, phone and otp
    public static final String KEY_USERID = "userid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_FATHERNAME = "fathername";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_OTP = "otp";
    public static final String KEY_ROOMID = "room_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_FEEDBACK = "feedback";
    //JSON Tag from response from server
    public static final String TAG_RESPONSE = "ErrorMessage";
}

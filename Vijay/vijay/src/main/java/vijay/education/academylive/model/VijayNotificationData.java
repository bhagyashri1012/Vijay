package vijay.education.academylive.model;


/**
 * @author Bhagyashri Burade: 03/04/2016
 *
 */
public class VijayNotificationData {
	//tble 1
	private String id;
	private String notificatn_my;
	private String notificatn_date;
//table 2
	private String notificatn_count;
	
	public VijayNotificationData() {
		// TODO Auto-generated constructor stub
	}

	public VijayNotificationData(String notificatn, String string) {
		notificatn_my=notificatn;
		setNotificatn_date(string);
	}
	// Labels table name
	public static final String TABLE = "Notification_data";
	// Labels Table Columns names
	public static final String col_id = "id";
	public static final String col_Notification = "notificatn_my";
	public static final String col_NotificationDate = "notificatn_date";
	public static final String col_NotificationCount = "notificatn_count";

	public String getNotificatn_my() {
		return notificatn_my;
	}

	public void setNotificatn_my(String notificatn_my) {
		this.notificatn_my = notificatn_my;
	}

	public String getNotificatn_date() {
		return notificatn_date;
	}

	public void setNotificatn_date(String notificatn_date) {
		this.notificatn_date = notificatn_date;
	}

	public String getNotificatn_count() {
		return notificatn_count;
	}

	public void setNotificatn_count(String notificatn_count) {
		this.notificatn_count = notificatn_count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

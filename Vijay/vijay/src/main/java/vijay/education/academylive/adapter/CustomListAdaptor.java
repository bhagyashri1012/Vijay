package vijay.education.academylive.adapter;

import java.sql.SQLException;
import java.util.ArrayList;

import vijay.education.academylive.NotificationDetails;
import vijay.education.academylive.R;
import vijay.education.academylive.sqlite.NotificatnDataRepository;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdaptor extends ArrayAdapter<String> {
	Context context;
	int layoutResourceId;
	ArrayList<String> students = new ArrayList<String>();
	ArrayList<String> studentsdt = new ArrayList<String>();
	NotiWrapper StudentWrapper = null;
	private NotificatnDataRepository dbRepo;
	public CustomListAdaptor(Context context, int layoutResourceId,
			ArrayList<String> strnoti, ArrayList<String> feedDataDt) {
		super(context, layoutResourceId, strnoti);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.students = strnoti;
		studentsdt=feedDataDt;
	}

	@Override
	public View getView(final int position, final View convertView,
			ViewGroup parent) {
		View item = convertView;
		

		if (item == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			item = inflater.inflate(layoutResourceId, parent, false);
			StudentWrapper = new NotiWrapper();
			StudentWrapper.noti = (TextView) item
					.findViewById(R.id.textviewNoti);
			StudentWrapper.time = (TextView) item.findViewById(R.id.textviewNotiTitle);
			item.setTag(StudentWrapper);
		} else {
			StudentWrapper = (NotiWrapper) item.getTag();
		}
		final String candi = students.get(position);
		final String candidt = studentsdt.get(position);
		StudentWrapper.noti.setText(candi);
		StudentWrapper.time.setText(candidt);
		try {
			dbRepo = new NotificatnDataRepository(context);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (dbRepo.getUnreadNoti(candi).equals("0")) {
			StudentWrapper.noti.setTextColor(Color.BLACK);
		} else {
			StudentWrapper.noti.setTextColor(Color.GRAY);
		}
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//Toast.makeText(v.getContext(), "nnn="+candi+candidt, Toast.LENGTH_LONG).show();
				Intent in = new Intent(v.getContext(), NotificationDetails.class);
				in.putExtra("notification", candi);
				in.putExtra("notificationdt", candidt);
				//countNoti--; 
				v.getContext().startActivity(in);
				((Activity)v.getContext()).finish();
			}
		});

		return item;

	}

	static class NotiWrapper {
		public TextView time;
		TextView noti;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}


}

package vijay.education.academylive.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vijay.education.academylive.R;

public class GridViewAdapter2 extends BaseAdapter {
	private Context mContext;

	// Keep all Images in array
	public ArrayList<String> mThumbIds;

	// Constructor
	public GridViewAdapter2(Context c, ArrayList<String> mThumbIds2) {
		mContext = c;
		this.mThumbIds = mThumbIds2;
	}
	public GridViewAdapter2() {
	}

	@Override
	public int getCount() {
		return mThumbIds.size();
	}

	@Override
	public Object getItem(int position) {
		return mThumbIds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout lineraLay= new LinearLayout(mContext);
		lineraLay.setBackgroundResource(R.drawable.image_border);
		lineraLay.setPadding(15, 15, 15, 15);
		lineraLay.setBackgroundColor(Color.WHITE);
		lineraLay.setOrientation(LinearLayout.VERTICAL);
		ImageView imgView=new ImageView(mContext);
		imgView.setLayoutParams(new GridView.LayoutParams(
				LayoutParams.MATCH_PARENT, 220));
		lineraLay.addView(imgView);
		imgView.setImageResource(R.drawable.classroom);
		TextView textView = new TextView(mContext);
		textView.setText(mThumbIds.get(position));
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundResource(R.color.white);
		textView.setLayoutParams(new GridView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		lineraLay.addView(textView);
		return lineraLay;
	}

}
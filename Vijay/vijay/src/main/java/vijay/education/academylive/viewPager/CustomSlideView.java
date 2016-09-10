package vijay.education.academylive.viewPager;



import vijay.education.academylive.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
public class CustomSlideView extends FragmentActivity {

	ViewPagerFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());

	        mPager = (ViewPager)findViewById(R.id.pager);
	        mPager.setAdapter(mAdapter);

	        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
	        mIndicator = indicator;
	        indicator.setViewPager(mPager);

	        final float density = getResources().getDisplayMetrics().density;
	        indicator.setBackgroundColor(0xF44336);
	        indicator.setRadius(10 * density);
	        indicator.setPageColor(0xFF888888);
	        indicator.setFillColor(0xF44336);
	        indicator.setStrokeColor(0xFF000000);
	        indicator.setStrokeWidth(2 * density);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

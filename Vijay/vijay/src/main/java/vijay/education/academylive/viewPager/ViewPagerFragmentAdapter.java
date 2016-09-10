package vijay.education.academylive.viewPager;

import vijay.education.academylive.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter  {
    protected static final int[] CONTENT = new int[] { R.string.frg1, R.string.frg2, R.string.frg3, };
    private int[] offerImages = {
			R.drawable.geeta2569,
			R.drawable.ranjeet_hanuman_2569,
			R.drawable.mmm9
	};

    private int mCount = CONTENT.length;

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ViewPagerFragment(offerImages[position],CONTENT[position]);
    }

    @Override
    public int getCount() {
        return mCount;
    }
    
    
   /* @Override
    public CharSequence getPageTitle(int position) {
      return ViewPagerFragmentAdapter.CONTENT[position % CONTENT.length];
    }
*/
   
    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	// TODO Auto-generated method stub
    	super.destroyItem(container, position, object);
    }
}
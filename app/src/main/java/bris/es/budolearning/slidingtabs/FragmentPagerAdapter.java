package bris.es.budolearning.slidingtabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import bris.es.budolearning.utiles.BLSession;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    Bundle oBundle;
    FragmentManager oFragmentManager;
    ArrayList<Fragment> oPooledFragments;
    List<PagerItem> oPagerItems;

    public FragmentPagerAdapter(FragmentManager fm, List<PagerItem> pagerItems) {
        super(fm);
        oFragmentManager=fm;
        oPagerItems = pagerItems;

    }

    @Override
    public int getItemPosition(Object object) {
        Fragment oFragment=(Fragment)object;
        oPooledFragments=new ArrayList<Fragment>(oFragmentManager.getFragments());
        if(oPooledFragments.contains(oFragment))
            return POSITION_NONE;
        else
            return POSITION_UNCHANGED;
    }

    @Override
    public Fragment getItem(int i) {
        return oPagerItems.get(i).createFragment();
    }

    @Override
    public int getCount() {
        return oPagerItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return oPagerItems.get(position).getTitle();
    }

}
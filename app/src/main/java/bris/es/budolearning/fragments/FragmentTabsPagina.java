package bris.es.budolearning.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bris.es.budolearning.R;
import bris.es.budolearning.slidingtabs.FragmentPagerAdapter;
import bris.es.budolearning.slidingtabs.PagerItem;
import bris.es.budolearning.slidingtabs.SlidingTabLayout;

public class FragmentTabsPagina extends FragmentAbstract {

    public static ViewPager mViewPager;
    public static SlidingTabLayout mSlidingTabLayout;
    public static FragmentPagerAdapter mFragmentPagerAdapter;
    public static List<PagerItem> pagerItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            try {
                mViewPager.setCurrentItem(savedInstanceState.getInt("tabPaginaIndex"));
            } catch(Exception e){

            }
        }

        pagerItems = new ArrayList<>();
        pagerItems.add(new PagerItem(FragmentPagina.class, "Origenes","http://www.budolearning.es/#!Origenes"));
        pagerItems.add(new PagerItem(FragmentPagina.class, "Escuelas","http://www.budolearning.es/#!Escuelas"));
        pagerItems.add(new PagerItem(FragmentPagina.class, "Maestros","http://www.budolearning.es/#!Maestros"));
        pagerItems.add(new PagerItem(FragmentPagina.class, "Armas"   ,"http://www.budolearning.es/#!Armas"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mFragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager(), pagerItems);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mFragmentPagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        // Set a TabColorizer to customize the indicator and divider colors. Here we just retrieve
        // the tab at the position, and return it's set color
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return PagerItem.DEFAULT_INDICATOR_COLOR;
            }

            @Override
            public int getDividerColor(int position) {
                return PagerItem.DEFAULT_DIVIDER_COLOR;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabPaginaIndex", mViewPager.getCurrentItem());
    }
}

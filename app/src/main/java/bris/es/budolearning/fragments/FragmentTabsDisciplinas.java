package bris.es.budolearning.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import bris.es.budolearning.R;
import bris.es.budolearning.slidingtabs.FragmentPagerAdapter;
import bris.es.budolearning.slidingtabs.PagerItem;
import bris.es.budolearning.slidingtabs.SlidingTabLayout;
import bris.es.budolearning.utiles.BLSession;
import bris.es.budolearning.task.TaskUsuario;

public class FragmentTabsDisciplinas extends FragmentAbstract{

    public static ViewPager mViewPager;
    public static SlidingTabLayout mSlidingTabLayout;
    public static FragmentPagerAdapter mFragmentPagerAdapter;
    public TaskUsuario taskUsuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            try {
                mViewPager.setCurrentItem(savedInstanceState.getInt("tabDisciplinaIndex"));
            } catch(Exception e){

            }
        }

        if(BLSession.getInstance().getTabsDisciplinas() == null || BLSession.getInstance().getTabsDisciplinas().size() == 0) {
            BLSession.getInstance().setTabsDisciplinas(new ArrayList<PagerItem>());
            BLSession.getInstance().getTabsDisciplinas().add(new PagerItem(FragmentDisciplinas.class, "Disciplinas"));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mFragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager(), BLSession.getInstance().getTabsDisciplinas());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mFragmentPagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had it's PagerAdapter set.
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
        outState.putInt("tabDisciplinaIndex", mViewPager.getCurrentItem());
    }

}

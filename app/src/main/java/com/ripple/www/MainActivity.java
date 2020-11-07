package com.ripple.www;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ripple.www.Classes.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // View Declaration
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout mMainHolder;

    // Fragment instances to initiate the exoplayers' start stop method
    private HomeFragment mHomeFragment;
    private GlobalFragment mGlobalFragment;

    // Class declarations
    private MyUtils mMyUtils = new MyUtils();

    // Nav bar icons
    private int[] imageResId = {
            R.drawable.nav_home_icon,
            R.drawable.nav_earth_icon,
            R.drawable.nav_message_icon,
            R.drawable.nav_profile_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dark status bar text
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Connecting view with code
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mMainHolder = (LinearLayout) findViewById(R.id.main_activity_main_holder);

        // Space on top to avoid the glitch for having a transparent navbar
        mMainHolder.setPadding(0, mMyUtils.getStatusBarHeight(this), 0, 0);

        // Setting up the view pager
        mViewPager.setOffscreenPageLimit(4);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setIcon(imageResId[i]);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        break;
                    case 1:
                        if(mGlobalFragment != null)
                            mGlobalFragment.initializeExoPlayer();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;

                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                // Stopping the home exoplayers when switched to another tab besides home
                if(tab.getPosition() == 0){
                    // Called from main activity to home fragment
                    mHomeFragment.stopExoPlayers();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                }else if(tab.getPosition() == 1) {
                    // Called from main activity to global fragment
                    mGlobalFragment.releaseExoPlayer();
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                // Scrolling home recycler to top when tapped on home nav icon while in home
                if(tab.getPosition() == 0){
                    // Called from main activity to home
                    mHomeFragment.scrollToTop();

                }

            }
        });

    }

    // The viewpager class constructor
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    // Viewpager setup method
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mHomeFragment = new HomeFragment();
        mGlobalFragment = new GlobalFragment();

        viewPagerAdapter.addFrag(mHomeFragment);
        viewPagerAdapter.addFrag(mGlobalFragment);
        viewPagerAdapter.addFrag(new MessageFragment());
        viewPagerAdapter.addFrag(new ProfileFragment());
        viewPager.setAdapter(viewPagerAdapter);
    }

}

package com.ripple.www.SubActivites;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.util.Util;
import com.ripple.www.Adapters.FullscreenPostAdapter;
import com.ripple.www.Adapters.RegularPostAdapter;
import com.ripple.www.Classes.RegularPost;
import com.ripple.www.Classes.RegularPostDivider;
import com.ripple.www.R;

import java.util.ArrayList;
import java.util.List;

public class FullScreenPost extends AppCompatActivity {

    // View declarations
    private RecyclerView mRecyclerView;

    // Home recyclerview posts arraylist and adapter
    private List<RegularPost> mPostsList = new ArrayList<>();
    private FullscreenPostAdapter mRegularPostAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean mActivityPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_post);

        // Connecting view to code
        mRecyclerView = (RecyclerView) findViewById(R.id.full_screen_recycler_view);

        // Setting up Recyclerview
        mRegularPostAdapter = new FullscreenPostAdapter(mPostsList, this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRegularPostAdapter);

        // Adding pager behavior
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) { }

            // THIS FUCKER makes the exoplayers' adapter autoplay the player that is most visible (in percentage)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mRecyclerView != null){

                    LinearLayoutManager layoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
                    mRegularPostAdapter.checkPercentageAndStartStopPlayer(getPercentage(layoutManager));

                }
            }
        });

        hideSystemUI();

        addTestPosts();

    }

    // Get the percentage of visibility of a home post
    private ArrayList<Integer> getPercentage(LinearLayoutManager layoutManager){

        ArrayList<Integer> arrayList = new ArrayList<Integer>();

        try {

            final int firstPosition = layoutManager.findFirstVisibleItemPosition();
            final int lastPosition = layoutManager.findLastVisibleItemPosition();

            Rect rvRect = new Rect();
            mRecyclerView.getGlobalVisibleRect(rvRect);

            for (int i = firstPosition; i <= lastPosition; i++) {
                Rect rowRect = new Rect();
                layoutManager.findViewByPosition(i).getGlobalVisibleRect(rowRect);

                int percentFirst;
                if (rowRect.right >= rvRect.right){
                    int visibleHeightFirst =rvRect.right - rowRect.left;
                    percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(i).getHeight();
                }else {
                    int visibleHeightFirst = rowRect.right - rvRect.left;
                    percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(i).getHeight();
                }

                if (percentFirst>100)
                    percentFirst = 100;


                arrayList.add(percentFirst);
            }

        }finally {

            return arrayList;

        }

    }


    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void addTestPosts(){

        RegularPost post = new RegularPost(0,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                0, "Aneek Rahman",
                "https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg",
                "Hello, im a description" , 1999999993L,1000, 2000, 40000);
        mPostsList.add(post);

        post = new RegularPost(0,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                0, "Someone Else",
                "https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg",
                "Hello, im a description" , 1993999,1000, 2000, 40000);
        mPostsList.add(post);

        post = new RegularPost(0,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                0, "Ligma Balls",
                "https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg",
                "Hello, im a description" , 1993999,1000, 2000, 40000);
        mPostsList.add(post);

        post = new RegularPost(0,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                0, "Kochi Dab",
                "https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg",
                "Hello, im a description" , 1993999,1000, 2000, 40000);
        mPostsList.add(post);

        post = new RegularPost(0,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                0, "Vallage Naa",
                "https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg",
                "Hello, im a description" , 1993999,1000, 2000, 40000);
        mPostsList.add(post);

        mRegularPostAdapter.notifyDataSetChanged();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Fade out animation on exiting fullscreen
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(mActivityPaused) {
                mRegularPostAdapter.initExoPlayerFromFragment();
                mActivityPaused = false;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23)) {
            if(mActivityPaused) {
                mRegularPostAdapter.initExoPlayerFromFragment();
                mActivityPaused = false;
            }
        }
        hideSystemUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            mRegularPostAdapter.releaseExoPlayerFromFragment();
            mActivityPaused = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            mRegularPostAdapter.releaseExoPlayerFromFragment();
            mActivityPaused = true;
        }
    }

}

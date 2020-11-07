package com.ripple.www;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.util.Util;
import com.ripple.www.Adapters.RegularPostAdapter;
import com.ripple.www.Classes.RegularPost;
import com.ripple.www.Classes.RegularPostDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    // View declaration
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mMainHolder;

    // Home recyclerview posts arraylist and adapter
    private List<RegularPost> mPostsList = new ArrayList<>();;
    private RegularPostAdapter mRegularPostAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO check if fragpaused is needed
    private boolean mFragPaused = false;

    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        // Connecting view to code
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.home_recyclerview);
        mMainHolder = (SwipeRefreshLayout) rootview.findViewById(R.id.home_main_holder);

        // Setting up the recyclerview with adapter and layout manager
        mRegularPostAdapter = new RegularPostAdapter(mPostsList, getContext(), this);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new RegularPostDivider(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRegularPostAdapter);


        // THIS FUCKER makes the exoplayers' adapter autoplay the player that is most visible (in percentage)
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mRecyclerView != null){
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());

                    mRegularPostAdapter.checkPercentageAndStartStopPlayer(getPercentage(layoutManager));

                }
            }
        });

        // TODO remove the test posts
        addTestPosts();

        return rootview;
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
                "Hell o, im a description" , 1993999,1000, 2000, 40000);
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
                if (rowRect.bottom >= rvRect.bottom){
                    int visibleHeightFirst =rvRect.bottom - rowRect.top;
                    percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(i).getHeight();
                }else {
                    int visibleHeightFirst = rowRect.bottom - rvRect.top;
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

    // Stop all the exoplayers' in adapter when switched to another tab (Called from main activity to home fragment)
    public void stopExoPlayers(){

        if(mRegularPostAdapter != null)
            mRegularPostAdapter.stopExoPlayerInAdapter();

    }

    // Scrolling to top of recyclerview when tapped on the home icon while in home (This is called from main activity)
    public void scrollToTop(){

        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        smoothScroller.setTargetPosition(0);
        mLayoutManager.startSmoothScroll(smoothScroller);

    }

    // TODO take reference and delete the Broadcast listener block
    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent ) {
            int data = intent.getIntExtra("stop-exo", 0);

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(mFragPaused)
            mRegularPostAdapter.initExoPlayerFromFragment();
            mFragPaused = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23)) {
            if(mFragPaused)
            mRegularPostAdapter.initExoPlayerFromFragment();
            mFragPaused = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            mRegularPostAdapter.releaseExoPlayerFromFragment();
            mFragPaused = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            mRegularPostAdapter.releaseExoPlayerFromFragment();
            mFragPaused = true;
        }
    }

}

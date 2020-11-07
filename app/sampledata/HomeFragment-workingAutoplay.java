package com.ripple.www;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ripple.www.Adapters.RegularPostAdapter;
import com.ripple.www.Classes.RegularPost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mMainHolder;

    private List<RegularPost> mPostsList = new ArrayList<>();;
    private RegularPostAdapter mRegularPostAdapter;

    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.home_recyclerview);
        mMainHolder = (SwipeRefreshLayout) rootview.findViewById(R.id.home_main_holder);

        mRegularPostAdapter = new RegularPostAdapter(mPostsList, getContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRegularPostAdapter);

        RegularPost post = new RegularPost(0,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                0, "Aneek",
                "https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg",
                0, 0, 0);
        mPostsList.add(post);

        post = new RegularPost(0,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                0, "Aneek",
                "https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg",
                0, 0, 0);
        mPostsList.add(post);

        post = new RegularPost(0,
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4",
                0, "Aneek",
                "https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg",
                0, 0, 0);
        mPostsList.add(post);

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

                    mRegularPostAdapter.checkPercentageAndStartStopPlayer(getFirstPercentage(layoutManager), getLastPercentage(layoutManager));

                }
            }
        });


        mRegularPostAdapter.notifyDataSetChanged();

        return rootview;

    }

    private int getFirstPercentage(LinearLayoutManager layoutManager){

        final int firstPosition = layoutManager.findFirstVisibleItemPosition();

        Rect rvRect = new Rect();
        mRecyclerView.getGlobalVisibleRect(rvRect);

        Rect rowRect = new Rect();
        layoutManager.findViewByPosition(firstPosition).getGlobalVisibleRect(rowRect);

        int percentFirst;
        if (rowRect.bottom >= rvRect.bottom){
            int visibleHeightFirst =rvRect.bottom - rowRect.top;
            percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(firstPosition).getHeight();
        }else {
            int visibleHeightFirst = rowRect.bottom - rvRect.top;
            percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(firstPosition).getHeight();
        }

        if (percentFirst>100)
            percentFirst = 100;

        return percentFirst;

    }

    private int getLastPercentage(LinearLayoutManager layoutManager){

        final int lastPosition = layoutManager.findLastVisibleItemPosition();

        Rect rvRect = new Rect();
        mRecyclerView.getGlobalVisibleRect(rvRect);

        Rect rowRect = new Rect();
        layoutManager.findViewByPosition(lastPosition).getGlobalVisibleRect(rowRect);

        int percentFirst;
        if (rowRect.bottom >= rvRect.bottom){
            int visibleHeightFirst =rvRect.bottom - rowRect.top;
            percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(lastPosition).getHeight();
        }else {
            int visibleHeightFirst = rowRect.bottom - rvRect.top;
            percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(lastPosition).getHeight();
        }

        if (percentFirst>100)
            percentFirst = 100;

        return percentFirst;

    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }

}

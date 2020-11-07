package com.ripple.www;

import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ripple.www.Adapters.PostGridLayoutAdapter;
import com.ripple.www.Classes.GlobalPost;

import java.util.ArrayList;
import java.util.List;

public class GlobalFragment extends Fragment {

    // View decalration
    private NestedScrollView mMainScrollView;
    private RecyclerView mLeftRecyclerView, mMiddleRecyclerView,mRightRecyclerView;
    private SimpleExoPlayerView mExoPlayerView;
    private ImageView mPlayButton, mPauseButton, mMuteButton, mUnmuteButton;

    // Separate Recycler view adapters and post lists
    private PostGridLayoutAdapter mLeftRecyclerAdapter, mMiddleRecyclerAdapter ,mRightRecyclerAdapter;
    private List<GlobalPost> mLeftPosts = new ArrayList<>();
    private List<GlobalPost> mMiddlePosts = new ArrayList<>();
    private List<GlobalPost> mRightPosts = new ArrayList<>();

    // Exoplayer declarations
    private String vidAddress;
    private SimpleExoPlayer mExoPlayer;
    private MediaSource mMediaSource;
    private int mExoPlayerWindowIndex = 0;
    private long mPlaybackPosition = 0;
    private float mMutePreviousVol = 0f;

    public GlobalFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_global, container, false);

        // Connecting view to code
        mMainScrollView = (NestedScrollView) rootview.findViewById(R.id.global_main_scrollview);

        mLeftRecyclerView = (RecyclerView) rootview.findViewById(R.id.global_grid_left_recycler);
        mMiddleRecyclerView = (RecyclerView) rootview.findViewById(R.id.global_grid_middle_recycler);
        mRightRecyclerView = (RecyclerView) rootview.findViewById(R.id.global_grid_right_recycler);

        mExoPlayerView = (SimpleExoPlayerView) rootview.findViewById(R.id.global_main_exo_player);
        mPlayButton = (ImageView) rootview.findViewById(R.id.global_play_button);
        mPauseButton = (ImageView) rootview.findViewById(R.id.global_pause_button);
        mMuteButton = (ImageView) rootview.findViewById(R.id.global_mute_button);
        mUnmuteButton = (ImageView) rootview.findViewById(R.id.global_unmute_button);

        // Setting up main Global ExoPlayer
        vidAddress = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        setExoPlayerFixedHeight();

        // Setting up Recycler Views' adapter
        mLeftRecyclerAdapter = new PostGridLayoutAdapter(getContext(), mLeftPosts, 1);
        mMiddleRecyclerAdapter = new PostGridLayoutAdapter(getContext(), mMiddlePosts, 2);
        mRightRecyclerAdapter = new PostGridLayoutAdapter(getContext(), mRightPosts, 3);

        // Separate layout managers for each adapter
        LinearLayoutManager mLeftLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager mMiddleLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager mRightLayoutManager = new LinearLayoutManager(getContext());

        // Setting up left post Recycler
        mLeftRecyclerView.setLayoutManager(mLeftLayoutManager);
        mLeftRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLeftRecyclerView.setAdapter(mLeftRecyclerAdapter);
        mLeftRecyclerView.setFocusable(false);
        mLeftRecyclerView.setNestedScrollingEnabled(false);

        // Setting up middle post Recycler
        mMiddleRecyclerView.setLayoutManager(mMiddleLayoutManager);
        mMiddleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMiddleRecyclerView.setAdapter(mMiddleRecyclerAdapter);
        mMiddleRecyclerView.setFocusable(false);
        mMiddleRecyclerView.setNestedScrollingEnabled(false);

        // Setting up right post Recycler
        mRightRecyclerView.setLayoutManager(mRightLayoutManager);
        mRightRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRightRecyclerView.setAdapter(mRightRecyclerAdapter);
        mRightRecyclerView.setFocusable(false);
        mRightRecyclerView.setNestedScrollingEnabled(false);

        // TODO remove test posts
        insertPostsToAdapter();

        // Setting all the click listeners
        setAllListeners();

        return rootview;

    }

    private void insertPostsToAdapter(){
        for(int i = 0; i < 10; i++){
            GlobalPost post = new GlobalPost(0, 100, 3900);
            mLeftPosts.add(post);
            mMiddlePosts.add(post);
            mRightPosts.add(post);
        }
        mLeftRecyclerAdapter.notifyDataSetChanged();
        mMiddleRecyclerAdapter.notifyDataSetChanged();
        mRightRecyclerAdapter.notifyDataSetChanged();

    }

    private void setExoPlayerFixedHeight(){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;



        mExoPlayerView.getLayoutParams().height = (height / 7) * 6;
        mExoPlayerView.requestLayout();

    }

    // Initializing the posts exoplayer
    public void initializeExoPlayer(){

        if(mExoPlayer == null){

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mExoPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mExoPlayerWindowIndex, mPlaybackPosition);
            setMute(true);
        }

        prepareExoPlayer();

    }

    // Preparing the exoplayer with the video address
    private void prepareExoPlayer(){

        Uri uri = Uri.parse(vidAddress);
        mMediaSource = buildMediaSource(uri);
        mExoPlayer.prepare(mMediaSource, false, false);

        mExoPlayer.addListener(mExoPlayerEventListener);

    }
    // Building mediasource for exoplayer
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("ripple-exo")).createMediaSource(uri);
    }

    private void setMute(boolean mute){

        if (mute){

            mMuteButton.setVisibility(View.GONE);
            mUnmuteButton.setVisibility(View.VISIBLE);

            mMutePreviousVol = mExoPlayer.getVolume();
            mExoPlayer.setVolume(0f);

        }else{

            mMuteButton.setVisibility(View.VISIBLE);
            mUnmuteButton.setVisibility(View.GONE);

            mExoPlayer.setVolume(mMutePreviousVol);

        }

    }

    // Exoplayer events listener
    Player.EventListener mExoPlayerEventListener = new Player.EventListener() {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            if(playWhenReady){

                mPauseButton.setVisibility(View.VISIBLE);
                mPlayButton.setVisibility(View.GONE);

            }else{

                mPauseButton.setVisibility(View.GONE);
                mPlayButton.setVisibility(View.VISIBLE);

            }

            // Giving Player state response to user
            switch (playbackState){

                case ExoPlayer.STATE_READY: {
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                break;
                case ExoPlayer.STATE_BUFFERING: { }
                case ExoPlayer.STATE_ENDED: {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                case ExoPlayer.STATE_IDLE: {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                break;
            }

        }

        // On error keep on trying to ready the player on and on (This happens when internet connection unintentionally goes)
        @Override
        public void onPlayerError(ExoPlaybackException error) {

            mExoPlayer.prepare(mMediaSource, false, false);

        }
        // Not needed default methods
        @Override public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {}
        @Override public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}
        @Override public void onLoadingChanged(boolean isLoading) {}
        @Override public void onPositionDiscontinuity(int reason) {}
        @Override public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}
        @Override public void onSeekProcessed() {}
        @Override public void onRepeatModeChanged(int repeatMode) {}
        @Override public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}
    };

    private void setAllListeners(){

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExoPlayer.setPlayWhenReady(true);

            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExoPlayer.setPlayWhenReady(false);

            }
        });

        mMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setMute(true);

            }
        });

        mUnmuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setMute(false);

            }
        });

        mMainScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                if(mExoPlayer == null) return;

                Rect scrollBounds = new Rect();
                mMainScrollView.getHitRect(scrollBounds);
                if (mExoPlayerView.getLocalVisibleRect(scrollBounds)) {

                    mExoPlayer.setPlayWhenReady(true);

                } else {

                    mExoPlayer.setPlayWhenReady(false);

                }

            }
        });

    }

    // Releasing the exoplayer manually ( When home fragment gets paused or stopped )
    public void releaseExoPlayer() {

        if(mExoPlayer != null) {

            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mExoPlayerWindowIndex = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.removeListener(mExoPlayerEventListener);
            mExoPlayer.release();
            mExoPlayer = null;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releaseExoPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releaseExoPlayer();
        }
    }

}

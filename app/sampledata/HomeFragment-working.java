package com.ripple.www;


import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.HashMap;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    PlayerView mPostExoPlayerView;
    ImageView mPostDpImageView;
    ProgressBar mBufferingSpinner;

    String vidAddress;
    ExoPlayer mExoPlayer;
    MediaSource mMediaSource;
    int mExoPlayerWindowIndex = 0;
    long mPlaybackPosition = 0;

    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        mPostExoPlayerView = (PlayerView) rootview.findViewById(R.id.regular_post_playerview);
        mPostDpImageView = (ImageView) rootview.findViewById(R.id.regular_post_dp);
        mBufferingSpinner = (ProgressBar) rootview.findViewById(R.id.regular_post_buffering_spinner);

        Glide.with(this)
                .load("https://pbs.twimg.com/profile_images/953631791256137730/Mq-GwMYZ.jpg")
                .apply(RequestOptions.circleCropTransform())
                .into(mPostDpImageView);

        vidAddress = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4";

        initializeExoPlayer();
        prepareExoPlayer();

        mExoPlayer.addListener(mExoPlayerEventListener);

        return rootview;

    }

    private void initializeExoPlayer(){

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        mPostExoPlayerView.setPlayer(mExoPlayer);

        mExoPlayer.setPlayWhenReady(true);

        mExoPlayer.seekTo(mExoPlayerWindowIndex, mPlaybackPosition);

    }

    private void prepareExoPlayer(){

        Uri uri = Uri.parse(vidAddress);
        mMediaSource = buildMediaSource(uri);
        mExoPlayer.prepare(mMediaSource, true, false);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("ripple-exo")).createMediaSource(uri);
    }

    Player.EventListener mExoPlayerEventListener = new Player.EventListener() {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            switch (playbackState){

                case ExoPlayer.STATE_READY: {
                    mBufferingSpinner.setVisibility(View.GONE);
                }
                break;
                case ExoPlayer.STATE_BUFFERING: {
                    mBufferingSpinner.setVisibility(View.VISIBLE);
                }
                break;
            }

        }
        @Override
        public void onPlayerError(ExoPlaybackException error) {

            mExoPlayer.prepare(mMediaSource, false, false);

        }
        @Override public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {}
        @Override public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}
        @Override public void onLoadingChanged(boolean isLoading) {}
        @Override public void onPositionDiscontinuity(int reason) {}
        @Override public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}
        @Override public void onSeekProcessed() {}
        @Override public void onRepeatModeChanged(int repeatMode) {}
        @Override public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}
    };

    private void releaseExoPlayer() {

        mPlaybackPosition = mExoPlayer.getCurrentPosition();
        mExoPlayerWindowIndex = mExoPlayer.getCurrentWindowIndex();
        mExoPlayer.removeListener(mExoPlayerEventListener);
        mExoPlayer.release();
        mExoPlayer = null;

    }
}

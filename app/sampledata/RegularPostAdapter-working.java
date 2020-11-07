package com.ripple.www.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.ripple.www.Classes.RegularPost;
import com.ripple.www.MainActivity;
import com.ripple.www.R;

import java.util.List;

public class RegularPostAdapter extends RecyclerView.Adapter<RegularPostAdapter.MyViewHolder> {

    private List<RegularPost> postsList;
    private Context mContext;

    private MyViewHolder firstHolder, secondHolder;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public PlayerView mPostExoPlayerView;
        public ImageView mPostDpImageView, mBufferingSpinner, mHeartImageView, mCommentImageView;
        public LinearLayout mMainHolder;

        public int holderID;

        public String vidAddress;
        public ExoPlayer mExoPlayer;
        public MediaSource mMediaSource;
        public int mExoPlayerWindowIndex = 0;
        public long mPlaybackPosition = 0;

        public void setHolderID(int holderID) {
            this.holderID = holderID;
        }

        public int getHolderID() {
            return holderID;
        }

        public MyViewHolder(View view) {
            super(view);

            mPostExoPlayerView = (PlayerView) view.findViewById(R.id.regular_post_playerview);
            mPostDpImageView = (ImageView) view.findViewById(R.id.regular_post_dp);
            mBufferingSpinner = (ImageView) view.findViewById(R.id.regular_post_buffering_spinner);
            mMainHolder = (LinearLayout) view.findViewById(R.id.regular_post_main_holder);
            mHeartImageView = (ImageView) view.findViewById(R.id.regular_post_heart_icon);
            mCommentImageView = (ImageView) view.findViewById(R.id.regular_post_comment_icon);

            rotateLoadingIcon();
            //setMainHolderHeight();

        }

        Player.EventListener mExoPlayerEventListener = new Player.EventListener() {

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                switch (playbackState){

                    case ExoPlayer.STATE_READY: {
                        stopLoadingIcon();
                        mBufferingSpinner.setVisibility(View.GONE);
                        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                    break;
                    case ExoPlayer.STATE_BUFFERING: {
                        rotateLoadingIcon();
                        mBufferingSpinner.setVisibility(View.VISIBLE);
                    }
                    case ExoPlayer.STATE_ENDED: {
                        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                    case ExoPlayer.STATE_IDLE: {
                        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

        private void initializeExoPlayer(){

            if(mExoPlayer == null){

                mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(mContext),
                        new DefaultTrackSelector(),
                        new DefaultLoadControl());

                mPostExoPlayerView.setPlayer(mExoPlayer);



                mExoPlayer.seekTo(mExoPlayerWindowIndex, mPlaybackPosition);
            }

            prepareExoPlayer();

        }

        private void prepareExoPlayer(){

            Uri uri = Uri.parse(vidAddress);
            mMediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mMediaSource, false, false);

            mExoPlayer.addListener(mExoPlayerEventListener);

        }

        public void startExoPlayer(){
            mExoPlayer.setPlayWhenReady(true);
        }

        public void stopExoPlayer(){
            mExoPlayer.setPlayWhenReady(false);
        }

        private MediaSource buildMediaSource(Uri uri) {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("ripple-exo")).createMediaSource(uri);
        }

        private void setHolderDimension(){

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            final float scale = mContext.getResources().getDisplayMetrics().density;
            int pixels = (int) (8 * scale + 0.5f);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width - pixels);
            mMainHolder.setLayoutParams(params);

        }

        private void setMainHolderHeight(){

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            final float scale = mContext.getResources().getDisplayMetrics().density;
            int pixels = (int) (20 * scale + 0.5f);

            int adjustedHeight = height - getStatusBarHeight() - getNavBarHeight() - pixels;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, adjustedHeight);
            mMainHolder.setLayoutParams(params);

        }

        private int getStatusBarHeight() {
            int result = 0;
            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        private int getNavBarHeight(){

            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
            return 0;

        }

        private void rotateLoadingIcon(){

            RotateAnimation rotate = new RotateAnimation(
                    0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            rotate.setInterpolator(new AccelerateDecelerateInterpolator());
            rotate.setDuration(500);
            rotate.setRepeatCount(Animation.INFINITE);
            mBufferingSpinner.startAnimation(rotate);

        }

        private void stopLoadingIcon(){

            mBufferingSpinner.clearAnimation();

        }

        private void releaseExoPlayer() {

            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mExoPlayerWindowIndex = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.removeListener(mExoPlayerEventListener);
            mExoPlayer.release();
            mExoPlayer = null;

        }
    }

    public RegularPostAdapter(List<RegularPost> postsList, Context context) {
        this.postsList = postsList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.regular_post_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        RegularPost post = postsList.get(position);

        holder.vidAddress = post.getPostContentUrl();

        Glide.with(mContext)
                .load(post.getPostUserDpUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mPostDpImageView);

        holder.initializeExoPlayer();

        holder.setHolderID(position);

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if(firstHolder == null){
            firstHolder = holder;
            return;
        }
        if(holder.getHolderID() == firstHolder.getHolderID() + 1){
            secondHolder = holder;
        }else if(holder.getHolderID() == firstHolder.getHolderID() + 2){
            firstHolder = secondHolder;
            secondHolder = holder;
        }else if(holder.getHolderID() == firstHolder.getHolderID() - 1){
            secondHolder = firstHolder;
            firstHolder = holder;

        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.stopExoPlayer();
    }

    public void checkPercentageAndStartStopPlayer(int firstHolderPercent, int secondHolderPercent){

        if(firstHolderPercent < 50 && secondHolderPercent > 50){

            firstHolder.stopExoPlayer();
            secondHolder.startExoPlayer();

        }else if(firstHolderPercent > 50 && secondHolderPercent < 50){

            firstHolder.startExoPlayer();
            secondHolder.stopExoPlayer();

        }

    }

}
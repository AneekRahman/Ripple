package com.ripple.www.Adapters;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.ripple.www.Classes.PostButtonBounceInterpolator;
import com.ripple.www.Classes.RegularPost;
import com.ripple.www.Classes.MyUtils;
import com.ripple.www.HomeFragment;
import com.ripple.www.R;
import com.ripple.www.SubActivites.CommentActivity;
import com.ripple.www.SubActivites.FullScreenPost;
import com.ripple.www.SubActivites.VisitingProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class RegularPostAdapter extends RecyclerView.Adapter<RegularPostAdapter.MyViewHolder> {

    // MyUtils instance for formatting numbers
    private MyUtils mMyUtils = new MyUtils();

    // Regular home posts list
    private List<RegularPost> postsList;
    private Context mContext;

    // Home Fragment Class constructor for accessing the showComments method
    private HomeFragment mHomeFragment;

    // View holders to control exoplayers
    private MyViewHolder previousHolder, afterHolder, thirdHolder;

    // Main view holder class constructor
    public class MyViewHolder extends RecyclerView.ViewHolder {

        // A home posts' layout View declaration
        private PlayerView mPostExoPlayerView;
        private ImageView mPostDpImageView, mBufferingSpinner, mHeartImageView, mCommentImageView, mBroadcastImageView, mFullscreenIcon;
        private RelativeLayout mMainHolder;
        private LinearLayout mPostInteractionHolder, mUserIdentityHolder;
        private TextView mUserNameTextView, mPostDescription, mViewsCountTextView, mHeartCountTextView, mCommentCountTextView, mBroadcastCountTextView;

        // Holders ID for previous, after and third view holder assignment management
        private int holderID, mPostUserID;

        // Exoplayer declarations
        private String vidAddress;
        private ExoPlayer mExoPlayer;
        private MediaSource mMediaSource;
        private int mExoPlayerWindowIndex = 0;
        private long mPlaybackPosition = 0;

        // Setting holder id method for management
        public void setHolderID(int holderID) {
            this.holderID = holderID;
        }

        // Getting holder id when managing
        public int getHolderID() {
            return holderID;
        }

        public void setPostUserID(int mPostUserID) {
            this.mPostUserID = mPostUserID;
        }

        public int getPostUserID() {
            return mPostUserID;
        }

        private MyViewHolder(View view) {
            super(view);

            // Connecting home post layout views to code
            mMainHolder = (RelativeLayout) view.findViewById(R.id.regular_post_main_holder);
            mPostDescription = (TextView) view.findViewById(R.id.regular_post_description);
            mPostExoPlayerView = (PlayerView) view.findViewById(R.id.regular_post_playerview);

            mUserNameTextView = (TextView) view.findViewById(R.id.regular_post_user_name);
            mPostDpImageView = (ImageView) view.findViewById(R.id.regular_post_dp);
            mUserIdentityHolder = (LinearLayout) view.findViewById(R.id.regular_post_user_identity_holder);

            mHeartImageView = (ImageView) view.findViewById(R.id.regular_post_heart_icon);
            mCommentImageView = (ImageView) view.findViewById(R.id.regular_post_comment_icon);
            mBroadcastImageView = (ImageView) view.findViewById(R.id.regular_post_broadcast_icon);

            mViewsCountTextView = (TextView) view.findViewById(R.id.regular_post_views_count);
            mHeartCountTextView = (TextView) view.findViewById(R.id.regular_post_heart_count);
            mCommentCountTextView = (TextView) view.findViewById(R.id.regular_post_comment_count);
            mBroadcastCountTextView = (TextView) view.findViewById(R.id.regular_post_broadcast_count);

            mPostInteractionHolder = (LinearLayout) view.findViewById(R.id.post_interaction_button_holder);
            mBufferingSpinner = (ImageView) view.findViewById(R.id.regular_post_buffering_spinner);
            mFullscreenIcon = (ImageView) view.findViewById(R.id.regular_post_fullscreen_icon);

            // Rotating the loading icon at start
            rotateLoadingIcon();
            // Setting a fixed size to the home post
            setMainHolderHeight();
            // Setting up the posts click listeners
            setUpPostClickListeners();

        }

        // Exoplayer events listener
        Player.EventListener mExoPlayerEventListener = new Player.EventListener() {

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                // Giving Player state response to user
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

        // Initializing the posts exoplayer
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

        // Preparing the exoplayer with the video address
        private void prepareExoPlayer(){

            Uri uri = Uri.parse(vidAddress);
            mMediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mMediaSource, false, false);

            mExoPlayer.addListener(mExoPlayerEventListener);

        }

        // Starting the exoplayer manually
        public void startExoPlayer(){
            mExoPlayer.setPlayWhenReady(true);
        }

        // Stopping the exoplayer manually
        public void stopExoPlayer(){
            mExoPlayer.setPlayWhenReady(false);
        }

        // Building mediasource for exoplayer
        private MediaSource buildMediaSource(Uri uri) {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("ripple-exo")).createMediaSource(uri);
        }

        // Setting a fixed height for the root view
        private void setMainHolderHeight(){

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;

            final float scale = mContext.getResources().getDisplayMetrics().density;
            int pixels = (int) ((55 + 4) * scale + 0.5f);

            int adjustedHeight = (height + getNavBarHeight()) - (getStatusBarHeight() + pixels);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, adjustedHeight);
            mMainHolder.setLayoutParams(params);

        }

        // Needed fot the calculation of the rootviews height
        private int getStatusBarHeight() {
            int result = 0;
            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        // Needed fot the calculation of the rootviews height
        private int getNavBarHeight(){

            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
            return 0;

        }

        // Starting the rotation of loading icon manually
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

        // Stopping the rotation of loading icon manually
        private void stopLoadingIcon(){

            mBufferingSpinner.clearAnimation();

        }

        // Bounce animation for the post interaction buttons
        private void bounceAnimation(final View v){

            final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.post_button_bounce);

            PostButtonBounceInterpolator interpolator = new PostButtonBounceInterpolator(0.08, 40);
            myAnim.setInterpolator(interpolator);

            v.startAnimation(myAnim);

        }

        // Setting up method for all the click listeners
        private void setUpPostClickListeners(){

            mPostExoPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                @Override
                public void onVisibilityChange(int visibility) {

                    if(visibility == View.VISIBLE){

                        Animation fadeIn = new AlphaAnimation(0, 1);  // the 1, 0 here notifies that we want the opacity to go from opaque (1) to transparent (0)
                        fadeIn.setInterpolator(new AnticipateOvershootInterpolator());
                        fadeIn.setDuration(400);

                        mPostInteractionHolder.startAnimation(fadeIn);
                        mPostInteractionHolder.setVisibility(View.VISIBLE);

                        mPostDescription.startAnimation(fadeIn);
                        mPostDescription.setVisibility(View.VISIBLE);

                        mFullscreenIcon.startAnimation(fadeIn);
                        mFullscreenIcon.setVisibility(View.VISIBLE);


                    }else{

                        Animation fadeOut = new AlphaAnimation(1, 0);  // the 1, 0 here notifies that we want the opacity to go from opaque (1) to transparent (0)
                        fadeOut.setInterpolator(new FastOutSlowInInterpolator());
                        fadeOut.setDuration(400);

                        mPostInteractionHolder.startAnimation(fadeOut);
                        mPostInteractionHolder.setVisibility(View.GONE);

                        mPostDescription.startAnimation(fadeOut);
                        mPostDescription.setVisibility(View.GONE);

                        mFullscreenIcon.startAnimation(fadeOut);
                        mFullscreenIcon.setVisibility(View.GONE);

                    }

                }
            });

            mHeartImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHeartImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_heart_icon_red));
                    bounceAnimation(mHeartImageView);
                }
            });

            mCommentImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bounceAnimation(mCommentImageView);

                    // Start comment activity
                    mContext.startActivity(new Intent(mContext, CommentActivity.class));
                }
            });

            mBroadcastImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bounceAnimation(mBroadcastImageView);
                }
            });

            mFullscreenIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Start full screen activity
                    mContext.startActivity(new Intent(mContext, FullScreenPost.class));
                    ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }
            });

            mUserIdentityHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, VisitingProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("profileUserName", "Nibba Whuat");
                    bundle.putString("profileUserDpUrl", mContext.getString(R.string.anotherDpUrl));
                    bundle.putInt("profileUserId", getPostUserID());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                }
            });

        }

        // Releasing the exoplayer manually ( When home fragment gets paused or stopped )
        private void releaseExoPlayer() {

            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mExoPlayerWindowIndex = mExoPlayer.getCurrentWindowIndex();
            mExoPlayer.removeListener(mExoPlayerEventListener);
            mExoPlayer.release();
            mExoPlayer = null;

        }
    }

    // Main class constructor
    public RegularPostAdapter(List<RegularPost> postsList, Context context, HomeFragment homeFragment) {
        this.postsList = postsList;
        this.mContext = context;
        this.mHomeFragment = homeFragment;
    }

    // Inflating the post layout
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.regular_post_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // Getting the posts instance to get that specific posts data and contents
        RegularPost post = postsList.get(position);

        // Getting posts video url
        holder.vidAddress = post.getPostContentUrl();

        // Setting posts users id
        holder.setPostUserID(post.getPostUserId());

        // Setting up posts views
        holder.mUserNameTextView.setText(post.getPostUserName());
        holder.mViewsCountTextView.setText(String.valueOf(mMyUtils.formatNumbers(post.getPostViewsCount())));
        holder.mHeartCountTextView.setText(String.valueOf(mMyUtils.formatNumbers(post.getPostHeartCount())));
        holder.mCommentCountTextView.setText(String.valueOf(mMyUtils.formatNumbers(post.getPostCommentCount())));
        holder.mBroadcastCountTextView.setText(String.valueOf(mMyUtils.formatNumbers(post.getPostBroadcastCount())));
        holder.mPostDescription.setText( mMyUtils.getTrimmedText(mContext.getString(R.string.example_desc), 200, "... ","See More") );

        // For spannable see more text click event
        holder.mPostDescription.setMovementMethod(LinkMovementMethod.getInstance());

        // Setting up posts users dp
        Glide.with(mContext)
                .load(post.getPostUserDpUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mPostDpImageView);

        // Initializing exoplayer
        holder.initializeExoPlayer();

        // Setting a unique holder id for managing the previous, after and third viewholders instances
        holder.setHolderID(position);

    }

    // Default method
    @Override
    public int getItemCount() {
        return postsList.size();
    }

    // Method when a view gets attached to the recyclersview ( Needed for managing the exoplayers autoplay and pause )
    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if(previousHolder == null){
            previousHolder = holder;
            return;
        }
        if(holder.getHolderID() == previousHolder.getHolderID() + 1){
            afterHolder = holder;
        }else if(holder.getHolderID() == previousHolder.getHolderID() + 2){
            thirdHolder = holder;
        }else if(holder.getHolderID() == previousHolder.getHolderID() - 1){
            thirdHolder = afterHolder;
            afterHolder = previousHolder;
            previousHolder = holder;
        }

    }

    // Method when a view gets detached to the recyclersview ( Needed for managing the exoplayers autoplay and pause )
    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        holder.stopExoPlayer();
        if(holder.getHolderID() == previousHolder.getHolderID()){
            previousHolder = afterHolder;
            afterHolder = thirdHolder;
            thirdHolder = null;
        }

    }

    // Checks the visibility percentage of a post on user screen
    public void checkPercentageAndStartStopPlayer(ArrayList<Integer> arrayList){

        if(arrayList.size() == 2){

            Integer previousPer = arrayList.get(0);
            Integer afterPer = arrayList.get(1);

            if(previousPer < 50 && afterPer > 50){
                previousHolder.stopExoPlayer();
                afterHolder.startExoPlayer();
            }else if(previousPer > 50 && afterPer < 50){
                previousHolder.startExoPlayer();
                afterHolder.stopExoPlayer();
            }

        }else if(arrayList.size() == 3){

            previousHolder.stopExoPlayer();
            afterHolder.startExoPlayer();
            thirdHolder.stopExoPlayer();

        }

    }

    // Stops all playing exoplayers manually
    public void stopExoPlayerInAdapter(){

        if(previousHolder != null)
            previousHolder.stopExoPlayer();
        if(afterHolder != null)
            afterHolder.stopExoPlayer();
        if(thirdHolder != null)
            thirdHolder.stopExoPlayer();

    }

    // Initializes all viewholders' exoplayers manually (When home fragment gets paused or stopped)
    public void initExoPlayerFromFragment(){

        if(previousHolder != null)
            previousHolder.initializeExoPlayer();
        if(afterHolder != null)
            afterHolder.initializeExoPlayer();
        if(thirdHolder != null)
            thirdHolder.initializeExoPlayer();
    }

    // Releases all viewholders' exoplayers manually (When home fragment gets resumed or started)
    public void releaseExoPlayerFromFragment(){

        if(previousHolder != null)
            previousHolder.releaseExoPlayer();
        if(afterHolder != null)
            afterHolder.releaseExoPlayer();
        if(thirdHolder != null)
            thirdHolder.releaseExoPlayer();


    }

}
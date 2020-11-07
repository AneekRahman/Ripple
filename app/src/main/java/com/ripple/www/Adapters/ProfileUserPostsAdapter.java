package com.ripple.www.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ripple.www.Classes.ProfileUserGridPost;
import com.ripple.www.R;

import java.util.List;

public class ProfileUserPostsAdapter extends BaseAdapter{

    // Given user profile posts
    private Context mContext;
    private List<ProfileUserGridPost> mPosts;

    // Class instance constructor
    public ProfileUserPostsAdapter(Context c, List<ProfileUserGridPost> posts) {
        mContext = c;
        this.mPosts = posts;
    }

    // Needed method
    @Override
    public int getCount() {
        return mPosts.size();
    }
    // Needed method
    @Override
    public Object getItem(int position) {
        return mPosts.get(position);
    }
    // Needed method
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inflating the profile grid layout for each of the girds
        convertView = LayoutInflater.from(mContext).inflate(R.layout.profile_user_post_layout, null);

        // Connecting the grid layouts view to code
        ImageView imageView = (ImageView) convertView.findViewById(R.id.profile_user_post_preview_imageview);

        // Setting a fixed size to the gird
        setPostImageViewSize(imageView);

        // Showing the user post gif
        Glide.with(mContext)
                .load("https://media.giphy.com/media/5UCieYmJFW2rzcHkab/giphy.gif")
                .apply(RequestOptions.centerCropTransform())
                .into(imageView);

        return convertView;

    }

    // The method for setting a fixed size to the gird
    private void setPostImageViewSize(ImageView v){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;

        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixelsFrom24Dp = (int) (24 * scale + 0.5f);
        int pixelsFrom40Dp = (int) (40 * scale + 0.5f);

        int adjustedWidthPerIV = ( displayWidth - pixelsFrom24Dp ) / 3;
        int adjustedHeightPerIV = adjustedWidthPerIV  + pixelsFrom40Dp;

        v.getLayoutParams().height = adjustedHeightPerIV;
        v.getLayoutParams().width = adjustedWidthPerIV;
        v.requestLayout();

    }


}
package com.ripple.www.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ripple.www.Classes.GlobalPost;
import com.ripple.www.R;

import java.util.List;

public class PostGridLayoutAdapter extends RecyclerView.Adapter<PostGridLayoutAdapter.ViewHolder> {

    private List<GlobalPost> mData;
    private Context mContext;
    private int mColumnNum = 1;

    // data is passed into the constructor
    public PostGridLayoutAdapter(Context context, List<GlobalPost> data, int columnNum) {
        this.mContext = context;
        this.mData = data;
        this.mColumnNum = columnNum;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_grid_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO make this functional
        GlobalPost post = mData.get(position);

        setPostMainHolderSize(holder.mRootHolder, position);

        Glide.with(mContext)
                .load(mContext.getString(R.string.gif_url))
                .apply(RequestOptions.centerCropTransform())
                .into(holder.mPostImageView);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mRootHolder;
        ImageView mPostImageView;

        ViewHolder(View itemView) {
            super(itemView);

            mRootHolder = (RelativeLayout) itemView.findViewById(R.id.global_root_holder);
            mPostImageView = (ImageView) itemView.findViewById(R.id.post_grid_layout_imageview);

        }

    }

    // Grid size setter method
    private void setPostMainHolderSize(RelativeLayout v, int pos){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;

        final float scale = mContext.getResources().getDisplayMetrics().density;
        int paddingPixels = (int) (16 * scale + 0.5f);

        double gridColumnWidth = (displayWidth - paddingPixels) / 3;

        int gridColumnHeight = (int) (gridColumnWidth * 1.75);

        if(mColumnNum == 1 && pos == 0){

            gridColumnHeight = (int) (((double) gridColumnWidth) * 1.30);

        }else if(mColumnNum == 2 && pos == 0){

            gridColumnHeight = (int) (((double) gridColumnWidth) * 1.60);

        }else if(mColumnNum == 3 && pos == 0){

            gridColumnHeight = (int) (((double) gridColumnWidth) * 1.90);

        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) gridColumnWidth, gridColumnHeight);
        v.setLayoutParams(params);

    }

    // convenience method for getting data at click position
    GlobalPost getItem(int id) {
        return mData.get(id);
    }

}
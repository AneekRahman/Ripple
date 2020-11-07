package com.ripple.www.Adapters;

import android.app.Activity;
import android.content.Context;
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
import com.ripple.www.Classes.CommentRow;
import com.ripple.www.Classes.GlobalPost;
import com.ripple.www.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<CommentRow> mData;
    private Context mContext;

    // data is passed into the constructor
    public CommentAdapter(Context context, List<CommentRow> data) {
        this.mContext = context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_row_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CommentRow post = mData.get(position);

        holder.mCommentUserName.setText(post.getCommentUserName());
        holder.mCommentComment.setText(post.getCommentMainText());
        holder.mCommentTimeStamp.setText(post.getCommentTimeStamp());

        if(post.getCommentReplied()){

            holder.mCommentRepliedToUserName.setText(post.getCommentRepliedToUserName());
            holder.mCommentRepliedToUserName.setVisibility(View.VISIBLE);

        }

        Glide.with(mContext)
                .load(post.getCommentUserDpUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mCommentUserDp);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mCommentUserDp;
        TextView mCommentUserName, mCommentComment, mCommentTimeStamp, mCommentRepliedToUserName;

        ViewHolder(View itemView) {
            super(itemView);

            mCommentUserName = (TextView) itemView.findViewById(R.id.comment_row_user_name);
            mCommentUserDp = (ImageView) itemView.findViewById(R.id.comment_row_dp);
            mCommentTimeStamp = (TextView) itemView.findViewById(R.id.time_stamp);
            mCommentRepliedToUserName = (TextView) itemView.findViewById(R.id.comment_row_replied_to_user_name);
            mCommentComment = (TextView) itemView.findViewById(R.id.comment_row_main_comment_textview);

        }

    }

    // convenience method for getting data at click position
    public CommentRow getItem(int id) {
        return mData.get(id);
    }

}
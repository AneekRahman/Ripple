package com.ripple.www.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ripple.www.Classes.MessageListRow;
import com.ripple.www.R;

import java.util.List;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class MessageListAdapter extends RecyclerView.Adapter {

    private List<MessageListRow> mMessageLists;
    Context mContext;

    public MessageListAdapter(List<MessageListRow> messageListRows, Context context) {

        this.mMessageLists = messageListRows;
        this.mContext = context;
    }

    public static class TypeSeenViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mMainLinearLayout;
        TextView msgUserName, msgMessage, msgSentTime;
        ImageView msgUserDpImageView;


        public TypeSeenViewHolder(View itemView) {
            super(itemView);

            this.mMainLinearLayout = (LinearLayout) itemView.findViewById(R.id.message_row_main_linear_layout);
            this.msgUserName = (TextView) itemView.findViewById(R.id.message_row_user_name);
            this.msgMessage = (TextView) itemView.findViewById(R.id.message_row_user_msg);
            this.msgSentTime = (TextView) itemView.findViewById(R.id.message_row_sent_time);
            this.msgUserDpImageView = (ImageView) itemView.findViewById(R.id.message_row_user_dp);
        }
    }

    public static class TypeNotSeenViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mMainLinearLayout;
        TextView msgUserName, msgMessage, msgSentTime;
        ImageView msgUserDpImageView;

        public TypeNotSeenViewHolder(View itemView) {
            super(itemView);

            this.mMainLinearLayout = (LinearLayout) itemView.findViewById(R.id.message_row_main_linear_layout);
            this.msgUserName = (TextView) itemView.findViewById(R.id.message_row_user_name);
            this.msgMessage = (TextView) itemView.findViewById(R.id.message_row_user_msg);
            this.msgSentTime = (TextView) itemView.findViewById(R.id.message_row_sent_time);
            this.msgUserDpImageView = (ImageView) itemView.findViewById(R.id.message_row_user_dp);
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (mMessageLists.get(position).getMsgRowType()) {
            case MessageListRow.TYPE_MSG_SEEN:
                return MessageListRow.TYPE_MSG_SEEN;
            case MessageListRow.TYPE_MSG_NOT_SEEN:
                return MessageListRow.TYPE_MSG_NOT_SEEN;
            default:
                return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case MessageListRow.TYPE_MSG_SEEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_layout_sent_not_seen, parent, false);
                return new TypeSeenViewHolder(view);
            case MessageListRow.TYPE_MSG_NOT_SEEN:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_layout_recieved_not_seen, parent, false);
                return new TypeNotSeenViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        MessageListRow messageRow = mMessageLists.get(listPosition);
        if (messageRow != null) {

            switch (messageRow.getMsgRowType()) {

                case MessageListRow.TYPE_MSG_SEEN:

                    ((TypeSeenViewHolder) holder).msgUserName.setText(messageRow.getMsgUserName());
                    ((TypeSeenViewHolder) holder).msgMessage.setText(messageRow.getMsgMessage());
                    ((TypeSeenViewHolder) holder).msgSentTime.setText(messageRow.getMsgSentTime());

                    Glide.with(mContext)
                            .load(messageRow.getMsgUserDpUrl())
                            .apply(RequestOptions.centerCropTransform())
                            .into(((TypeSeenViewHolder) holder).msgUserDpImageView);

                    break;

                case MessageListRow.TYPE_MSG_NOT_SEEN:

                    ((TypeNotSeenViewHolder) holder).msgUserName.setText(messageRow.getMsgUserName());
                    ((TypeNotSeenViewHolder) holder).msgMessage.setText(messageRow.getMsgMessage());
                    ((TypeNotSeenViewHolder) holder).msgSentTime.setText(messageRow.getMsgSentTime());

                    Glide.with(mContext)
                            .load(messageRow.getMsgUserDpUrl())
                            .apply(RequestOptions.centerCropTransform())
                            .into(((TypeNotSeenViewHolder) holder).msgUserDpImageView);

                    break;

            }
        }
    }

    @Override
    public int getItemCount() {
        return mMessageLists.size();
    }
}
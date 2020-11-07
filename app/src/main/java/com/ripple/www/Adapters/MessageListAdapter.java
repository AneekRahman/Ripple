package com.ripple.www.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.ripple.www.SubActivites.MessagingActivity;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {

    private List<MessageListRow> mMessageLists;
    Context mContext;

    public MessageListAdapter(List<MessageListRow> messageListRows, Context context) {

        this.mMessageLists = messageListRows;
        this.mContext = context;
    }

    public static class MessageRowViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mMainLinearLayout;
        TextView msgUserName, msgMessage, msgSentTime, msgYouTextView;
        ImageView msgUserDpImageView;

        public MessageRowViewHolder(View itemView) {
            super(itemView);

            this.mMainLinearLayout = (LinearLayout) itemView.findViewById(R.id.message_row_main_linear_layout);
            this.msgUserName = (TextView) itemView.findViewById(R.id.message_row_user_name);
            this.msgMessage = (TextView) itemView.findViewById(R.id.message_row_user_msg);
            this.msgSentTime = (TextView) itemView.findViewById(R.id.message_row_sent_time);
            this.msgUserDpImageView = (ImageView) itemView.findViewById(R.id.message_row_user_dp);
            this.msgYouTextView = (TextView) itemView.findViewById(R.id.message_sent_you_textview);
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (mMessageLists.get(position).getMsgRowType()) {
            case MessageListRow.TYPE_MSG_RECEIVED_NOT_SEEN:
                return MessageListRow.TYPE_MSG_RECEIVED_NOT_SEEN;
            case MessageListRow.TYPE_MSG_RECEIVED_SEEN:
                return MessageListRow.TYPE_MSG_RECEIVED_SEEN;
            case MessageListRow.TYPE_MSG_SENT_NOT_SEEN:
                return MessageListRow.TYPE_MSG_SENT_NOT_SEEN;
            case MessageListRow.TYPE_MSG_SENT_SEEN:
                return MessageListRow.TYPE_MSG_SENT_SEEN;
            default:
                return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_layout, parent, false);

        return new MessageRowViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        MessageListRow messageRow = mMessageLists.get(listPosition);

        MessageRowViewHolder messageViewHolder = (MessageRowViewHolder) holder;

        messageViewHolder.mMainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.startActivity(new Intent(mContext, MessagingActivity.class));

            }
        });

        if (messageRow != null) {

            switch (messageRow.getMsgRowType()) {

                case MessageListRow.TYPE_MSG_RECEIVED_NOT_SEEN:

                    messageReceivedNotSeen(messageViewHolder, messageRow);

                    break;

                case MessageListRow.TYPE_MSG_RECEIVED_SEEN:

                    messageReceivedSeen(messageViewHolder, messageRow);

                    break;

                case MessageListRow.TYPE_MSG_SENT_NOT_SEEN:

                    messageSentNotSeen(messageViewHolder, messageRow);

                    break;

                case MessageListRow.TYPE_MSG_SENT_SEEN:

                    messageSentSeen(messageViewHolder, messageRow);

                    break;

            }
        }
    }

    @Override
    public int getItemCount() {
        return mMessageLists.size();
    }

    private void messageReceivedNotSeen(MessageRowViewHolder viewHolder, MessageListRow messageRow){

        viewHolder.msgYouTextView.setVisibility(View.GONE);

        viewHolder.msgUserName.setText(messageRow.getMsgUserName());
        viewHolder.msgMessage.setText(messageRow.getMsgMessage());
        viewHolder.msgSentTime.setText(messageRow.getMsgSentTime());

        int textColor = Color.parseColor("#000000");
        viewHolder.msgUserName.setTextColor(textColor);
        viewHolder.msgMessage.setTextColor(textColor);
        viewHolder.msgSentTime.setTextColor(textColor);

        viewHolder.msgMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        Glide.with(mContext)
                .load(messageRow.getMsgUserDpUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(viewHolder.msgUserDpImageView);

    }

    private void messageReceivedSeen(MessageRowViewHolder viewHolder, MessageListRow messageRow){

        viewHolder.msgYouTextView.setVisibility(View.GONE);

        viewHolder.msgUserName.setText(messageRow.getMsgUserName());
        viewHolder.msgMessage.setText(messageRow.getMsgMessage());
        viewHolder.msgSentTime.setText(messageRow.getMsgSentTime());

        int textColor = Color.parseColor("#842d151d");
        viewHolder.msgUserName.setTextColor(textColor);
        viewHolder.msgMessage.setTextColor(textColor);
        viewHolder.msgSentTime.setTextColor(textColor);

        Glide.with(mContext)
                .load(messageRow.getMsgUserDpUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(viewHolder.msgUserDpImageView);

    }

    private void messageSentNotSeen(MessageRowViewHolder viewHolder, MessageListRow messageRow){

        viewHolder.msgYouTextView.setVisibility(View.VISIBLE);

        viewHolder.msgUserName.setText(messageRow.getMsgUserName());
        viewHolder.msgMessage.setText(messageRow.getMsgMessage());
        viewHolder.msgSentTime.setText(messageRow.getMsgSentTime());

        int textColor = Color.parseColor("#842d151d");
        viewHolder.msgUserName.setTextColor(textColor);
        viewHolder.msgMessage.setTextColor(textColor);
        viewHolder.msgSentTime.setTextColor(textColor);

        Glide.with(mContext)
                .load(messageRow.getMsgUserDpUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(viewHolder.msgUserDpImageView);

    }

    private void messageSentSeen(MessageRowViewHolder viewHolder, MessageListRow messageRow){

        viewHolder.msgYouTextView.setVisibility(View.VISIBLE);

        viewHolder.msgUserName.setText(messageRow.getMsgUserName());
        viewHolder.msgMessage.setText(messageRow.getMsgMessage());
        viewHolder.msgSentTime.setText(messageRow.getMsgSentTime());

        int textColor = Color.parseColor("#842d151d");
        viewHolder.msgUserName.setTextColor(textColor);
        viewHolder.msgMessage.setTextColor(textColor);
        viewHolder.msgSentTime.setTextColor(textColor);

        Glide.with(mContext)
                .load(messageRow.getMsgUserDpUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(viewHolder.msgUserDpImageView);


    }


}
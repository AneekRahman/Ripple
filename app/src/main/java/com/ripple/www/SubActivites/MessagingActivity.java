package com.ripple.www.SubActivites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ripple.www.Classes.MyUtils;
import com.ripple.www.R;

public class MessagingActivity extends AppCompatActivity {

    // View declaration
    private ImageView mMsgUserDpImageView, mBackIcon;
    private TextView mMsgUserName, mMsgUserActiveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        // Dark status bar text
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // Connecting view to code
        mMsgUserDpImageView = (ImageView) findViewById(R.id.messaging_msg_user_dp);
        mBackIcon = (ImageView) findViewById(R.id.messaging_back_icon);
        mMsgUserName = (TextView) findViewById(R.id.messaging_msg_user_name);
        mMsgUserActiveState = (TextView) findViewById(R.id.messaging_msg_user_active_state);

        // Setting up message user
        Glide.with(this)
                .load(getString(R.string.aneek_dp_url))
                .into(mMsgUserDpImageView);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

}

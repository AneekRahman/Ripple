package com.ripple.www.SubActivites;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ripple.www.Adapters.ProfileUserPostsAdapter;
import com.ripple.www.Classes.ExpandableHeightGridView;
import com.ripple.www.Classes.MyUtils;
import com.ripple.www.Classes.ProfileUserGridPost;
import com.ripple.www.R;

import java.util.ArrayList;
import java.util.List;

public class VisitingProfileActivity extends AppCompatActivity {

    // View declaration
    private LinearLayout mMainHolder;
    private ImageView mUserDpImageView, mBackBtn;
    private ExpandableHeightGridView mGridView;
    private TextView mUserName;

    // Profile grid posts list and adapter
    private ProfileUserPostsAdapter mAdapter;
    private List<ProfileUserGridPost> mPosts = new ArrayList<>();

    // Class declarations
    private MyUtils mMyUtils = new MyUtils();

    // Profile user data
    private String profileUserName, profileUserDpUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visiting_profile);

        // Dark status bar text
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Get data passed from the calling activity
        Bundle bundle = getIntent().getExtras();
        try {
            profileUserName = bundle.getString("profileUserName");
            profileUserDpUrl = bundle.getString("profileUserDpUrl");
        }catch (NullPointerException e){
            throw e;
        }

        // Connecting view to code
        mMainHolder = (LinearLayout) findViewById(R.id.main_holder);
        mUserDpImageView = (ImageView) findViewById(R.id.profile_user_dp_imageview);
        mGridView = (ExpandableHeightGridView) findViewById(R.id.profile_posts_gridview);
        mUserName = (TextView) findViewById(R.id.profile_user_name);
        mBackBtn = (ImageView) findViewById(R.id.back_btn);

        // Space on top to avoid the glitch for having a transparent navbar
        mMainHolder.setPadding(0, mMyUtils.getStatusBarHeight(this), 0, 0);

        // Load the users profile
        mUserName.setText(profileUserName);
        Glide.with(this)
                .load(profileUserDpUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(mUserDpImageView);

        // Setting up the Gridview and its adapter
        mAdapter = new ProfileUserPostsAdapter(this, mPosts);
        mGridView.setAdapter(mAdapter);
        mGridView.setExpanded(true);
        mGridView.setFocusable(false);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // TODO remove test posts
        insertPostsToAdapter();

    }

    private void insertPostsToAdapter(){

        ProfileUserGridPost post = new ProfileUserGridPost(0, "");
        mPosts.add(post);

        for (int i = 0; i < 20; i++){

            post = new ProfileUserGridPost(0, "");
            mPosts.add(post);

        }

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

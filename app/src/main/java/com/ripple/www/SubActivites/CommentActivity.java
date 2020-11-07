package com.ripple.www.SubActivites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ripple.www.Adapters.CommentAdapter;
import com.ripple.www.Adapters.RegularPostAdapter;
import com.ripple.www.Classes.CommentRow;
import com.ripple.www.Classes.MyUtils;
import com.ripple.www.Classes.RegularPostDivider;
import com.ripple.www.R;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    // View declarations
    private RecyclerView mRecyclerView;
    private ImageView mDescUserDp, mBackBtn;

    // Class declarations
    private MyUtils mMyUtils = new MyUtils();
    private LinearLayoutManager mLayoutManager;

    // Recyclerview declarations
    private CommentAdapter mAdapter;
    private List<CommentRow> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Dark status bar text
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // Connecting view with code
        mRecyclerView = (RecyclerView) findViewById(R.id.comment_activity_recycler);
        mDescUserDp = (ImageView) findViewById(R.id.comment_activity_user_dp);
        mBackBtn = (ImageView) findViewById(R.id.back_btn);

        // Setting up post description user
        Glide.with(this)
                .load(getString(R.string.aneek_dp_url))
                .apply(RequestOptions.circleCropTransform())
                .into(mDescUserDp);

        // Setting up Recyclerview
        mAdapter = new CommentAdapter(this, mList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // TODO remove test comments
        testComments();

    }

    private void testComments(){

        for(int i = 0; i < 10; i++){

            CommentRow row = new CommentRow(0, "CommentWriterAneek", getString(R.string.aneek_dp_url), getString(R.string.example_desc), "20:20PM", false, 0, "ReplyingToYAboi");
            mList.add(row);

        }

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

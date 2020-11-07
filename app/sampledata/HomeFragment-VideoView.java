package com.ripple.www;


import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import java.util.HashMap;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    VideoView mPostVideoView;
    ImageView mPostDpImageView, mPostThumbImageView;
    RelativeLayout mThumbScreen;
    String vidAddress;
    ProgressBar mBufferingSpinner;

    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        mPostVideoView = (VideoView) rootview.findViewById(R.id.regular_post_videoview);
        mPostDpImageView = (ImageView) rootview.findViewById(R.id.regular_post_dp);
        mPostThumbImageView = (ImageView) rootview.findViewById(R.id.regular_post_thumb);
        mThumbScreen = (RelativeLayout) rootview.findViewById(R.id.regular_post_loading_screen);
        mBufferingSpinner = (ProgressBar) rootview.findViewById(R.id.regular_post_buffering_spinner);


        vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";

        new AsyncGettingBitmapFromUrl().execute("");

        Uri vidUri = Uri.parse(vidAddress);
        mPostVideoView.setVideoURI(vidUri);

        mPostVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: {
                        mBufferingSpinner.setVisibility(View.GONE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                        mBufferingSpinner.setVisibility(View.VISIBLE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                        mBufferingSpinner.setVisibility(View.GONE);
                        return true;
                    }
                    case MediaPlayer.MEDIA_INFO_VIDEO_NOT_PLAYING:{
                        mBufferingSpinner.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });

        mPostVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mThumbScreen.setVisibility(View.GONE);

            }
        });

        mPostVideoView.start();

        Glide.with(this)
                .load("data:image/jpeg;base64,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")
                .apply(RequestOptions.circleCropTransform())
                .into(mPostDpImageView);

        return rootview;

    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    private class AsyncGettingBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {

            System.out.println("doInBackground");

            Bitmap bitmap = retriveVideoFrameFromVideo(vidAddress);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            MultiTransformation multi = new MultiTransformation(new BlurTransformation(20), new CenterCrop());
            Glide.with(getContext())
                    .load(bitmap)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(20)))
                    .into(mPostThumbImageView);

        }
    }

}

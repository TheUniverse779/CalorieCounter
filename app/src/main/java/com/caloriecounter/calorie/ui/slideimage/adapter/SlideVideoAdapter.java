package com.caloriecounter.calorie.ui.slideimage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.ui.main.model.image.Image;

import java.util.ArrayList;

public class SlideVideoAdapter extends PagerAdapter {
    private ArrayList<Image> images = new ArrayList<>();

    private Context mContext;
    private LoadBitmap loadBitmap;
    private ClickImage clickImage;



    private int centerPosition = 0;

    public ArrayList<ViewHolder> viewHolders = new ArrayList<>();

    public void setCenterPosition(int centerPosition) {
//        try {
//            this.centerPosition = centerPosition;
//            viewHolders.get(centerPosition).setPlay();
//        } catch (Exception e) {
//
//        }
        for (int i = 0; i < viewHolders.size(); i++) {
            if(i != centerPosition){
                viewHolders.get(i).simpleExoplayer.release();
            }
        }
    }

    public SlideVideoAdapter(Context context, ArrayList<Image> images, LoadBitmap loadBitmap, ClickImage clickImage) {
        mContext = context;
        this.images = images;
        this.loadBitmap = loadBitmap;
        this.clickImage = clickImage;

    }



    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_video_slide, collection, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.bind(layout, position);
        if(position == viewHolders.size()){
            viewHolders.add(viewHolder);
        }else {
            viewHolders.get(position).simpleExoplayer.release();
            viewHolders.set(position, viewHolder);
        }
//        ImageView imageView = layout.findViewById(R.id.img);
//        ImageView imageViewCache = layout.findViewById(R.id.imgCache);
//        PlayerView exoPlayer = layout.findViewById(R.id.playerView);
//
//        Glide.with(mContext)
//                .load(images.get(position).getImageVariations().getPreview_small().getUrl())
//                .into(imageViewCache);
//
//
//        if(position == centerPosition){
//            exoPlayer.setPlayer(simpleExoplayer);
//            exoPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
//        }
//
//
//        layout.post(new Runnable() {
//            @Override
//            public void run() {
//                if(position == centerPosition + 1 || position == centerPosition  -1){
//                    layout.setScaleX(0.8f);
//                    layout.setScaleY(0.8f);
//                }
//            }
//        });






//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickImage.onClick();
//            }
//        });
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        try {
            return images.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public interface LoadBitmap{
        void onLoadBitmap(Bitmap bitmap);
    }
    public interface ClickImage{
        void onClick();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public class ViewHolder{
        ImageView imageView;
        ImageView imageViewCache;
        PlayerView exoPlayer;

        public SimpleExoPlayer simpleExoplayer;
        private DataSource.Factory dataSourceFactory;
        public void bind(ViewGroup layout, int position){
            imageView = layout.findViewById(R.id.img);
            imageViewCache = layout.findViewById(R.id.imgCache);
            exoPlayer = layout.findViewById(R.id.playerView);


            Glide.with(mContext)
                    .load(images.get(position).getImageVariations().getPreview_small().getUrl())
                    .into(imageViewCache);

            dataSourceFactory = new DefaultDataSourceFactory(mContext, "exoplayer-sample");
            initializePlayer();

            setPlay();

        }

        private void initializePlayer() {
            simpleExoplayer = new SimpleExoPlayer.Builder(mContext).build();
            preparePlayer("default");

            simpleExoplayer.setPlayWhenReady(false);
            simpleExoplayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            simpleExoplayer.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Player.EventListener.super.onPlayerStateChanged(playWhenReady, playbackState);
                }
            });

//        binding?.playerView?.player = simpleExoplayer
//        binding?.playerView?.useController = false
//        binding?.playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }

        private MediaSource buildMediaSource(Uri uri, String type) {
            return new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
        }

        public void preparePlayer(String type) {
            Uri uri  = RawResourceDataSource.buildRawResourceUri(R.raw.splash);
            MediaSource mediaSource = buildMediaSource(uri, type);
            simpleExoplayer.prepare(mediaSource);
        }

        public void setPlay(){
            exoPlayer.setPlayer(simpleExoplayer);
            exoPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        }

    }

}

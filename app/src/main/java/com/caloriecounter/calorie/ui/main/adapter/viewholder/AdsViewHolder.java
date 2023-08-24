package com.caloriecounter.calorie.ui.main.adapter.viewholder;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAdRevenue;
import com.adjust.sdk.AdjustConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.caloriecounter.calorie.BuildConfig;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.ads.Prefs;
import com.caloriecounter.calorie.ui.main.model.image.Image;

import java.util.List;
import java.util.Locale;

public class AdsViewHolder extends RecyclerView.ViewHolder {
    private static final String ADMOB_AD_UNIT_ID_TEST = "ca-app-pub-3940256099942544/2247696110";

    private static final String ADMOB_AD_UNIT_ID_DEFAULT = "ca-app-pub-6989606115612474/6526614083";
    private static final String TAG = "MainActivity";

    private Button refresh;
    private CheckBox startVideoAdsMuted;
    private TextView videoStatus;
    private NativeAd nativeAd;

    private View layoutRootView;
    private View layoutProgressbar;
    private View fl_adplaceholder;
    private Context context;

    private List<Image> images;
    private int position;

    public AdsViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        refresh = itemView.findViewById(R.id.btn_refresh);
        layoutRootView = itemView.findViewById(R.id.viewHeight);
        fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
        layoutProgressbar = itemView.findViewById(R.id.layoutProgressbar);
        startVideoAdsMuted = itemView.findViewById(R.id.cb_start_muted);
        videoStatus = itemView.findViewById(R.id.tv_video_status);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View unusedView) {
                refreshAd(0);
            }
        });

//        refreshAd();

    }

    public void bind(int res, List<Image> images, int position) {
        this.images = images;
        this.position = position;
        int isPro = new Prefs(WeatherApplication.get()).getPremium();
        if(isPro != 1) {
            refreshAd(res);
            layoutRootView.setVisibility(View.VISIBLE);
            layoutProgressbar.setVisibility(View.VISIBLE);
            fl_adplaceholder.setVisibility(View.VISIBLE);
        }else {
            layoutRootView.setVisibility(View.GONE);
            layoutProgressbar.setVisibility(View.GONE);
            fl_adplaceholder.setVisibility(View.GONE);
        }

    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.GONE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.GONE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (nativeAd.getMediaContent() != null && nativeAd.getMediaContent().hasVideoContent()) {

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    refresh.setEnabled(true);
                    videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
            videoStatus.setText("Video status: Ad does not contain a video asset.");
            refresh.setEnabled(true);
        }
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */
    private void refreshAd(int res) {
        refresh.setEnabled(false);
        AdLoader.Builder builder = new AdLoader.Builder(context, BuildConfig.DEBUG ? ADMOB_AD_UNIT_ID_TEST : ADMOB_AD_UNIT_ID_DEFAULT);

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        refresh.setEnabled(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = ((Activity) context).isDestroyed();
                        }
                        if (isDestroyed || ((Activity) context).isFinishing() || ((Activity) context).isChangingConfigurations()) {
                            nativeAd.destroy();
                            return;
                        }

                        if(nativeAd != null){
                            nativeAd.setOnPaidEventListener(adValue -> {
                                try {
                                    WeatherApplication.initROAS(adValue.getValueMicros(),adValue.getCurrencyCode());
                                    AdjustAdRevenue adRevenue = new AdjustAdRevenue(AdjustConfig.AD_REVENUE_ADMOB);
                                    adRevenue.setRevenue((double) (adValue.getValueMicros() / 1000000f), adValue.getCurrencyCode());
                                    Adjust.trackAdRevenue(adRevenue);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }

                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (AdsViewHolder.this.nativeAd != null) {
                            AdsViewHolder.this.nativeAd.destroy();
                        }
                        AdsViewHolder.this.nativeAd = nativeAd;
                        FrameLayout frameLayout = itemView.findViewById(R.id.fl_adplaceholder);
                        NativeAdView adView =
                                (NativeAdView) ((Activity) context).getLayoutInflater().inflate(R.layout.ad_unified, frameLayout, false);
                        populateNativeAdView(nativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                        try {
                            Image image = images.get(position);
                            image.setNativeAd(nativeAd);
                            images.set(position, image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        VideoOptions videoOptions =
                new VideoOptions.Builder().setStartMuted(startVideoAdsMuted.isChecked()).build();

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        refresh.setEnabled(true);
                                        String error =
                                                String.format(
                                                        Locale.getDefault(),
                                                        "domain: %s, code: %d, message: %s",
                                                        loadAdError.getDomain(),
                                                        loadAdError.getCode(),
                                                        loadAdError.getMessage());
//                                        Toast.makeText(
//                                                        MainActivity.this,
//                                                        "Failed to load native ad with error " + error,
//                                                        Toast.LENGTH_SHORT)
//                                                .show();
                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());

        videoStatus.setText("");

    }
}

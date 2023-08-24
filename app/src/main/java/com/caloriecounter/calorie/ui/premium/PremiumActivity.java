package com.caloriecounter.calorie.ui.premium;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.google.common.collect.*;


import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.caloriecounter.calorie.R;
import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.ads.Prefs;
import com.caloriecounter.calorie.ads.iap.IapConnector;
import com.caloriecounter.calorie.databinding.ActivityPremiumBinding;
import com.caloriecounter.calorie.ui.main.view.MainActivity;
import com.caloriecounter.calorie.ui.onboarding.view.OnBoardingFragment;
import com.caloriecounter.calorie.ui.splash.view.SplashActivity;


import java.util.ArrayList;
import java.util.List;

import games.moisoni.google_iab.BillingConnector;

public class PremiumActivity extends FragmentActivity {
    private BillingConnector billingConnector;
    private final static String KEY_DEV = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmdHETvtJgQ716AV/Whz2+2+uCGy5R+mI88W5LNtVaGJkd40liiorMvcLvP7e2GGWAEDPoe8HdxNcU7ySNwa64KmIMkzSt1D+as4c71HF0ndc6/dnE158V2KAuUrrNlFZTg/wbg1v5MOp+cd10snb0HE4OV4YPv+j6Hp6wL/i0rvyIvZq4IopuezuKDOVQbNH2Tn1pUTH0vB/d4tfNbgU+q1fpSlC0biHewMNzsWlXY4+DEurx1XYXuuSokzTRK+s12bpw/1Q6ezcPghk3vJW9JWFX1X0JWTfOK4s7zfi3ERs6Z3k7xEbKNMnwQ1zXPGQ73qn502Q0Q/BGPXD8PqUyQIDAQAB";
    private Prefs prefs;
    private ImageView imgBack;
    public static String LIFETIME = "pro_lifetime";
    public static String MONTHLY = "1_week";
    public static String MONTHLY_6 = "1_month";
    public static String YEAR = "1_year";
    private IapConnector iapConnector;
    private BillingClient billingClient;
    List<ProductDetails> productDetailsList;
    private boolean isMonthly = true;
    boolean isTrial;

    private ActivityPremiumBinding binding;

    public void setStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
    public void FullScreencall() {
        View decorView2 = getWindow().getDecorView();
        decorView2.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }
        decorView2.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, new PremiumVideoFragment()).commit();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_premium);
        if(getIntent().hasExtra("free_trial")){
            isTrial = true;
        }else isTrial = false;
        FullScreencall();
        setStatusBar(getResources().getColor(R.color.color_excel_dark));

        prefs = new Prefs(this);
        WeatherApplication.trackingEvent("SHOW_PREMIUM");
        //initializeBillingClient();
        initView();
        Log.e("xxx2", "onCreate: " );

        initIAP();
    }

    private void initIAP() {
        try {
            productDetailsList = new ArrayList<>();
            billingClient = BillingClient.newBuilder(this)
                    .enablePendingPurchases()
                    .setListener(
                            (billingResult, list) -> {
                                if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK && list !=null) {
                                    for (Purchase purchase: list){
                                        verifySubPurchase(purchase);
                                    }
                                }
                            }
                    ).build();

            //start the connection after initializing the billing client
            establishConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void establishConnection() {
        try {
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        // The BillingClient is ready. You can query purchases here.
                        showAllSub();
                        showAllInApp();
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                    establishConnection();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    void showAllSub() {

        try {
            ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                    //Product 1
                    QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(MONTHLY)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build(),

                    QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(MONTHLY_6)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    ,
                    QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(YEAR)
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()

            );

            QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                    .setProductList(productList)
                    .build();

            billingClient.queryProductDetailsAsync(
                    params,
                    (billingResult, prodDetailsList) -> {
                        // Process the result
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                            try {
                                productDetailsList.addAll(prodDetailsList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        for ( ProductDetails entry : prodDetailsList) {
                                            Log.e("xxx", "onPricesUpdated: " + entry);
                                            String sku = entry.getProductId();
                                            String price = entry.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice();
                                            try {
                                                if (sku.equalsIgnoreCase(MONTHLY)) {
                                                    binding.tvPrice1.setText(price);
                                                    binding.pg1.setVisibility(View.GONE);
                                                }else if(sku.equalsIgnoreCase(MONTHLY_6)){
                                                    binding.tvPrice2.setText(price);
                                                    binding.pg2.setVisibility(View.GONE);
                                                }else if(sku.equalsIgnoreCase(YEAR)){
                                                    binding.tvPrice3.setText(price);
                                                }
                                            } catch (Exception e) {
                                                Log.e("xxx", "showProducts: "+e.getMessage());
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }


                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void showAllInApp() {

        try {
            ImmutableList<QueryProductDetailsParams.Product> productList = ImmutableList.of(
                    QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(LIFETIME)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()

            );

            QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                    .setProductList(productList)
                    .build();

            billingClient.queryProductDetailsAsync(
                    params,
                    (billingResult, prodDetailsList) -> {
                        // Process the result
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                            try {
                                productDetailsList.addAll(prodDetailsList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        for ( ProductDetails entry : prodDetailsList) {
                                            Log.e("xxx", "onPricesUpdated: " + entry);
                                            String sku = entry.getProductId();
                                            String price = entry.getOneTimePurchaseOfferDetails().getFormattedPrice();
                                            try {
                                                if (sku.equalsIgnoreCase(LIFETIME)) {
                                                    binding.tvPrice4.setText(price);
                                                }
                                            } catch (Exception e) {
                                                Log.e("xxx", "showProducts: "+e.getMessage());
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }


                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void launchPurchaseFlow(ProductDetails productDetails) {
        try {
            assert productDetails.getSubscriptionOfferDetails() != null;
            ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                    ImmutableList.of(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
                                    .build()
                    );
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build();

            billingClient.launchBillingFlow(this, billingFlowParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void launchPurchaseFlowForInApp(ProductDetails productDetails) {
        try {
            ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParamsList =
                    ImmutableList.of(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(productDetails)
                                    .build()
                    );
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build();

            billingClient.launchBillingFlow(this, billingFlowParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void verifySubPurchase(Purchase purchases) {

        try {
            AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchases.getPurchaseToken())
                    .build();

            billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    //use prefs to set premium
                    //  Toast.makeText(Subscriptions.this, "Subscription activated, Enjoy!", Toast.LENGTH_SHORT).show();
                    //Setting premium to 1
                    // 1 - premium
                    // 0 - no premium
                    WeatherApplication.trackingEvent("premium_ok");
                    prefs.setPremium(1);
                    restart();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void onResume() {
        super.onResume();
        try {
            if(billingClient != null){
            billingClient.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                    (billingResult, list) -> {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            for (Purchase purchase : list) {
                                if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                    verifySubPurchase(purchase);
                                }
                            }
                        }
                    }
            );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void restart() {
        Intent intent = new Intent(this, SplashActivity.class);
        this.startActivity(intent);
        this.finishAffinity();
    }

    private void initView() {
        binding.viewBuy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllBorder();
                enableBorder(binding.viewBorder4);
                try {
                    WeatherApplication.trackingEvent("premium_click");
                    //iapConnector.subscribe(PremiumActivity.this, YEARLY);
                    try {
                        for (ProductDetails productDetail : productDetailsList){
                            if(productDetail.getProductId().equals(LIFETIME)){
                                launchPurchaseFlowForInApp(productDetail);
                                return;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        binding.viewBuy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllBorder();
                enableBorder(binding.viewBorder1);
                try {
                    WeatherApplication.trackingEvent("remove_ads_click");
                    for (ProductDetails productDetail : productDetailsList){
                        if(productDetail.getProductId().equals(MONTHLY)){
                            launchPurchaseFlow(productDetail);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.viewBuy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllBorder();
                enableBorder(binding.viewBorder2);
                try {
                    WeatherApplication.trackingEvent("remove_ads_click");
                    for (ProductDetails productDetail : productDetailsList){
                        if(productDetail.getProductId().equals(MONTHLY_6)){
                            launchPurchaseFlow(productDetail);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.viewBuy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableAllBorder();
                enableBorder(binding.viewBorder3);
                try {
                    WeatherApplication.trackingEvent("remove_ads_click");
                    for (ProductDetails productDetail : productDetailsList){
                        if(productDetail.getProductId().equals(YEAR)){
                            launchPurchaseFlow(productDetail);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://docs.google.com/document/d/1prnBnlhwxSPGcXZ7EKvqKOFaxHObRMUS3BtgcMw5wHM/edit?usp=sharing";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });




//        Glide.with(this)
//                .load(R.drawable.bg_premium)
//                .transform(new GlideBlurTransformation(this))
//                .into(binding.imgBackground);


    }

    @Override
    public void onBackPressed() {
        if(isTrial){
            startActivity(new Intent(PremiumActivity.this, MainActivity.class));
            finish();
        }else {
            super.onBackPressed();
        }

    }

    private void disableAllBorder(){
        binding.viewBorder1.setVisibility(View.GONE);
        binding.viewBorder2.setVisibility(View.GONE);
        binding.viewBorder3.setVisibility(View.GONE);
        binding.viewBorder4.setVisibility(View.GONE);
    }

    private void enableBorder(View view){
        view.setVisibility(View.VISIBLE);
    }

}

package com.my.applovin_ads;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;

import java.util.concurrent.TimeUnit;

class Applovin_Ads extends AppCompatActivity {
    public static MaxAdView adView;
    public static MaxNativeAdLoader nativeAdLoader;
    public static FrameLayout nativeAdLayout;
    public static MaxNativeAdView nativeAdView;

    public static MaxAd nativeAd;
    //.........Intersital
    public static MaxInterstitialAd interstitialAd;
    public static int retryAttempt;
    public static Intent intersitalintent;
    public static void  Initlize_Sdk(Activity activity){
        AppLovinSdk.getInstance(activity ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( activity, new AppLovinSdk.SdkInitializationListener()
        {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
                Log.e("Applovin","Sdk Intilized Successfully");
                loadintersitalad(activity);
            }
        } );
    }

    public static void loadandshowbanner(Activity activity,FrameLayout frameLayout){
        adView = new MaxAdView( "96d598f412d429cc", activity );
        final boolean isTablet = AppLovinSdkUtils.isTablet( activity );
        final int heightPx = AppLovinSdkUtils.dpToPx( activity, isTablet ? 90 : 50 );


        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd maxAd) {
                Log.e("Applovin","Banner ad expended");
            }

            @Override
            public void onAdCollapsed(MaxAd maxAd) {
                Log.e("Applovin","Banner ad onAdCollapsed");
            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {
                Log.e("Applovin","Banner ad onAdLoaded");
            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {
                Log.e("Applovin","Banner ad onAdDisplayed");
            }

            @Override
            public void onAdHidden(MaxAd maxAd) {
                Log.e("Applovin","Banner ad onAdHidden");
            }

            @Override
            public void onAdClicked(MaxAd maxAd) {
                Log.e("Applovin","Banner ad onAdClicked");
            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {
                Log.e("Applovin","Banner ad onAdLoadFailed");
                Log.e("Applovin","Banner maxError Message"+maxError.getMessage());
                Log.e("Applovin","Banner maxError Code"+maxError.getCode());

            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                Log.e("Applovin","Banner ad onAdDisplayFailed");
            }
        });


        // Banner width must match the screen to be fully functional.
        adView.setLayoutParams( new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, heightPx ) );

        // Need to set the background or background color for banners to be fully functional.
        adView.setBackgroundColor( Color.BLACK );

        frameLayout.addView(adView);

        // Load the first ad.
        adView.loadAd();
    }

    public static void loadandshownative(Activity activity,FrameLayout native_ad_layout){
        nativeAdLayout = native_ad_layout;

        MaxNativeAdViewBinder binder = new MaxNativeAdViewBinder.Builder( R.layout.native_custom_ad_view )
                .setTitleTextViewId( R.id.title_text_view )
                .setBodyTextViewId( R.id.body_text_view )
                .setAdvertiserTextViewId( R.id.advertiser_text_view )
                .setIconImageViewId( R.id.icon_image_view )
                .setMediaContentViewGroupId( R.id.media_view_container )
                .setOptionsContentViewGroupId( R.id.options_view )
                .setStarRatingContentViewGroupId( R.id.star_rating_view )
                .setCallToActionButtonId( R.id.cta_button )
                .build();
        nativeAdView = new MaxNativeAdView( binder, activity );

        nativeAdLoader = new MaxNativeAdLoader( "cc6cb908875542f0", activity );

        nativeAdLoader.setNativeAdListener( new MaxNativeAdListener()
        {
            @Override
            public void onNativeAdLoaded(@Nullable final MaxNativeAdView nativeAdView, final MaxAd ad)
            {
                Log.e("Applovin","Native Ad Load successfully");
                // Cleanup any pre-existing native ad to prevent memory leaks.
                if ( nativeAd != null )
                {
                    nativeAdLoader.destroy( nativeAd );
                }

                // Save ad for cleanup.
                nativeAd = ad;

                // Add ad view to view.
                nativeAdLayout.removeAllViews();
                nativeAdLayout.addView( nativeAdView );
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error)
            {
                Log.e("Applovin","Native ad load failed");
                Log.e("Applovin","error code: "+error.getCode());
                Log.e("Applovin","error message: "+error.getMessage());
                nativeAdLoader.loadAd( nativeAdView );

            }

            @Override
            public void onNativeAdClicked(final MaxAd ad)
            {
                Log.e("Applovin","Native ad clicked");
            }

            @Override
            public void onNativeAdExpired(final MaxAd ad)
            {
                Log.e("Applovin","Native ad expired");
            }
        } );

        nativeAdLoader.loadAd( nativeAdView );

    }


    public static void loadintersitalad(Activity activity){
        interstitialAd = new MaxInterstitialAd( "b4b263de35ef26d5", activity );

        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd maxAd) {
                // Reset retry attempt
                retryAttempt = 0;
                Log.e("Applovin","Intersitail ad load succesffuly");
            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {
                Log.e("Applovin","Intersitail ad displayed");
            }

            @Override
            public void onAdHidden(MaxAd maxAd) {
                Log.e("Applovin","Intersitail ad on hidden");
                activity.startActivity(intersitalintent);
                loadintersitalad(activity);
            }

            @Override
            public void onAdClicked(MaxAd maxAd) {
                Log.e("Applovin","Intersitail ad clicked");
            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {
                // Interstitial ad failed to load. We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds).
                activity.startActivity(intersitalintent);
                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        interstitialAd.loadAd();
                    }
                }, delayMillis );
                Log.e("Applovin","Intersitail ad load failed");
                Log.e("Applovin","Intersitail ad message"+maxError.getMessage());
                Log.e("Applovin","Intersitail ad code: "+maxError.getCode());
            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                Log.e("Applovin","Intersitail ad display failed");
                loadintersitalad(activity);
                activity.startActivity(intersitalintent);
            }
        });

        interstitialAd.loadAd();


    }
    public static void showintersital(Activity activity,Intent intent){
        intersitalintent=intent;
        if ( interstitialAd.isReady() )
        {
            interstitialAd.showAd();
        }
        else {
            if (intersitalintent!=null) {
                activity.startActivity(intersitalintent);
            }
            loadintersitalad(activity);
        }
    }


    @Override
    protected void onDestroy() {
        // Must destroy native ad or else there will be memory leaks.
        if ( nativeAd != null )
        {
            // Call destroy on the native ad from any native ad loader.
            nativeAdLoader.destroy( nativeAd );
        }

        // Destroy the actual loader itself
        nativeAdLoader.destroy();

        super.onDestroy();
    }
}

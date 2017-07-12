package vn.bongtran.antirat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

public class MainActivity extends Activity {
    // Remove the below line after defining your own ad unit ID.
//    private static final String TOAST_TEXT = "Test ads are being shown. "
//            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";

//    private static final int START_LEVEL = 1;
//    private int mLevel;
    private Button btControl;
    private InterstitialAd mInterstitialAd;
//    private TextView mLevelTextView;
    private LinearLayout llAdView;
    private AdView adView;
    private MediaPlayer mPlayer;
    private ImageView imageView;
    private boolean isPlaying = false;
//    private RotateAnimation rotateAnimation;
    private NotificationManager notifier;
    Notification notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the next level button, which tries to show an interstitial when clicked.
        btControl = ((Button) findViewById(R.id.next_level_button));
//        btControl.setEnabled(false);
        btControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showInterstitial();
                controlMedia();
            }
        });

        // Create the text view to show the level number.
//        mLevelTextView = (TextView) findViewById(R.id.level);
//        mLevel = START_LEVEL;

        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.llAdView = (LinearLayout) findViewById(R.id.ll_ad);
        this.adView = (AdView) findViewById(R.id.adView);
        this.adView.loadAd(new AdRequest.Builder().build());
        this.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                llAdView.setVisibility(View.VISIBLE);
            }
        });

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).

//        rotateAnimation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setDuration(Animation.INFINITE);

        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
//        Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.antirat)
//                .setSmallIcon(R.drawable.antirat_inverse)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(pIntent)
                .build();

        notification.flags |= Notification.FLAG_NO_CLEAR
                | Notification.FLAG_ONGOING_EVENT;
        notifier = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onPause() {
        this.adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.adView.resume();
    }

    @Override
    protected void onDestroy() {
        this.adView.destroy();
        this.mPlayer.stop();
        super.onDestroy();
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
//                btControl.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                btControl.setEnabled(true);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
//                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
//            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
//        btControl.setEnabled(false);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
//        mLevelTextView.setText("Level " + (++mLevel));
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    private void controlMedia(){
        if(isPlaying){
            notifier.cancel(1);
            btControl.setText(getString(R.string.start_level));
            imageView.setImageResource(R.drawable.anticat512);
//            imageView.clearAnimation();
            pauseSound();
            showInterstitial();
            this.adView.loadAd(new AdRequest.Builder().build());
        }else {
            notifier.notify(1, notification);
            btControl.setText(getString(R.string.next_level));
            imageView.setImageResource(R.drawable.antirat512);
//            imageView.startAnimation(rotateAnimation);
            playSound();
            mInterstitialAd = newInterstitialAd();
            loadInterstitial();
        }
    }

    private void playSound(){
        try {
        if(mPlayer != null){
            mPlayer.start();
        }
        else {
            Random r = new Random();
            int random = r.nextInt(3);
            switch (random){
                case 0: {
                    mPlayer = MediaPlayer.create(MainActivity.this, R.raw.meo01);
                    mPlayer.setLooping(true);
                    mPlayer.start();
                    break;
                }
                case 1: {
                    mPlayer = MediaPlayer.create(MainActivity.this, R.raw.meo2);
                    mPlayer.setLooping(true);
                    mPlayer.start();
                    break;
                }
                case 2: {
                    mPlayer = MediaPlayer.create(MainActivity.this, R.raw.threemeo);
                    mPlayer.setLooping(true);
                    mPlayer.start();
                    break;
                }
                case 3: {
                    mPlayer = MediaPlayer.create(MainActivity.this, R.raw.meomix);
                    mPlayer.setLooping(true);
                    mPlayer.start();
                    break;
                }
            }
        }

        isPlaying = true;
        }catch (Exception e){

        }
    }

    private void pauseSound(){
        if(mPlayer != null){
            try {
                mPlayer.pause();
                isPlaying = false;
            }catch (Exception e){

            }
        }
    }
}

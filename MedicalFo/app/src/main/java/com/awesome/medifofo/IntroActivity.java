package com.awesome.medifofo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class IntroActivity extends AppCompatActivity {

    private Handler handler;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        init();

        /*
         Before GridView is loaded, ImageLoader should be loaded for Memory out of bounce
          */
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        handler.postDelayed(runnable, 2000);


    }

    public void init() {
        handler = new Handler();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}
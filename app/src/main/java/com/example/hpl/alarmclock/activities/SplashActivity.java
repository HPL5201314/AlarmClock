package com.example.hpl.alarmclock.activities;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.hpl.alarmclock.R;
import com.example.hpl.alarmclock.utils.MyUtil;
import com.example.hpl.alarmclock.utils.LogUtil;

/**
 * Created by Administrator on 2016/10/24.
 */

public class SplashActivity extends BaseActivity{

    private static final String LOG_TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 解决初次安装后打开后按home返回后重新打开重启问题。。。
        if (!this.isTaskRoot()) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;//finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }

/*        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
        }*/

        overridePendingTransition(R.anim.zoomin, 0);
        // 禁止滑动后退
        setSwipeBackEnable(false);
        setContentView(R.layout.activity_splash);
        MyUtil.setStatusBarTranslucent(this);
        assignViews();

    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
    }

    private void assignViews() {
        // 设置版本号
        setVersion();
        // 设置标语
        setSlogan();
        // 开启欢迎动画
        startAnimation();
    }

    private void startAnimation() {
        final View splashIv = findViewById(R.id.splash_iv);
        ValueAnimator animator = ValueAnimator.ofObject(new FloatEvaluator(), 1.0f, 1.2f);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                if (value != 1.2f) {
                    splashIv.setScaleX(value);
                    splashIv.setScaleY(value);
                } else {
                    goToActivity();
                }
            }

            private void goToActivity() {
                MyUtil.startActivity(SplashActivity.this, MainActivity.class);
                overridePendingTransition(0, android.R.anim.fade_out);
                finish();
            }
        });
        animator.start();
    }

    private void setSlogan() {
        try {
            AssetManager mgr = getAssets();
            Typeface fontFace = Typeface.createFromAsset(mgr, "fonts/weac_slogan.ttf");
            TextView SloganTv = (TextView) findViewById(R.id.weac_slogan_tv);
            SloganTv.setTypeface(fontFace);
        } catch (Exception e) {
            LogUtil.e(LOG_TAG, "Typeface.createFromAsset: " + e.toString());
        }
    }

    private void setVersion() {
        TextView versionTv = (TextView) findViewById(R.id.version_tv);
        versionTv.setText(getString(R.string.weac_version, MyUtil.getVersion(this)));
    }
}

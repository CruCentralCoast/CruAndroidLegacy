package org.androidcru.crucentralcoast.presentation.views.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.presenters.MvpBasePresenter;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends MvpActivity<MvpBasePresenter>
{
    @Bind(R.id.logo) protected ImageView logo;
    @Bind(R.id.central_coast) protected TextView centralCoast;

    @Override
    protected MvpBasePresenter createPresenter()
    {
        return new MvpBasePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        Glide.with(this).load(R.drawable.logo_grey).into(logo);
        centralCoast.setText(getResources().getText(R.string.central_coast));
        centralCoast.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/FreigSanProLig.otf"));
        switchToApp();
    }

    public void switchToApp()
    {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final long DELAY = TimeUnit.MILLISECONDS.convert(2, TimeUnit.SECONDS);

        Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(DELAY);
                }
                catch (InterruptedException e)
                {
                    Logger.d("Delay was interrupted.");
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });
            }
        };
        thread.start();
    }
}

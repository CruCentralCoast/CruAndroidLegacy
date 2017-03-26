package com.crucentralcoast.app.presentation.views.onboarding;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.OnboardingInfo;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.subscriptions.SubscriptionActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brittanyberlanga on 3/20/17.
 */

public class OnboardingActivity extends AppCompatActivity {
    @BindView(R.id.prev_button)
    protected Button skipPrevButton;
    @BindView(R.id.next_button)
    protected Button nextButton;
    @BindView(R.id.view_pager)
    protected ViewPager viewPager;
    @BindView(R.id.dot_tabs)
    protected TabLayout dotTabLayout;

    private List<OnboardingInfo> onboardingInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
        ViewUtil.setFont(skipPrevButton, AppConstants.FREIG_SAN_PRO_LIGHT);
        ViewUtil.setFont(nextButton, AppConstants.FREIG_SAN_PRO_LIGHT);
        onboardingInfo = OnboardingInfo.getDefaultOnboardingInfo();
        viewPager.setAdapter(new OnboardingPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                Resources res = getResources();
                if (position == onboardingInfo.size()) {
                    nextButton.setText(res.getString(R.string.done));
                }
                else {
                    nextButton.setText(res.getString(R.string.next));
                }
                if (position == 0) {
                    skipPrevButton.setText(res.getString(R.string.skip));
                }
                else {
                    skipPrevButton.setText(res.getString(R.string.previous));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        dotTabLayout.setupWithViewPager(viewPager, true);
    }

    @OnClick(R.id.prev_button)
    public void onClickPrevButton() {
        int currItem = viewPager.getCurrentItem();
        if (currItem == 0) {
            // skip
            continueToSubscriptions();
        }
        else {
            viewPager.setCurrentItem(currItem - 1);
        }
    }

    @OnClick(R.id.next_button)
    public void onClickNextButton() {
        int currItem = viewPager.getCurrentItem();
        if (currItem < onboardingInfo.size()) {
            viewPager.setCurrentItem(currItem + 1);
        }
        else {
            // done
            continueToSubscriptions();
        }
    }

    private void continueToSubscriptions() {
        Intent intent = new Intent(this, SubscriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private class OnboardingPagerAdapter extends FragmentStatePagerAdapter {
        OnboardingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = new InfoFragment();
            Bundle args = new Bundle();
            if (position == 0) {
                args.putBoolean(InfoFragment.CRU_INFO_EXTRA, true);
            }
            else {
                OnboardingInfo info = onboardingInfo.get(position - 1);
                args.putInt(InfoFragment.TITLE_ID_EXTRA, info.titleId);
                args.putInt(InfoFragment.DESC_ID_EXTRA, info.descriptionId);
                args.putInt(InfoFragment.IMG_ID_EXTRA, info.drawableId);
            }
            frag.setArguments(args);
            return frag;
        }

        @Override
        public int getCount() {
            return onboardingInfo.size() + 1;
        }
    }
}

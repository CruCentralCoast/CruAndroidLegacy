package com.crucentralcoast.app.presentation.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.providers.FacebookProvider;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.presentation.views.communitygroups.CommunityGroupsFragment;
import com.crucentralcoast.app.presentation.views.events.EventsFragment;
import com.crucentralcoast.app.presentation.views.home.HomeFragment;
import com.crucentralcoast.app.presentation.views.ministryteams.MinistryTeamsFragment;
import com.crucentralcoast.app.presentation.views.notifications.NotificationFragment;
import com.crucentralcoast.app.presentation.views.resources.ResourcesFragment;
import com.crucentralcoast.app.presentation.views.ridesharing.myrides.MyRidesFragment;
import com.crucentralcoast.app.presentation.views.settings.SettingsActivity;
import com.crucentralcoast.app.presentation.views.summermissions.SummerMissionsFragment;
import com.crucentralcoast.app.presentation.views.videos.VideosFragment;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.banner_image)
    ImageView mBannerImage;

    private boolean isNavHome = true;

    private static Activity activity;

    private static final String STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String DIMEN = "dimen";
    private static final String ANDROID = "android";
    private static final String BANNER_IMAGE = "https://s3-us-west-1.amazonaws.com/" +
            "static.crucentralcoast.com/images/misc/banner.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Picasso.with(getContext())
                .load(BANNER_IMAGE)
                .placeholder(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)))
                .fit()
                .centerCrop()
                .into(mBannerImage);

        activity = this;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.setDrawerSlideAnimationEnabled(false);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            switchToHome();
        }

        checkPlayServicesCode();
    }

    private void checkPlayServicesCode() {
        int playServicesCode = SharedPreferencesUtil.getPlayServicesCode();
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        if (playServicesCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(playServicesCode)) {
                apiAvailability.getErrorDialog(this, playServicesCode, AppConstants.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            }
            else {
                //REVIEW magic strings
                new AlertDialog.Builder(this)
                        .setTitle("Unsupported Device")
                        .setMessage("Sorry but your device does not support Google Play Services")
                        .setOnCancelListener(dialog -> finish())
                        .show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean displayAsSelectedItem = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        switch (id) {
            case R.id.nav_home:
                mToolbarTitle.setText(getString(R.string.nav_home));
                transaction.replace(R.id.content, HomeFragment.newInstance()).commit();
                isNavHome = true;
                break;
            case R.id.nav_events:
                mToolbarTitle.setText(getString(R.string.nav_events));
                transaction.replace(R.id.content, EventsFragment.newInstance()).commit();
                isNavHome = false;
                break;
            case R.id.nav_my_rides:
                mToolbarTitle.setText(getString(R.string.nav_my_rides));
                transaction.replace(R.id.content, MyRidesFragment.newInstance()).commit();
                isNavHome = false;
                break;
            case R.id.nav_summer_missions:
                mToolbarTitle.setText(getString(R.string.nav_summer_missions));
                transaction.replace(R.id.content, SummerMissionsFragment.newInstance()).commit();
                isNavHome = false;
                break;
            case R.id.nav_community_groups:
                mToolbarTitle.setText(getString(R.string.community_groups));
                transaction.replace(R.id.content, CommunityGroupsFragment.newInstance()).commit();
                isNavHome = false;
                break;
            case R.id.nav_ministry_teams:
                mToolbarTitle.setText(getString(R.string.ministry_teams));
                transaction.replace(R.id.content, MinistryTeamsFragment.newInstance()).commit();
                isNavHome = false;
                break;
            case R.id.nav_resources:
                mToolbarTitle.setText(getString(R.string.resources));
                transaction.replace(R.id.content, ResourcesFragment.newInstance()).commit();
                isNavHome = false;
                break;
            case R.id.nav_videos:
                mToolbarTitle.setText(getString(R.string.nav_videos));
                transaction.replace(R.id.content, VideosFragment.newInstance()).commit();
                isNavHome = false;
                break;
            case R.id.nav_notifications:
                mToolbarTitle.setText(getString(R.string.nav_notifications));
                transaction.replace(R.id.content, NotificationFragment.newInstance()).commit();
                isNavHome = false;
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                displayAsSelectedItem = false;
                break;
        }

        if (isNavHome) {
            unlockCollapsingToolbar();
            mAppBarLayout.setExpanded(true, false);
            ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
            params.topMargin = 0;
        }
        else {
            lockCollapsingToolbar();
            mAppBarLayout.setExpanded(false, false);
            ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
            params.topMargin = getStatusBarHeight();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return displayAsSelectedItem;
    }

    private void lockCollapsingToolbar() {
        mBannerImage.setVisibility(View.GONE);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void unlockCollapsingToolbar() {
        mBannerImage.setVisibility(View.VISIBLE);
        mToolbar.setBackgroundColor(0);
    }

    public void switchToMyRides(Bundle bundle) {
        mNavigationView.setCheckedItem(R.id.nav_my_rides);
        mToolbarTitle.setText(getString(R.string.nav_my_rides));
        MyRidesFragment fragment = MyRidesFragment.newInstance();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, fragment).commit();
        mAppBarLayout.setExpanded(false, true);
        lockCollapsingToolbar();
        ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        params.topMargin = getStatusBarHeight();
    }

    //REVIEW this should be used in onNavigationItemSelected
    public void switchToEvents() {
        mNavigationView.setCheckedItem(R.id.nav_events);
        mToolbarTitle.setText(getString(R.string.nav_events));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, EventsFragment.newInstance()).commit();
        mAppBarLayout.setExpanded(false, true);
        lockCollapsingToolbar();
        ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        params.topMargin = getStatusBarHeight();
    }

    public void switchToVideos() {
        mNavigationView.setCheckedItem(R.id.nav_videos);
        mToolbarTitle.setText(getString(R.string.nav_videos));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, VideosFragment.newInstance()).commit();
        mAppBarLayout.setExpanded(false, true);
        lockCollapsingToolbar();
        ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        params.topMargin = getStatusBarHeight();
    }

    public void switchToHome() {
        mNavigationView.setCheckedItem(R.id.nav_home);
        mToolbarTitle.setText(getString(R.string.nav_home));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.content, HomeFragment.newInstance()).commit();
        unlockCollapsingToolbar();
        mAppBarLayout.setExpanded(true, true);
        ViewGroup.MarginLayoutParams params =(ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
        params.topMargin = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.DRIVER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.MY_RIDES_TAB, AppConstants.DRIVER_TAB);
            switchToMyRides(bundle);
        }
        else if (requestCode == AppConstants.PASSENGER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.MY_RIDES_TAB, AppConstants.PASSENGER_TAB);
            switchToMyRides(bundle);
        }

        FacebookProvider.tokenReceived(requestCode, resultCode, data);
    }

    public static void loginWithFacebook() {
        LoginManager.getInstance().logInWithPublishPermissions(activity, Collections.singletonList("rsvp_event"));
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(STATUS_BAR_HEIGHT, DIMEN, ANDROID);

        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}

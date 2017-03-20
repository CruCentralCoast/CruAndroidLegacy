package com.crucentralcoast.app.presentation.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.providers.FacebookProvider;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.presentation.views.communitygroups.CommunityGroupsFragment;
import com.crucentralcoast.app.presentation.views.events.EventsFragment;
import com.crucentralcoast.app.presentation.views.feed.FeedFragment;
import com.crucentralcoast.app.presentation.views.hub.HubFragment;
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

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        activity = this;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.setDrawerSlideAnimationEnabled(false);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            switchToFeed();
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
            case R.id.nav_feed:
                // mToolbar.setTitle(R.string.nav_feed);
                // transaction.replace(R.id.content, new FeedFragment()).commit();
                mToolbar.setTitle(R.string.nav_hub);
                transaction.replace(R.id.content, HubFragment.newInstance()).commit();
                break;
            case R.id.nav_events:
                mToolbar.setTitle(R.string.nav_events);
                transaction.replace(R.id.content, EventsFragment.newInstance()).commit();
                break;
            case R.id.nav_my_rides:
                mToolbar.setTitle(R.string.nav_my_rides);
                transaction.replace(R.id.content, MyRidesFragment.newInstance()).commit();
                break;
            case R.id.nav_summer_missions:
                mToolbar.setTitle(R.string.nav_summer_missions);
                transaction.replace(R.id.content, SummerMissionsFragment.newInstance()).commit();
                break;
            case R.id.nav_community_groups:
                mToolbar.setTitle(R.string.community_groups);
                transaction.replace(R.id.content, CommunityGroupsFragment.newInstance()).commit();
                break;
            case R.id.nav_ministry_teams:
                mToolbar.setTitle(R.string.ministry_teams);
                transaction.replace(R.id.content, MinistryTeamsFragment.newInstance()).commit();
                break;
            case R.id.nav_resources:
                mToolbar.setTitle(R.string.resources);
                transaction.replace(R.id.content, ResourcesFragment.newInstance()).commit();
                break;
            case R.id.nav_videos:
                mToolbar.setTitle(R.string.nav_videos);
                transaction.replace(R.id.content, VideosFragment.newInstance()).commit();
                break;
            case R.id.nav_notifications:
                mToolbar.setTitle(R.string.nav_notifications);
                transaction.replace(R.id.content, NotificationFragment.newInstance()).commit();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                displayAsSelectedItem = false;
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return displayAsSelectedItem;
    }

    public void switchToMyRides(Bundle b) {
        mNavigationView.setCheckedItem(R.id.nav_my_rides);
        mToolbar.setTitle(R.string.nav_my_rides);
        MyRidesFragment fragment = new MyRidesFragment();
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    //REVIEW this should be used in onNavigationItemSelected
    public void switchToEvents() {
        mNavigationView.setCheckedItem(R.id.nav_events);
        mToolbar.setTitle(R.string.nav_events);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new EventsFragment()).commit();
    }

    public void switchToVideos() {
        mNavigationView.setCheckedItem(R.id.nav_videos);
        mToolbar.setTitle(R.string.nav_videos);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new VideosFragment()).commit();
    }

    public void switchToFeed() {
        mNavigationView.setCheckedItem(R.id.nav_feed);
        mToolbar.setTitle(R.string.nav_feed);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new FeedFragment()).commit();
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
}

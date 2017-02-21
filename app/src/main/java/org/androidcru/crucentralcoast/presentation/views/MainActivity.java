package org.androidcru.crucentralcoast.presentation.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.providers.FacebookProvider;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.presentation.views.communitygroups.CommunityGroupsFragment;
import org.androidcru.crucentralcoast.presentation.views.events.EventsFragment;
import org.androidcru.crucentralcoast.presentation.views.feed.FeedFragment;
import org.androidcru.crucentralcoast.presentation.views.ministryteams.MinistryTeamsFragment;
import org.androidcru.crucentralcoast.presentation.views.notifications.NotificationFragment;
import org.androidcru.crucentralcoast.presentation.views.resources.ResourcesFragment;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides.MyRidesFragment;
import org.androidcru.crucentralcoast.presentation.views.settings.SettingsActivity;
import org.androidcru.crucentralcoast.presentation.views.summermissions.SummerMissionsFragment;
import org.androidcru.crucentralcoast.presentation.views.videos.VideosFragment;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

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
        switch (id) {
            case R.id.nav_feed:
                mToolbar.setTitle(R.string.nav_feed);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new FeedFragment()).commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.content, new HubFragment()).commit();
                break;
            case R.id.nav_events:
                mToolbar.setTitle(R.string.nav_events);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new EventsFragment()).commit();
                break;
            case R.id.nav_my_rides:
                mToolbar.setTitle(R.string.nav_my_rides);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new MyRidesFragment()).commit();
                break;
            case R.id.nav_summer_missions:
                mToolbar.setTitle(R.string.nav_summer_missions);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new SummerMissionsFragment()).commit();
                break;
            case R.id.nav_community_groups:
                mToolbar.setTitle(R.string.community_groups);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new CommunityGroupsFragment()).commit();
                break;
            case R.id.nav_ministry_teams:
                mToolbar.setTitle(R.string.ministry_teams);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new MinistryTeamsFragment()).commit();
                break;
            case R.id.nav_resources:
                mToolbar.setTitle(R.string.resources);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new ResourcesFragment()).commit();
                break;
            case R.id.nav_videos:
                mToolbar.setTitle(R.string.nav_videos);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new VideosFragment()).commit();
                break;
            case R.id.nav_notifications:
                mToolbar.setTitle(R.string.nav_notifications);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new NotificationFragment()).commit();
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

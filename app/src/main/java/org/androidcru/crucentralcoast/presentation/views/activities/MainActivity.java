package org.androidcru.crucentralcoast.presentation.views.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.presenters.MvpBasePresenter;
import org.androidcru.crucentralcoast.presentation.views.fragments.EventsFragment;
import org.androidcru.crucentralcoast.presentation.views.fragments.SubscriptionsFragment;
import org.androidcru.crucentralcoast.presentation.views.views.MvpView;
import org.androidcru.crucentralcoast.presentation.views.fragments.VideoFragment;

public class MainActivity extends MvpActivity<MvpBasePresenter>
        implements NavigationView.OnNavigationItemSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected MvpBasePresenter createPresenter()
    {
        return new MvpBasePresenter();
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_home:
                break;
            case R.id.nav_tools:
                break;
            case R.id.nav_events:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new EventsFragment()).commit();
                break;
            case R.id.nav_cruber:
                break;
            case R.id.nav_summer_missions:
                break;
            case R.id.nav_community_groups:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new SubscriptionsFragment()).commit();
                break;
            case R.id.nav_ministry_teams:
                break;
            case R.id.nav_articles:

                break;
            case R.id.nav_videos:
                getSupportFragmentManager().beginTransaction().replace(R.id.content, new VideoFragment()).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

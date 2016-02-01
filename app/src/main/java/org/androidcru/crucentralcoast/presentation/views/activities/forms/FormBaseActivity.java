package org.androidcru.crucentralcoast.presentation.views.activities.forms;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;

public class FormBaseActivity extends AppCompatActivity implements FormHolder
{
    RelativeLayout previousView;
    RelativeLayout nextView;

    ImageView previousIcon;
    ImageView nextIcon;

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    //NestedScrollView nestedScrollView;

    private FormPage currentFormPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        setupUI();
        setupButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.getItem(0).setIcon(DrawableUtil.getTintedDrawable(this, R.drawable.ic_close_grey600_48dp, android.R.color.white));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.action_close:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupUI()
    {
        //nestedScrollView = (NestedScrollView) findViewById(R.id.scrollView);
        //nestedScrollView.setNestedScrollingEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //appBarLayout.setExpanded(false, false);

        previousView = (RelativeLayout) findViewById(R.id.prev);
        nextView = (RelativeLayout) findViewById(R.id.next);

        previousIcon = (ImageView) findViewById(R.id.prevIcon);
        nextIcon = (ImageView) findViewById(R.id.nextIcon);
    }

    private void setupButtons()
    {
        previousIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_chevron_left_grey600_48dp));
        nextIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_chevron_right_grey600_48dp));
    }

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        super.onAttachFragment(fragment);
        if(fragment instanceof FormPage)
        {
            currentFormPage = (FormPage) fragment;
            setupButtonListeners();
            currentFormPage.setFormHolder(this);
        }
    }

    private void setupButtonListeners()
    {
        previousView.setOnClickListener(v -> {
            if (currentFormPage != null)
                currentFormPage.onPrevious();
        });

        nextView.setOnClickListener(v -> {
            if (currentFormPage != null)
                currentFormPage.onNext();
        });
    }

    @Override
    public void setTitle(String title)
    {
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    public void setPreviousVisbility(int visibility)
    {
        previousView.setVisibility(visibility);
    }

    @Override
    public void setNextVisibility(int visibility)
    {
        nextView.setVisibility(visibility);
    }

    @Override
    public void setToolbarExpansion(boolean expanded)
    {
        appBarLayout.setExpanded(expanded);
    }
}



package org.androidcru.crucentralcoast.presentation.views.forms;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.databinding.ActivityFormBinding;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;

public class FormActivity extends AppCompatActivity implements FormHolder
{
    private ActivityFormBinding binding;

    private FormContent currentFormContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_form);
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

    protected void setCurrentFormContent(FormContent content)
    {
        currentFormContent = content;
        setupButtonListeners();
    }

    private void setupButtonListeners()
    {
        binding.prev.setOnClickListener(v -> {
            if (currentFormContent != null)
                currentFormContent.onPrevious();
        });

        binding.next.setOnClickListener(v -> {
            if (currentFormContent != null)
                currentFormContent.onNext();
        });
    }

    @Override
    public void onBackPressed()
    {
        if(binding.viewPager.getCurrentItem() != 0)
        {
            currentFormContent.onPrevious();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void setAdapter(FormAdapter adapter)
    {
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                adapter.getFormPage(position).formHolder.clearUI();
                adapter.getFormPage(position).setupUI();
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    @Override
    public void clearUI()
    {
        setTitle(getResources().getString(R.string.app_name));
        setNavigationVisibility(View.VISIBLE);
        setPreviousVisibility(View.VISIBLE);
        setNextVisibility(View.VISIBLE);
        setToolbarExpansion(false);
    }

    @Override
    public void setTitle(String title)
    {
        binding.collapsingToolbar.setTitle(title);
    }

    @Override
    public void setPreviousVisibility(int visibility)
    {
        binding.prev.setVisibility(visibility);
    }

    @Override
    public void setNextVisibility(int visibility)
    {
        binding.next.setVisibility(visibility);
    }

    @Override
    public void setToolbarExpansion(boolean expanded)
    {
        binding.appbar.setExpanded(expanded);
    }

    @Override
    public void complete()
    {
        finish();
    }

    @Override
    public void setNavigationVisibility(int visibility)
    {
        binding.bottomBar.setVisibility(visibility);
    }

    @Override
    public void next()
    {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
    }

    @Override
    public void prev()
    {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
    }
}



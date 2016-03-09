package org.androidcru.crucentralcoast.presentation.views.forms;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;

import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FormActivity extends AppCompatActivity implements FormHolder
{
    private FormContent currentFormContent;
    private Stack<Object> dataObjects;
    public FormState formState;

    @Bind(R.id.appbar) AppBarLayout appBar;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.bottom_bar) RelativeLayout bottomBar;
    @Bind(R.id.prev) RelativeLayout prev;
    @Bind(R.id.next) RelativeLayout next;
    @Bind(R.id.nextText) TextView nextText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        dataObjects = new Stack<>();

        ButterKnife.bind(this);
        formState = FormState.PROGRESS;
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
        prev.setOnClickListener(v -> {
            if (currentFormContent != null)
                currentFormContent.onPrevious();
        });

        next.setOnClickListener(v -> {
            if (currentFormContent != null)
                currentFormContent.onNext();
        });
    }

    @Override
    public void onBackPressed()
    {
        if (formState == FormState.FINISH)
            complete();
        else if(viewPager.getCurrentItem() != 0)
            currentFormContent.onPrevious();
        else
            super.onBackPressed();
    }

    @Override
    public void setAdapter(FormAdapter adapter)
    {
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
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
        setNextText("NEXT");
    }

    @Override
    public void setTitle(String title)
    {
        toolbar.setTitle(title);
    }

    @Override
    public void setSubtitle(String subtitle)
    {
        toolbar.setSubtitle(subtitle);
    }

    @Override
    public void setPreviousVisibility(int visibility)
    {
        prev.setVisibility(visibility);
    }

    @Override
    public void setNextVisibility(int visibility)
    {
        next.setVisibility(visibility);
    }

    @Override
    public void complete()
    {
        finish();
    }

    @Override
    public void setNavigationVisibility(int visibility)
    {
        bottomBar.setVisibility(visibility);
    }

    @Override
    public void next()
    {
        if(viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1)
        {
            complete();
        }
        else
        {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void setFormState(FormState state)
    {
        switch(state)
        {
            case PROGRESS:
                setNextText("NEXT");
                break;
            case FINISH:
                setNextText(FormState.FINISH.toString());
                setPreviousVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setNextText(String text)
    {
        nextText.setText(text);
    }

    @Override
    public void prev()
    {
        if(!dataObjects.isEmpty())
        {
            dataObjects.pop();
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    @Override
    public void setNavigationClickable(boolean isClickable)
    {
        prev.setClickable(isClickable);
        next.setClickable(isClickable);
    }

    @Override
    public void addDataObject(Object dataObject)
    {
        this.dataObjects.push(dataObject);
    }

    @Override
    public Object getDataObject()
    {
        if(dataObjects.isEmpty())
            return null;
        return dataObjects.peek();
    }
}



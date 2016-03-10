package org.androidcru.crucentralcoast.presentation.views.forms;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;

import java.util.ArrayList;
import java.util.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FormActivity extends AppCompatActivity implements FormHolder
{
    private FormContent currentFormContent;
    private ArrayList<Object> dataObjects;
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

        ButterKnife.bind(this);
        formState = FormState.PROGRESS;
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
        dataObjects = new ArrayList<>();
        for(int i = 0; i < adapter.getCount(); i++)
        {
            dataObjects.add(null);
        }
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
        toolbar.requestLayout();
        toolbar.invalidate();
    }

    @Override
    public void setSubtitle(String subtitle)
    {
        toolbar.setSubtitle(subtitle);
        toolbar.requestLayout();
        toolbar.invalidate();
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
        formState = state;
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
        if(viewPager.getCurrentItem() + 1 >= dataObjects.size())
        {
            this.dataObjects.add(dataObject);
            return;
        }
        this.dataObjects.set(viewPager.getCurrentItem() + 1, dataObject);
    }

    protected void setFirstDataObject(Object dataObject) {
        if (dataObjects != null && !dataObjects.isEmpty()) {
            dataObjects.set(0, dataObject);
        }
    }

    @Override
    public Object getDataObject()
    {
        if(dataObjects.size() < viewPager.getCurrentItem())
            return null;
        return dataObjects.get(viewPager.getCurrentItem());
    }
}



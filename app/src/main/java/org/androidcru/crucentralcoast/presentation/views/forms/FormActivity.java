package org.androidcru.crucentralcoast.presentation.views.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class    FormActivity extends AppCompatActivity implements FormHolder
{
    private FormContent currentFormContent;
    private HashMap<String, Object> dataObjects = new HashMap<>();
    private ArrayList<FormContentFragment> fragments = new ArrayList<>();
    private int currentIndex = 0;

    private FragmentManager fm;

    public FormState formState;

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

        fm = getSupportFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.form, menu);
        return true;
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

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        super.onAttachFragment(fragment);
        if(!(fragment instanceof FormContentFragment))
        {
            throw new RuntimeException("Only " + FormContentFragment.class.getSimpleName()
                    + " are allowed to be attached to this Activity.");
        }
        else
        {
            currentFormContent = (FormContentFragment) fragment;
        }
    }

    private void onPageChange()
    {
        if(fragments.size() <=  currentIndex)
        {
            complete();
            return;
        }

        clearUI();
        if(currentIndex == 0 && fragments.size() != currentIndex + 1)
        {
            formState = FormState.PROGRESS;
            setPreviousVisibility(View.GONE);
            nextText.setText("NEXT");
        }
        else if(fragments.size() == currentIndex + 1)
        {
            formState = FormState.FINISH;
            setPreviousVisibility(View.GONE);
            setNextText("FINISH");
        }
        else
        {
            formState = FormState.PROGRESS;
            nextText.setText("NEXT");
        }
    }

    @Override
    public void setFormContent(List<FormContentFragment> fragments)
    {
        if(fragments != null && !fragments.isEmpty())
        {
            setupButtonListeners();
            this.fragments = new ArrayList<>(fragments);
            performTransaction(fragments.get(0));
            onPageChange();
        }
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
        if (formState == FormState.FINISH || currentIndex == 0)
            complete();
        else
            currentFormContent.onPrevious();
    }

    public void performTransaction(Fragment fragment) {
        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left,
                R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.content, fragment).addToBackStack(null).commit();
    }

    private void clearUI()
    {
        setTitle(getResources().getString(R.string.app_name));
        setNavigationVisibility(View.VISIBLE);
        setPreviousVisibility(View.VISIBLE);
        setNextVisibility(View.VISIBLE);
        setNavigationClickable(true);
        setNextText("NEXT");
    }

    @Override
    public void setTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setSubtitle(String subtitle)
    {
        getSupportActionBar().setSubtitle(subtitle);
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
        currentIndex++;
        FormContentFragment nextFragment = currentIndex < fragments.size() ? fragments.get(currentIndex) : null;
        if(nextFragment != null)
        {
            performTransaction(nextFragment);
        }
        onPageChange();
    }

    @Override
    public void setNextText(String text)
    {
        nextText.setText(text);
    }

    @Override
    public void prev()
    {
        currentIndex--;
        fm.popBackStack();
        onPageChange();
    }

    @Override
    public void setNavigationClickable(boolean isClickable)
    {
        prev.setClickable(isClickable);
        next.setClickable(isClickable);
    }

    @Override
    public void addDataObject(String key, Object dataObject)
    {
        dataObjects.put(key, dataObject);
    }

    @Override
    public Object getDataObject(String key)
    {
        if(dataObjects.containsKey(key))
            return dataObjects.get(key);
        return null;
    }
}



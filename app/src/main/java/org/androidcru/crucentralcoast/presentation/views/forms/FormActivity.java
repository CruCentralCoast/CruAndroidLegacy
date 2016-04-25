package org.androidcru.crucentralcoast.presentation.views.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Observers;
import rx.schedulers.Schedulers;

public class FormActivity extends AppCompatActivity implements FormHolder
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

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ButterKnife.bind(this);
        formState = FormState.PROGRESS;

        fm = getSupportFragmentManager();
        setSupportActionBar(toolbar);
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
        Observable<Long> o500 = Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        prev.setOnClickListener(v -> {
            if (currentFormContent != null)
            {
                setNavigationClickable(false);
                currentFormContent.onPrevious();
                o500.subscribe(Observers.create(vo -> setNavigationClickable(true)));
            }
        });

        next.setOnClickListener(v -> {
            if (currentFormContent != null)
            {
                setNavigationClickable(false);
                currentFormContent.onNext();
                o500.subscribe(Observers.create(vo -> setNavigationClickable(true)));
            }
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
        fm.executePendingTransactions();
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
        currentIndex++;
        FormContentFragment nextFragment = currentIndex < fragments.size() ? fragments.get(currentIndex) : null;
        onPageChange();
        if(nextFragment != null)
        {
            performTransaction(nextFragment);
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
        currentIndex--;
        onPageChange();
        fm.popBackStack();
        fm.executePendingTransactions();
    }

    @Override
    public void setNavigationClickable(boolean isClickable)
    {
        if(!isClickable)
        {
            prev.setOnClickListener(null);
            next.setOnClickListener(null);
        }
        else
        {
            setupButtonListeners();
        }

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



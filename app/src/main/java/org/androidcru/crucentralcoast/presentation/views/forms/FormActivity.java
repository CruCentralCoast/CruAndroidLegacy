package org.androidcru.crucentralcoast.presentation.views.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FormActivity extends AppCompatActivity implements FormHolder
{
    private FormContent currentFormContent;
    private HashMap<String, Object> dataObjects = new HashMap<>();
    private FormContentFragment previousFragment;
    private ArrayList<WeakReference<FormContentFragment>> fragments = new ArrayList<>();
    private int currentIndex = 0;

    private FragmentManager fm;
    private Animation.AnimationListener animationListener = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            if (previousFragment != null) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().hide(previousFragment).commit();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

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
    }

    @Override
    public void setFormContent(List<FormContentFragment> fragments)
    {
        if(fragments != null && !fragments.isEmpty())
        {
            setupButtonListeners();
            this.fragments = new ArrayList<>();
            for(FormContentFragment fragment : fragments)
            {
                //fragment.setAnimationListener(animationListener);
                this.fragments.add(new WeakReference<FormContentFragment>(fragment));
            }
            currentFormContent = fragments.get(0);
            performTransaction(fragments.get(0));
            previousFragment = null;
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
        previousFragment = fragments.get(currentIndex).get();
        currentIndex++;
        FormContentFragment nextFragment = fragments.get(currentIndex).get();
        if(nextFragment != null)
        {
            performTransaction(nextFragment);
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
        previousFragment = fragments.get(currentIndex).get();
        currentIndex--;
        fm.popBackStack();
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



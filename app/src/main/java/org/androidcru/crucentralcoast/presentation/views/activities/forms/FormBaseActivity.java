package org.androidcru.crucentralcoast.presentation.views.activities.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;

public class FormBaseActivity extends AppCompatActivity implements FormHolder
{
    ImageButton closeButton;
    TextView titleView;

    Button previous;
    Button next;

    private FormPage currentFormPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        setupUI();
        setupButtons();
    }

    private void setupUI()
    {
        closeButton = (ImageButton) findViewById(R.id.close);
        titleView = (TextView) findViewById(R.id.title);

        previous = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
    }

    private void setupButtons()
    {
        closeButton.setImageDrawable(DrawableUtil.getTintedDrawable(this, R.drawable.ic_close_grey600_48dp, android.R.color.white));
        previous.setCompoundDrawablesWithIntrinsicBounds(DrawableUtil.getTintedDrawable(this, R.drawable.ic_chevron_left_grey600_48dp, android.R.color.white), null, null, null);
        next.setCompoundDrawablesWithIntrinsicBounds(null, null, DrawableUtil.getTintedDrawable(this, R.drawable.ic_chevron_right_grey600_48dp, android.R.color.white), null);
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
        previous.setOnClickListener(v -> {
            if(currentFormPage != null)
                currentFormPage.onPrevious();
        });

        next.setOnClickListener(v -> {
            if (currentFormPage != null)
                currentFormPage.onNext();
        });
    }

    @Override
    public void setTitle(String title)
    {
        titleView.setText(title);
    }

    @Override
    public void setPreviousVisbility(int visibility)
    {
        previous.setVisibility(visibility);
    }

    @Override
    public void setNextVisibility(int visibility)
    {
        next.setVisibility(visibility);
    }
}



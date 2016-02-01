package org.androidcru.crucentralcoast.presentation.views.activities.forms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FormActivity extends AppCompatActivity implements FormHolder
{
    ImageButton closeButton;
    TextView titleView;

    ImageButton previous;
    ImageButton next;

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

        previous = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);
    }

    private void setupButtons()
    {
        closeButton.setImageDrawable(DrawableUtil.getTintedDrawable(this, R.drawable.ic_close_grey600_48dp, android.R.color.white));
        previous.setImageDrawable(DrawableUtil.getTintedDrawable(this, R.drawable.ic_chevron_left_grey600_48dp, android.R.color.white));
        next.setImageDrawable(DrawableUtil.getTintedDrawable(this, R.drawable.ic_chevron_right_grey600_48dp, android.R.color.white));
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
}



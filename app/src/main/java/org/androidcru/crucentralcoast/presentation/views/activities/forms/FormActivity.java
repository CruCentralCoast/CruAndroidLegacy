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

public class FormActivity extends AppCompatActivity
{
    @Bind(R.id.close) ImageButton closeButton;
    @Bind(R.id.title) TextView title;

    @Bind(R.id.prev) ImageButton previous;
    @Bind(R.id.next) ImageButton next;

    private FormPage currentFormPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);

        setupButtons();
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
            setupTitle();
        }
    }

    private void setupButtonListeners()
    {
        previous.setOnClickListener(v -> {
            if(currentFormPage != null)
                currentFormPage.onPrevious();
        });

        next.setOnClickListener(v -> {
            if(currentFormPage != null)
                currentFormPage.onNext();
        });
    }

    private void setupTitle()
    {
        title.setText(currentFormPage.getTitle());
    }
}



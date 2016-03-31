package org.androidcru.crucentralcoast.presentation.views.dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.providers.FacebookProvider;

import rx.android.schedulers.AndroidSchedulers;

public class RsvpDialog extends AlertDialog
{
    private RadioGroup mRadioGroup;
    private ProgressBar mProgressBar;
    private RSVP_STATUS mSelection = RSVP_STATUS.NO_REPLY;
    private String mEventURL;

    public RsvpDialog(Context context, String eventUrl)
    {
        super(context);
        inflateView();

        setupTitle();
        setupButtons();

        this.mEventURL = eventUrl;

        FacebookProvider.getInstance().getEventStatus(mEventURL)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rsvpStatus -> {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRadioGroup.setVisibility(View.VISIBLE);
                    mSelection = rsvpStatus;
                    setupRadioGroup();

                });
    }

    private void setupRadioGroup()
    {
        ((RadioButton) mRadioGroup.getChildAt(mSelection.ordinal())).setChecked(true);
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            mSelection = RSVP_STATUS.values()[mRadioGroup.indexOfChild(findViewById(checkedId))];
            Logger.d(mSelection.toString() + " selected.");
        });
    }

    private void inflateView()
    {
        View v = View.inflate(getContext(), R.layout.dialog_rsvp, null);
        super.setView(v);
        mRadioGroup = (RadioGroup) v.findViewById(R.id.rsvp_group);
        mProgressBar = (ProgressBar) v.findViewById(R.id.rsvp_progress);
    }

    private void setupButtons()
    {
        //REVIEW magic strings
        super.setButton(BUTTON_NEGATIVE, "JUST OPEN IN FACEBOOK", (dialog, which) -> {
            Logger.d("Should have opened in Facebook");
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mEventURL)));
        });
        super.setButton(BUTTON_POSITIVE, "OKAY", (dialog, which) -> {
            Logger.d("Okay clicked");
            FacebookProvider.getInstance().setRSVPStatus(mEventURL, mSelection)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        });
    }

    private void toggleButtons(boolean clickable)
    {
        super.getButton(BUTTON_POSITIVE).setClickable(clickable);
        super.getButton(BUTTON_NEGATIVE).setClickable(clickable);
    }

    private void setupTitle()
    {
        super.setTitle(R.string.rsvp_title);
    }

    public enum RSVP_STATUS
    {
        NO_REPLY, ATTENDING, INTERESTED
    }
}

package com.crucentralcoast.app.presentation.views.prayers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerRequest;
import com.crucentralcoast.app.data.providers.PrayerProvider;
import com.crucentralcoast.app.data.providers.observer.ObserverUtil;
import com.crucentralcoast.app.presentation.util.DrawableUtil;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rx.Observer;
import rx.observers.Observers;
import timber.log.Timber;

/**
 * Created by brittanyberlanga on 5/13/17.
 */

public class SubmitPrayerRequestActivity extends BaseAppCompatActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.prayer_field)
    EditText prayerText;
    @BindView(R.id.visible_to_leaders_only)
    RadioButton visibleToLeadersOnlyButton;
    @BindView(R.id.personal_contact_layout)
    LinearLayout personalContactLayout;
    @BindView(R.id.contact_phone_layout)
    LinearLayout contactPhoneLayout;
    @BindView(R.id.contact_email_layout)
    LinearLayout contactEmailLayout;
    @BindView(R.id.personal_contact_checkbox)
    CheckBox personalContactCheckbox;

    private Observer<PrayerRequest> prayerRequestSubscriber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_request_form);
        ButterKnife.bind(this);
        setupFab();

        prayerRequestSubscriber = ObserverUtil.create(Observers.create(t -> {
                },
                (e) -> {
                    Timber.e(e, "Failed to retrieve.");
                    if (getCurrentFocus() != null) {
                        Snackbar.make(getCurrentFocus(), R.string.prayer_request_error,
                                Snackbar.LENGTH_SHORT).show();
                    }
                },
                SubmitPrayerRequestActivity.this::finish),
                () -> {
                },
                () -> {
                    // no network
                    if (getCurrentFocus() != null) {
                        Snackbar.make(getCurrentFocus(), R.string.no_network_connection,
                                Snackbar.LENGTH_SHORT).show();
                    }
                }, () -> {
                    // network error
                    if (getCurrentFocus() != null) {
                        Snackbar.make(getCurrentFocus(), R.string.network_error,
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupFab() {
        fab.setImageDrawable(DrawableUtil.getTintedDrawable(this, R.drawable.ic_check_grey600,
                android.R.color.white));
        fab.setOnClickListener(v ->
                createPrayerRequest()
        );
    }

    @OnClick({R.id.visible_to_public, R.id.visible_to_leaders_only})
    public void onCheckedVisibility(View view) {
        personalContactLayout.setVisibility(visibleToLeadersOnlyButton.isChecked() ? View.VISIBLE :
                View.GONE);
        personalContactCheckbox.setChecked(false);
    }

    @OnCheckedChanged(R.id.personal_contact_checkbox)
    public void onCheckPersonalContactButton(CompoundButton compoundButton, boolean checked) {
        contactPhoneLayout.setVisibility(checked ? View.VISIBLE : View.GONE);
        contactEmailLayout.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    private void createPrayerRequest() {
        String prayer = prayerText.getText().toString().trim();
        if (!TextUtils.isEmpty(prayer)) {
            PrayerRequest prayerRequest =
                    new PrayerRequest(SharedPreferencesUtil.getFCMID(),
                            visibleToLeadersOnlyButton.isChecked(), prayer);
            PrayerProvider.createPrayerRequest(prayerRequestSubscriber, prayerRequest);
        }
    }
}

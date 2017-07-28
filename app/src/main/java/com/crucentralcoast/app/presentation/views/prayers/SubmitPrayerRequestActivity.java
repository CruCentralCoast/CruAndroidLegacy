package com.crucentralcoast.app.presentation.views.prayers;

import android.content.Context;
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
import android.widget.Spinner;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerRequest;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.providers.PrayerProvider;
import com.crucentralcoast.app.data.providers.observer.ObserverUtil;
import com.crucentralcoast.app.presentation.util.DrawableUtil;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.validator.BaseValidator;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import commons.validator.routines.EmailValidator;
import commons.validator.routines.RegexValidator;
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
    @NotEmpty
    EditText prayerText;
    @BindView(R.id.visible_to_leaders_only)
    RadioButton visibleToLeadersOnlyButton;
    @BindView(R.id.gender_pref_layout)
    LinearLayout genderPreferenceLayout;
    @BindView(R.id.gender_pref_field)
    Spinner genderPreferenceSpinner;
    @BindView(R.id.personal_contact_layout)
    LinearLayout personalContactLayout;
    @BindView(R.id.contact_phone_layout)
    LinearLayout contactPhoneLayout;
    @BindView(R.id.contact_phone_field)
    EditText contactPhoneText;
    @BindView(R.id.contact_email_layout)
    LinearLayout contactEmailLayout;
    @BindView(R.id.contact_email_field)
    EditText contactEmailText;
    @BindView(R.id.personal_contact_checkbox)
    CheckBox personalContactCheckbox;

    private Observer<PrayerRequest> prayerRequestSubscriber;
    private BaseValidator validator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_request_form);
        ButterKnife.bind(this);
        setupFab();

        initValidator();
        enableHomeBackArrow();
        ViewUtil.setSpinner(genderPreferenceSpinner, genderPreferencesForSpinner(), null, 0);

        prayerRequestSubscriber = ObserverUtil.create(Observers.create(t -> {
                },
                (e) -> {
                    Timber.e(e, "Failed to retrieve.");
                    e.printStackTrace();
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
        fab.setOnClickListener(v -> {
            if (validator.validate()) {
                createPrayerRequest();
            }
        });
    }

    private void initValidator() {
        validator = new BaseValidator(this);
        validator.put(prayerText, new QuickRule<EditText>() {
            @Override
            public boolean isValid(EditText view) {
                return !TextUtils.isEmpty(view.getText().toString().trim());
            }

            @Override
            public String getMessage(Context context) {
                return getContext().getString(R.string.empty_field_message);
            }
        });
        validator.put(contactPhoneText, new QuickRule<EditText>() {
            boolean validContact;

            @Override
            public boolean isValid(EditText view) {
                validContact = validContact();
                String value = view.getText().toString().trim();
                return validContact && (TextUtils.isEmpty(value) ||
                        new RegexValidator(AppConstants.PHONE_REGEX).isValid(value));
            }

            @Override
            public String getMessage(Context context) {
                return validContact ? getContext().getString(R.string.phone_number_error) :
                        getContext().getString(R.string.invalid_contact_message);
            }
        });
        validator.put(contactEmailText, new QuickRule<EditText>() {
            boolean validContact;

            @Override
            public boolean isValid(EditText view) {
                validContact = validContact();
                String value = view.getText().toString().trim();
                return validContact && (TextUtils.isEmpty(value) || EmailValidator.getInstance()
                        .isValid(value));
            }

            @Override
            public String getMessage(Context context) {
                return validContact ? getContext().getString(R.string.invalid_email_message) :
                        getContext().getString(R.string.invalid_contact_message);
            }
        });
    }

    private boolean validContact() {
        return !personalContactCheckbox.isChecked() ||
                (!TextUtils.isEmpty(contactEmailText.getText().toString().trim()) ||
                        !TextUtils.isEmpty(contactPhoneText.getText().toString().trim()));
    }

    @OnClick({R.id.visible_to_public, R.id.visible_to_leaders_only})
    public void onCheckedVisibility(View view) {
        personalContactLayout.setVisibility(visibleToLeadersOnlyButton.isChecked() ? View.VISIBLE :
                View.GONE);
        genderPreferenceLayout.setVisibility(visibleToLeadersOnlyButton.isChecked() ? View.VISIBLE :
                View.GONE);
        personalContactCheckbox.setChecked(false);
    }

    @OnCheckedChanged(R.id.personal_contact_checkbox)
    public void onCheckPersonalContactButton(CompoundButton compoundButton, boolean checked) {
        contactPhoneLayout.setVisibility(checked ? View.VISIBLE : View.GONE);
        contactEmailLayout.setVisibility(checked ? View.VISIBLE : View.GONE);
    }

    private void createPrayerRequest() {
        PrayerRequest prayerRequest;
        if (visibleToLeadersOnlyButton.isChecked()) {
            prayerRequest = new PrayerRequest(SharedPreferencesUtil.getFCMID(), true,
                    prayerText.getText().toString(), Ride.Gender.getFromColloquial((String)
                    genderPreferenceSpinner.getSelectedItem()), personalContactCheckbox.isChecked(),
                    contactPhoneText.getText().toString().trim(), contactEmailText.getText()
                    .toString().trim());
        } else {
            prayerRequest = new PrayerRequest(SharedPreferencesUtil.getFCMID(), false,
                    prayerText.getText().toString());
        }

        PrayerProvider.createPrayerRequest(prayerRequestSubscriber, prayerRequest);
    }

    protected String[] genderPreferencesForSpinner() {
        ArrayList<String> genders = new ArrayList<>(Ride.Gender.getColloquials());
        // remove not applicable
        genders.remove(3);
        // remove not known
        genders.remove(0);
        // add no gender preference
        genders.add(0, getContext().getString(R.string.no_preference));
        return genders.toArray(new String[genders.size()]);
    }
}

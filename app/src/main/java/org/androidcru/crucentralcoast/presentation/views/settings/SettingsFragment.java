package org.androidcru.crucentralcoast.presentation.views.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.LoginResponse;
import org.androidcru.crucentralcoast.data.providers.LoginProvider;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;

import rx.Observer;
import rx.Subscription;
import rx.observers.Observers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by main on 5/4/2016.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SubscriptionsHolder {

    private CompositeSubscription subscription = new CompositeSubscription();

    private SharedPreferences sharedPreferences;

    private Preference subscriptionButton;
    private Preference loginButton;
    private Preference notificationCheckbox;
    private Preference cruGoldIsTheBest;

    //login
    private AlertDialog alertDialog;
    private View alertDialogView;
    @NotEmpty EditText loginName;
    @NotEmpty EditText loginPassword;
    private BaseValidator validator;
    private ProgressBar progress;
    private TextView loginFailed;

    private Observer<LoginResponse> observer;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        sharedPreferences = CruApplication.getSharedPreferences();
        addPreferencesFromResource(R.xml.fragment_settings);

        //subscription button listener
        subscriptionButton = (Preference) getPreferenceManager().findPreference(getString(R.string.subscription_button_key));
        subscriptionButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                getActivity().startActivity(new Intent(SettingsFragment.this.getContext(), SubscriptionActivity.class));
                return true;
            }
        });

        //login button
        LayoutInflater factory = LayoutInflater.from(getContext());
        alertDialogView = factory.inflate(R.layout.login_alert, null);
        loginName = (EditText)alertDialogView.findViewById(R.id.username_field);
        loginPassword = (EditText)alertDialogView.findViewById(R.id.password_field);
        progress = (ProgressBar) alertDialogView.findViewById(R.id.progress);
        loginFailed = (TextView) alertDialogView.findViewById(R.id.login_failed);
        validator = new BaseValidator(this);

        observer = Observers.create(response -> {
            if(response.success)
            {
                loginName.setText("");
                loginName.setError("");

                loginPassword.setText("");
                loginPassword.setError("");

                loginFailed.setVisibility(View.GONE);
                sharedPreferences.edit().putString(AppConstants.LOGIN_KEY, response.leaderAPIKey).commit();
                Timber.d("LOGIN SUCCEEDED");
                alertDialog.dismiss();
            }
            else
                loginFailed.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        });

        alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Community Group Leader Login")
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    alertDialog.dismiss();
                })
                .create();

        alertDialog.setOnShowListener(dialog -> {
            Button posButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            posButton.setOnClickListener(view -> {
                if(validator.validate())
                {
                    //login api call
                    String name = loginName.getText().toString();
                    String password = loginPassword.getText().toString();
                    progress.setVisibility(View.VISIBLE);
                    LoginProvider.login(SettingsFragment.this, observer, name, password, CruApplication.getGCMID());

                    Timber.d("Logging in as " + name + " and pw: " + password);
                }
            });
        });

        alertDialog.setView(alertDialogView);

        loginButton = (Preference) getPreferenceManager().findPreference(getString(R.string.login_button_key));
        loginButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                alertDialog.show();
                return true;
            }
        });

        //notification
        notificationCheckbox = (Preference) getPreferenceManager().findPreference(getString(R.string.notification_checkbox_key));
        notificationCheckbox.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {


                return true;
            }
        });

        //important functionality
        cruGoldIsTheBest = (Preference) getPreferenceManager().findPreference(getString(R.string.Cru_Gold_is_the_best));
        cruGoldIsTheBest.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean newValue = o.toString().equals("true");
                System.out.println("object value is " + o + " " + o.toString());
                SharedPreferences.Editor e = sharedPreferences.edit();
                e.putString("CruGoldTheme", newValue ? "YES" : "NO");
                e.commit();
                getActivity().recreate();
                return true;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void addSubscription(Subscription s)
    {
        subscription.add(s);
    }

    @Override
    public void clearSubscriptions()
    {
        subscription.clear();
    }
}

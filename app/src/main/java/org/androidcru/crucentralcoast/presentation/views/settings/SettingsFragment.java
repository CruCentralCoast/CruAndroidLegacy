package org.androidcru.crucentralcoast.presentation.views.settings;

import android.content.Intent;
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

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.LoginResponse;
import org.androidcru.crucentralcoast.data.providers.LoginProvider;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;
import org.androidcru.crucentralcoast.util.SharedPreferencesUtil;

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

    private Preference subscriptionButton;
    private Preference loginButton;
    private Preference notificationCheckbox;

    //login
    private AlertDialog loginDialog;
    private View loginView;
    @NotEmpty @Email(messageResId = R.string.invalid_email)EditText loginName;
    @NotEmpty EditText loginPassword;
    private BaseValidator loginValidator;
    private ProgressBar loginProgress;
    private TextView loginFailed;

    //logout
    private AlertDialog logoutDialog;

    private Observer<LoginResponse> loginObserver;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
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
        loginView = factory.inflate(R.layout.alert_login, null);
        loginName = (EditText) loginView.findViewById(R.id.username_field);
        loginPassword = (EditText) loginView.findViewById(R.id.password_field);
        loginProgress = (ProgressBar) loginView.findViewById(R.id.progress);
        loginFailed = (TextView) loginView.findViewById(R.id.login_failed);
        loginValidator = new BaseValidator(this);

        loginObserver = Observers.create(response -> {
            if(response.success)
            {
                SharedPreferencesUtil.writeLoginInformation(loginName.getText().toString(), response.leaderAPIKey);
                loginName.setText("");
                loginName.setError("");

                loginPassword.setText("");
                loginPassword.setError("");

                loginFailed.setVisibility(View.GONE);
                Timber.d("LOGIN SUCCEEDED");
                setLoginButtonTitle();
                loginDialog.dismiss();
            }
            else {
                loginFailed.setVisibility(View.VISIBLE);
            }
            loginProgress.setVisibility(View.GONE);
        });

        loginDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.login_button_title)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    loginDialog.dismiss();
                })
                .create();

        loginDialog.setOnShowListener(dialog -> {
            Button posButton = loginDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            posButton.setOnClickListener(view -> {
                if(loginValidator.validate())
                {
                    //login api call
                    String name = loginName.getText().toString();
                    String password = loginPassword.getText().toString();
                    loginProgress.setVisibility(View.VISIBLE);
                    LoginProvider.login(SettingsFragment.this, loginObserver, name, password, SharedPreferencesUtil.getGCMID());

                    Timber.d("Logging in as " + name + " and pw: " + password);
                }
            });
        });

        loginDialog.setView(loginView);

        loginButton = (Preference) getPreferenceManager().findPreference(getString(R.string.login_button_key));
        setLoginButtonTitle();
        loginButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (SharedPreferencesUtil.containsKey(AppConstants.LOGIN_KEY))
                    logoutDialog.show();
                else
                    loginDialog.show();
                return true;
            }
        });

        //logout button
        logoutDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_dialog_text)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    SharedPreferencesUtil.removeKey(AppConstants.USERNAME_KEY);
                    SharedPreferencesUtil.removeKey(AppConstants.LOGIN_KEY);
                    Timber.d("LOGOUT SUCCEEDED");
                    setLoginButtonTitle();
                    logoutDialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    logoutDialog.dismiss();
                })
                .create();

        //notification
        notificationCheckbox = (Preference) getPreferenceManager().findPreference(getString(R.string.notification_checkbox_key));
        notificationCheckbox.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean newVal = o.toString().equals("true");
                SharedPreferencesUtil.setNotificationEnable(newVal);
//                Timber.d("toggled to " + SharedPreferencesUtil.getNotificationEnabled());
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

    private void setLoginButtonTitle() {
        String str;
        if (SharedPreferencesUtil.containsKey(AppConstants.LOGIN_KEY))
            str = getString(R.string.logged_in) + " " + SharedPreferencesUtil.getLoginUsername();
        else
            str = getString(R.string.login);
        loginButton.setTitle(str);
    }
}

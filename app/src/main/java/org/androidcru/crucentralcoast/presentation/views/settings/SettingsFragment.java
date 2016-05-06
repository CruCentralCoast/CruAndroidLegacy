package org.androidcru.crucentralcoast.presentation.views.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.AlertDialogCreator;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionActivity;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by main on 5/4/2016.
 */
public class SettingsFragment extends PreferenceFragmentCompat { //extends BaseSupportFragment {

    private SharedPreferences sharedPreferences;

    private Preference subscriptionButton;
    private Preference loginButton;
    private Preference notificationCheckbox;
    private Preference cruGoldIsTheBest;

    //login
    private AlertDialogCreator alertDialog;
    private View alertDialogView;
    private EditText loginName;
    private EditText loginPassword;

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
        alertDialog = new AlertDialogCreator(getContext(), "Community Group Leader Login", "",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //login api call
                        String name = loginName.getText().toString();
                        String password = loginPassword.getText().toString();

                        Timber.d("Logging in as " + name + " and pw: " + password); //TODO: remove this
                    }
                },
                CruApplication.getContext().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.hide();
                    }
                },
                CruApplication.getContext().getString(R.string.cancel));
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
}

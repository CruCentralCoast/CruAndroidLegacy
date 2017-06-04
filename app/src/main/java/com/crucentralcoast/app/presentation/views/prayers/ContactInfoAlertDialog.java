package com.crucentralcoast.app.presentation.views.prayers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerRequest;
import com.crucentralcoast.app.data.providers.PrayerProvider;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * Created by brittanyberlanga on 6/3/17.
 */

public class ContactInfoAlertDialog extends DialogFragment {

    private static final String PRAYER_REQUEST_ARG = "prayer_request_arg";

    @BindView(R.id.phone_label)
    protected TextView phoneLabel;
    @BindView(R.id.phone_field)
    protected TextView phoneField;
    @BindView(R.id.email_label)
    protected TextView emailLabel;
    @BindView(R.id.email_field)
    protected TextView emailField;
    @BindView(R.id.contacted)
    protected CheckBox contacted;

    private PrayerRequest prayerRequest;
    private Observer<PrayerRequest> prayerRequestSubscriber;

    public static ContactInfoAlertDialog newInstance(PrayerRequest prayerRequest) {
        ContactInfoAlertDialog dialog = new ContactInfoAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PRAYER_REQUEST_ARG, Parcels.wrap(prayerRequest));
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            updatePrayerRequest();
        });
        builder.setTitle(R.string.author_contact_info);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_contact_info, null);
        builder.setView(view);

        prayerRequest = Parcels.unwrap(getArguments().getParcelable(PRAYER_REQUEST_ARG));
        ButterKnife.bind(this, view);
        bind();

        return builder.create();
    }

    private void bind() {
        if (TextUtils.isEmpty(prayerRequest.contactPhone)) {
            phoneLabel.setVisibility(View.GONE);
            phoneField.setVisibility(View.GONE);
        } else {
            phoneField.setText(prayerRequest.contactPhone);
        }
        if (TextUtils.isEmpty(prayerRequest.contactEmail)) {
            emailLabel.setVisibility(View.GONE);
            emailField.setVisibility(View.GONE);
        } else {
            emailField.setText(prayerRequest.contactEmail);
        }
        contacted.setChecked(prayerRequest.contacted);
    }

    private void updatePrayerRequest() {
        if (prayerRequestSubscriber != null && contacted.isChecked() != prayerRequest.contacted) {
            PrayerProvider.setPrayerRequestContacted(prayerRequestSubscriber, prayerRequest.id,
                    SharedPreferencesUtil.getLeaderAPIKey(), contacted.isChecked());
        }
    }

    public void setPrayerRequestSubscriber(Observer<PrayerRequest> prayerRequestSubscriber) {
        this.prayerRequestSubscriber = prayerRequestSubscriber;
    }
}

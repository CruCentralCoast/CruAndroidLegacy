package com.crucentralcoast.app.presentation.views.prayers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerRequest;
import com.crucentralcoast.app.data.models.PrayerResponse;
import com.crucentralcoast.app.data.providers.PrayerProvider;
import com.crucentralcoast.app.data.providers.observer.ObserverUtil;
import com.crucentralcoast.app.presentation.views.base.BaseAppCompatActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import org.parceler.Parcels;

import java.text.DateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.observers.Observers;
import timber.log.Timber;

/**
 * Created by brittanyberlanga on 5/22/17.
 */

public class PrayerRequestDetailsActivity extends BaseAppCompatActivity {

    public static final String PRAYER_REQ_EXTRA = "prayer_request_extra";

    @BindView(R.id.prayer_text)
    protected TextView prayerText;
    @BindView(R.id.creation_date)
    protected TextView creationDate;
    @BindView(R.id.leaders_info)
    protected TextView leadersInfo;
    @BindView(R.id.alert_info)
    protected TextView alertInfo;
    @BindView(R.id.response)
    protected EditText response;

    private PrayerRequest prayerRequest;
    private PrayerResponseListFragment prayerResponseListFragment;
    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
            DateFormat.SHORT);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_request_details);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        prayerRequest = Parcels.unwrap(getIntent().getParcelableExtra(PRAYER_REQ_EXTRA));
        bind();

        prayerResponseListFragment = PrayerResponseListFragment.newInstance(prayerRequest);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, prayerResponseListFragment);
        transaction.commit();
    }

    private void bind() {
        prayerText.setText(prayerRequest.prayer);
        creationDate.setText(dateFormat.format(prayerRequest.creationDate));
        leadersInfo.setVisibility(prayerRequest.leadersOnly ? View.VISIBLE : View.GONE);

        // show alert icons if leaders only and author would like to be contacted
        if (prayerRequest.leadersOnly && prayerRequest.contact) {
            alertInfo.setVisibility(View.VISIBLE);
            if (prayerRequest.contactLeader == null || !prayerRequest.contacted) {
                if (prayerRequest.contactLeader == null) {
                    alertInfo.setText(R.string.no_leader);
                    alertInfo.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources
                            .getDrawable(this, R.drawable.ic_alert_circle), null, null, null);
                } else {
                    alertInfo.setText(R.string.not_contacted);
                    alertInfo.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources
                                    .getDrawable(this,
                                            R.drawable.ic_report_problem_yellow_500_18dp),
                            null, null, null);
                }
            } else {
                alertInfo.setCompoundDrawablesWithIntrinsicBounds(AppCompatResources
                        .getDrawable(this, R.drawable.ic_check_all), null, null, null);
                alertInfo.setText(R.string.contacted);
            }
        } else {
            alertInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @OnClick(R.id.alert_info)
    public void onClickAlert() {
        if (prayerRequest.contactLeader == null) {
            new AlertDialog.Builder(this).setTitle(R.string.contact_sign_up)
                    .setMessage(R.string.contact_sign_up_details).setPositiveButton(R.string.ok,
                    (DialogInterface dialog, int which) -> {
                        Observer<PrayerRequest> prayerRequestSubscriber =
                                ObserverUtil.create(Observers.create(t -> {
                                            prayerRequest = t;
                                            bind();
                                        },
                                        (e) -> {
                                            Timber.e(e, "Failed to post prayer response.");
                                            if (getCurrentFocus() != null) {
                                                Snackbar.make(getCurrentFocus(),
                                                        R.string.contact_leader_error,
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                        }, () -> {
                                        }),
                                        () -> {
                                        },
                                        () -> {
                                            // no network
                                            if (getCurrentFocus() != null) {
                                                Snackbar.make(getCurrentFocus(),
                                                        R.string.no_network_connection,
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                        }, () -> {
                                            // network error
                                            if (getCurrentFocus() != null) {
                                                Snackbar.make(getCurrentFocus(),
                                                        R.string.network_error,
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                        PrayerProvider.setPrayerRequestContactLeader(prayerRequestSubscriber,
                                prayerRequest.id, SharedPreferencesUtil.getLeaderAPIKey(),
                                SharedPreferencesUtil.getUserId());
                    })
                    .setNegativeButton(R.string.cancel, null).create().show();
        }
    }

    @OnClick(R.id.respond)
    public void onClickRespond() {
        String responseText = response.getText().toString().trim();
        if (!TextUtils.isEmpty(responseText)) {
            Observer<PrayerResponse> prayerResponseSubscriber =
                    ObserverUtil.create(Observers.create(t -> {
                            },
                            (e) -> {
                                Timber.e(e, "Failed to post prayer response.");
                                if (getCurrentFocus() != null) {
                                    Snackbar.make(getCurrentFocus(), R.string.prayer_response_error,
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            }, () -> {
                                response.setText("");
                                response.clearFocus();
                                prayerResponseListFragment.onResume();
                            }),
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
            PrayerProvider.createPrayerResponse(prayerResponseSubscriber,
                    new PrayerResponse(SharedPreferencesUtil.getFCMID(), responseText,
                            prayerRequest.id), SharedPreferencesUtil.getLeaderAPIKey());
        }
    }
}

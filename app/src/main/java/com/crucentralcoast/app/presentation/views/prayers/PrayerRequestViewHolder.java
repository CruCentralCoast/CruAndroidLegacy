package com.crucentralcoast.app.presentation.views.prayers;

import android.content.Intent;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerRequest;

import org.parceler.Parcels;

import java.text.DateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by brittanyberlanga on 5/6/17.
 */

public class PrayerRequestViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.creation_date)
    protected TextView creationDate;
    @BindView(R.id.prayer_text)
    protected TextView prayerText;
    @BindView(R.id.responses_text)
    protected TextView responsesText;
    @BindView(R.id.leaders_icon)
    protected ImageView leadersIcon;
    @BindView(R.id.alert_icon)
    protected ImageView alertIcon;


    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
            DateFormat.SHORT);
    private PrayerRequest prayerRequest;

    public PrayerRequestViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(PrayerRequest prayerRequest) {
        this.prayerRequest = prayerRequest;
        creationDate.setText(dateFormat.format(prayerRequest.creationDate));
        prayerText.setText(prayerRequest.prayer);
        responsesText.setText(String.format("responses (%d)", prayerRequest.prayerResponseCount));
        leadersIcon.setVisibility(prayerRequest.leadersOnly ? View.VISIBLE : View.GONE);
        // show alert icons if leaders only and author would like to be contacted
        if (prayerRequest.leadersOnly && prayerRequest.contact) {
            alertIcon.setVisibility(View.VISIBLE);
            if (prayerRequest.contactLeader == null) {
                alertIcon.setImageDrawable(AppCompatResources.getDrawable(alertIcon.getContext(),
                        R.drawable.ic_alert_circle));
            } else if (!prayerRequest.contacted) {
                alertIcon.setImageDrawable(AppCompatResources.getDrawable(alertIcon.getContext(),
                        R.drawable.ic_report_problem_yellow_500_18dp));
            } else {
                alertIcon.setImageDrawable(AppCompatResources.getDrawable(alertIcon.getContext(),
                        R.drawable.ic_check_all));
            }
        } else {
            alertIcon.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.item_view)
    public void onClickPrayerRequest(View view) {
        Intent detailsIntent = new Intent(view.getContext(), PrayerRequestDetailsActivity.class);
        detailsIntent.putExtra(PrayerRequestDetailsActivity.PRAYER_REQ_EXTRA,
                Parcels.wrap(prayerRequest));
        view.getContext().startActivity(detailsIntent);
    }
}

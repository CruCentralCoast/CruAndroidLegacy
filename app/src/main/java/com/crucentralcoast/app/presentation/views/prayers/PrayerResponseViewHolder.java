package com.crucentralcoast.app.presentation.views.prayers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.PrayerResponse;

import java.text.DateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brittanyberlanga on 5/22/17.
 */

public class PrayerResponseViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.creation_date)
    protected TextView creationDate;
    @BindView(R.id.response_text)
    protected TextView responseText;

    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
            DateFormat.SHORT);

    public PrayerResponseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(PrayerResponse prayerResponse) {
        creationDate.setText(dateFormat.format(prayerResponse.creationDate));
        responseText.setText(prayerResponse.response);
    }
}

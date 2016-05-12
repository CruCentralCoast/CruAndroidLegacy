package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

public class MinistryQuestionsDateTimeHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cgq_datetime_text) TextView toggleDescription;
    @BindView(R.id.cgq_date_field) EditText date;
    @BindView(R.id.cgq_time_field) EditText time;

    public MinistryQuestion question;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;
    protected LocalDate setDate;
    protected LocalTime setTime;
    protected GregorianCalendar questionDate;
    protected static final int NUM_HOURS = 24;
    protected static final int NUM_MINUTES = 60;
    protected static final int INTERVAL = 15;
    protected static final int NUM_TIMES = NUM_HOURS * (NUM_MINUTES / INTERVAL);
    protected static Observable<Integer> hours = Observable.range(0, NUM_HOURS);
    protected static Observable<Integer> minutes = Observable.range(0, NUM_MINUTES).filter(m -> m % INTERVAL == 0);
    protected static Timepoint[] timepoints = hours.flatMap((h) -> minutes.map((m) -> new Timepoint(h, m)))
            .toList().toBlocking().first().toArray(new Timepoint[NUM_TIMES]);


    public MinistryQuestionsDateTimeHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        super(rootView);
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        ButterKnife.bind(this, rootView);
    }

    public void bindQuestion(MinistryQuestion question) {
        this.question = question;
        toggleDescription.setText(this.question.question);
        time.setOnKeyListener(null);
        date.setOnKeyListener(null);
        // TODO do we want to set a text hint?
    }
}
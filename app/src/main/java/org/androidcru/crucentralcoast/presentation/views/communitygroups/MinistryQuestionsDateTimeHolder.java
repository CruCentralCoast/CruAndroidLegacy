package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.app.FragmentManager;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    protected FragmentManager fm;
    protected GregorianCalendar questionDateTime;
    protected static final int NUM_HOURS = 24;
    protected static final int NUM_MINUTES = 60;
    protected static final int INTERVAL = 15;
    protected static final int NUM_TIMES = NUM_HOURS * (NUM_MINUTES / INTERVAL);
    protected static Observable<Integer> hours = Observable.range(0, NUM_HOURS);
    protected static Observable<Integer> minutes = Observable.range(0, NUM_MINUTES).filter(m -> m % INTERVAL == 0);
    protected static Timepoint[] timepoints = hours.flatMap((h) -> minutes.map((m) -> new Timepoint(h, m)))
            .toList().toBlocking().first().toArray(new Timepoint[NUM_TIMES]);


    public MinistryQuestionsDateTimeHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager, FragmentManager fm) {
        super(rootView);
        this.fm = fm;
        this.adapter = adapter;
        this.layoutManager = layoutManager;
        questionDateTime = new GregorianCalendar();

        ButterKnife.bind(this, rootView);
    }

    public void bindQuestion(MinistryQuestion question) {
        this.question = question;
        toggleDescription.setText(this.question.question);
        time.setOnKeyListener(null);
        date.setOnKeyListener(null);
//        time.setText(ride.time.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
//        date.setText(ride.time.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        // TODO do we want to set a text hint?
    }

    @OnClick(R.id.cgq_time_field)
    public void onTimeClicked(View v)
    {
        onEventTimeClicked(v, questionDateTime);
    }

    @OnClick(R.id.cgq_date_field)
    public void onDateClicked(View v)
    {
        onEventDateClicked(v, questionDateTime);
    }

    protected void onEventDateClicked(View v, GregorianCalendar gc)
    {
        DatePickerDialog dpd;
        //use Ride's start time if editing a Ride
        if (setDate == null)
            dpd = getDateDialog(gc);
        else
            dpd = getDateDialog(new GregorianCalendar(setDate.getYear(), setDate.getMonthValue() - 1, setDate.getDayOfMonth()));
        //sets the text of the DatePicker EditText
        dpd.setOnDateSetListener((view, year, monthOfYear, dayOfMonth) -> {
            setDate = LocalDate.of(year, Month.values()[monthOfYear], dayOfMonth);
            String yyyymmdd = setDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            ((EditText) v).setText(convertToddMMyyyy(yyyymmdd));
        });

        dpd.show(fm, AppConstants.SUPER_SPECIAL_STRING);
    }

    protected void onEventTimeClicked(View v, GregorianCalendar gc)
    {
        TimePickerDialog tpd;
        if (setTime == null)
            tpd = getTimeDialog(gc);
        else
            tpd = getTimeDialog(new GregorianCalendar(0, 0, 0, setTime.getHour(), setTime.getMinute()));
        //sets the text of the TimePicker EditText
        tpd.setOnTimeSetListener((view, hourOfDay, minute, second) -> {
            setTime = LocalTime.of(hourOfDay, minute, second);
            ((EditText) v).setText(getTimeString(setTime));
        });
        tpd.show(fm, AppConstants.SUPER_SPECIAL_STRING);
    }

    private DatePickerDialog getDateDialog(GregorianCalendar c)
    {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                null,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
        );
        dpd.vibrate(false);
        dpd.setMinDate(new GregorianCalendar(c.get(Calendar.YEAR) - 1, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)));
        //dpd.setMaxDate(eventEndDate);
        return dpd;
    }

    private TimePickerDialog getTimeDialog(GregorianCalendar c)
    {
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                null,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                false
        );
        tpd.vibrate(false);
        tpd.setSelectableTimes(timepoints);
        return tpd;
    }

    protected String getTimeString(LocalTime time)
    {
        return convertTo12Hour(setTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
    }

    protected String getDateString(LocalDate date)
    {
        return convertToddMMyyyy(setDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    private String convertToddMMyyyy(String s)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DATE_yyyyMMdd);
        Date d = null;

        try
        {
            d = sdf.parse(s);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        sdf.applyPattern(AppConstants.DATE_DISPLAY_PATTERN);
        return sdf.format(d);
    }

    private String convertTo12Hour(String t)
    {
        DateFormat f1 = new SimpleDateFormat(AppConstants.TIME_PARSE);
        DateFormat f2 = new SimpleDateFormat(AppConstants.TIME_FORMAT);
        Date d = null;

        try
        {
            d = f1.parse(t);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return f2.format(d);
    }
}
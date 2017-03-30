package com.crucentralcoast.app.presentation.views.events;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.data.models.Location;
import com.crucentralcoast.app.presentation.providers.CalendarProvider;
import com.crucentralcoast.app.presentation.providers.FacebookProvider;
import com.crucentralcoast.app.presentation.util.DrawableUtil;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.viewmodels.ExpandableState;
import com.crucentralcoast.app.presentation.views.MainActivity;
import com.crucentralcoast.app.presentation.views.dialogs.RsvpDialog;
import com.crucentralcoast.app.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import com.crucentralcoast.app.presentation.views.ridesharing.passengersignup.PassengerSignupActivity;
import com.crucentralcoast.app.util.SharedPreferencesUtil;
import com.facebook.login.LoginResult;

import org.parceler.Parcels;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.observers.Observers;
import timber.log.Timber;

/**
 * CruEventViewHolder is a view representation of the model for the list
 */
public class CruEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.bottom_buttons) public PercentRelativeLayout bottomButtons;
    @BindView(R.id.eventName) public TextView eventName;
    @BindView(R.id.eventDate) public TextView eventDate;
    @BindView(R.id.event_banner) public ImageView eventBanner;
    @BindView(R.id.chevView) public ImageView chevronView;
    @BindView(R.id.fbButton) public ImageButton fbButton;
    @BindView(R.id.mapButton) public ImageButton mapButton;
    @BindView(R.id.calButton) public ImageButton calButton;
    @BindView(R.id.rideSharingButton) public ImageButton rideSharingButton;
    @BindView(R.id.eventDescription) public TextView eventDescription;
    @BindView(R.id.eventAddress) public TextView eventAddress;
    @BindView(R.id.animating_layout) public LinearLayout animatingLayout;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ExpandableState<CruEvent> state;
    private CruEvent cruEvent;
    public View rootView;

    public boolean addedToCalendar;
    public long localEventId;

    public CruEventViewHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager)
    {
        super(rootView);
        this.layoutManager = layoutManager;
        this.adapter = adapter;
        this.rootView = rootView;

        ButterKnife.bind(this, rootView);
        animatingLayout.setOnClickListener(this);
        ViewUtil.debounceExpandingView(animatingLayout, this);
    }
    
    public void bind(ExpandableState<CruEvent> state)
    {
        this.state = state;
        this.cruEvent = state.model;

        addedToCalendar = SharedPreferencesUtil.containsKey(cruEvent.id);
        localEventId = SharedPreferencesUtil.getCalendarEventId(cruEvent.id);

        bindUI();
    }

    private void bindUI()
    {
        eventName.setText(cruEvent.name);
        eventDate.setText(getDateTime());
        Context context = eventBanner.getContext();

        ViewUtil.setSource(eventBanner, cruEvent.image, 0, null, null, ViewUtil.SCALE_TYPE.FIT);

        fbButton.setEnabled(cruEvent.url != null && !cruEvent.url.isEmpty());
        fbButton.setSelected(fbButton.isEnabled());
        ViewUtil.setSelected(fbButton, fbButton.isEnabled(), R.drawable.ic_facebook_box_grey600, R.color.facebook_state);
        mapButton.setImageDrawable(DrawableUtil.getTintedDrawable(context, R.drawable.ic_map_marker_grey600, R.color.red600));

        ViewUtil.setSelected(calButton,
                addedToCalendar,
                R.drawable.ic_calendar_check_grey600,
                R.drawable.ic_calendar_plus_grey600,
                R.color.cal_action);

        ViewUtil.setSelected(rideSharingButton, cruEvent.rideSharingEnabled,
                R.drawable.ic_car_grey600,
                R.color.ride_sharing_state);

        chevronView.setImageDrawable(state.isExpanded
                ? DrawableUtil.getDrawable(context, R.drawable.ic_chevron_up_grey600)
                : DrawableUtil.getDrawable(context, R.drawable.ic_chevron_down_grey600));
        eventDescription.setText(cruEvent.description);
        eventDescription.setVisibility(state.isExpanded ? View.VISIBLE : View.GONE);
        eventAddress.setText(cruEvent.location.street1 + ", " + cruEvent.location.suburb +
                ", " + cruEvent.location.state + " " + cruEvent.location.postcode);
        eventAddress.setVisibility(state.isExpanded ? View.VISIBLE : View.GONE);
    }

    /**
     * Invoked by Android if setOnClickListener() is called.
     *
     * Toggles the eventDescription Visibility if tapped, stores it in the view model so that
     * RecycledViews will work properly
     *
     * @param v View that was clicked on
     */
    @Override
    public void onClick(View v)
    {
        if(state != null)
        {
            int visibility;

            if (eventDescription.getVisibility() == View.VISIBLE)
                visibility = View.GONE;
            else
                visibility = View.VISIBLE;

            eventDescription.setVisibility(visibility);
            eventAddress.setVisibility(visibility);

            state.isExpanded = (View.VISIBLE == visibility);
            adapter.notifyItemChanged(getAdapterPosition());
            layoutManager.scrollToPosition(getAdapterPosition());
        }
    }

    public String getDateTime()
    {
        return cruEvent.startDate.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + cruEvent.startDate.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER));
    }

    @OnClick(R.id.calButton)
    public void onCalendarClick(View v)
    {
        Observer<Pair<String, Long>> onCalendarWrittenObserver = Observers.create(eventInfo -> {
            String cruEventId = eventInfo.first;
            long calendarId = eventInfo.second;
            if (eventInfo.second > 0)
            {
                SharedPreferencesUtil.writeCalendarID(cruEventId, calendarId);
            }
            
            adapter.notifyItemChanged(getAdapterPosition());
        });

        final boolean adding = !addedToCalendar;
        //REVIEW magic strings
        String operation = adding ? "Add " : "Remove ";
        String toFrom = adding ? "to" : "from";
        AlertDialog confirmDialog = new AlertDialog.Builder(v.getContext())
                .setTitle(operation + cruEvent.name + " " + toFrom + " your calendar?")
                .setNegativeButton("NOPE", (dialog, which) -> {
                })
                .setPositiveButton("SURE", (dialog, which) -> {
                    if (adding)
                        CalendarProvider.addEventToCalendar(v.getContext(), cruEvent, onCalendarWrittenObserver);
                    else
                        CalendarProvider.removeEventFromCalendar(v.getContext(), cruEvent, localEventId, onCalendarWrittenObserver);
                })
                .create();
        confirmDialog.show();
    }

    @OnClick(R.id.mapButton)
    public void onMapClick(View v)
    {
        Location loc = cruEvent.location;
        String uri = String.format("geo:0,0?q=%s", loc.getAsQuery());
        //Uri gmmIntentUri = Uri.parse(String.format("geo:0,0?q=%s"), loc.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try
        {
            v.getContext().startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            //REVIEW magic string
            Toast.makeText(v.getContext(), "Please install Google Maps to view this event's location", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.fbButton)
    public void onFacebookClick(View v)
    {
        Intent openInFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse(cruEvent.url));
        openInFacebook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        RsvpDialog rsvpDialog = new RsvpDialog(v.getContext(), cruEvent.url);

        Observer<LoginResult> loginResultObserver = Observers.create(result -> {
            Set<String> grantedPermissions = FacebookProvider.getPermissions();
            //REVIEW magic strings, AppConstants
            if(grantedPermissions.contains("rsvp_event"))
                rsvpDialog.show();
            else
                v.getContext().startActivity(openInFacebook);
        });


        AlertDialog loginDialog = new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.facebook_title)
                .setNegativeButton(R.string.facebook_no, (dialog, which) -> {
                    v.getContext().startActivity(openInFacebook);
                })
                .setPositiveButton(R.string.facebook_yes, (dialog, which) -> {
                    MainActivity.loginWithFacebook();
                    FacebookProvider.setupTokenCallback(loginResultObserver);
                })
                .setMessage(R.string.facebook_reasoning)
                .create();

        if(cruEvent.url != null)
        {
            if(FacebookProvider.isTokenValid())
            {
                if (!FacebookProvider.getPermissions().contains("rsvp_event"))
                    loginDialog.show();
                else
                    rsvpDialog.show();
            }
            else
            {
                loginDialog.show();
            }

        }
        else
            Timber.d("No Facebook URL set");
    }

    @OnClick(R.id.rideSharingButton)
    public void onRideShareSharing(View v)
    {
        AppCompatActivity activity = (AppCompatActivity) rootView.getContext();

        //TODO Passenger and Driver Activities should have a unified set of Extras
        if(cruEvent.rideSharingEnabled)
        {
            switch (cruEvent.rideStatus)
            {
                case NEITHER:
                    //REVIEW magic strings
                    AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                            .setTitle("Ride Sharing")
                            .setMessage(Html.fromHtml(String.format("For %s, would you like to be a <b>Driver</b> " +
                                    "or a <b>Passenger</b>?", cruEvent.name)))
                            .setPositiveButton("PASSENGER", (dialog1, which) -> {

                                Intent passengerIntent = new Intent(activity, PassengerSignupActivity.class);

                                passengerIntent.putExtra(AppConstants.EVENT_KEY, Parcels.wrap(cruEvent));

                                activity.startActivityForResult(passengerIntent, AppConstants.PASSENGER_REQUEST_CODE);
                            })
                            .setNegativeButton("DRIVER", (dialog1, which) -> {

                                Intent driverIntent = new Intent(activity,
                                        DriverSignupActivity.class);

                                Parcelable serializedEvent = Parcels.wrap(cruEvent);
                                driverIntent.putExtra(AppConstants.EVENT_KEY, serializedEvent);
                                activity.startActivityForResult(driverIntent, AppConstants.DRIVER_REQUEST_CODE);
                            })
                            .create();
                    dialog.show();
                    break;
                case DRIVER:
                    Bundle driverBundle = new Bundle();
                    driverBundle.putInt(AppConstants.MY_RIDES_TAB, AppConstants.DRIVER_TAB);
                    ((MainActivity) activity).switchToMyRides(driverBundle);
                    break;
                case PASSENGER:
                    Bundle passengerBundle = new Bundle();
                    passengerBundle.putInt(AppConstants.MY_RIDES_TAB, AppConstants.PASSENGER_TAB);
                    ((MainActivity) activity).switchToMyRides(passengerBundle);
                    break;
            }
        }
        else
        {
            Snackbar.make(activity.findViewById(android.R.id.content), "Ride Sharing Unavailable!", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}

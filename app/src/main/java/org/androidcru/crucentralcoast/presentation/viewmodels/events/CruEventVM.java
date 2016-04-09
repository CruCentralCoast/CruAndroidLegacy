package org.androidcru.crucentralcoast.presentation.viewmodels.events;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.Location;
import org.androidcru.crucentralcoast.presentation.providers.CalendarProvider;
import org.androidcru.crucentralcoast.presentation.providers.FacebookProvider;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.androidcru.crucentralcoast.presentation.views.dialogs.RsvpDialog;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup.DriverSignupActivity;
import org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup.PassengerSignupActivity;
import org.parceler.Parcels;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Set;

import rx.Observer;

public class CruEventVM
{
    private Fragment eventFragment;
    public CruEvent cruEvent;
    public boolean isExpanded;
    public boolean addedToCalendar;
    public long localEventId;

    //REVIEW can we reuse what's in AppConstants for consistents date formats?
    public final static String DATE_FORMATTER = "EEEE MMMM d,";
    public final static String TIME_FORMATTER = "h:mm a";

    public CruEventVM(Fragment eventFragment, CruEvent cruEvent, boolean isExpanded, boolean addedToCalendar, long localEventId)
    {
        this.cruEvent = cruEvent;
        this.isExpanded = isExpanded;
        this.addedToCalendar = addedToCalendar;
        this.localEventId = localEventId;
        this.eventFragment = eventFragment;
    }

    public String getDateTime()
    {
        return cruEvent.startDate.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                + " " + cruEvent.startDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
    }

    public View.OnClickListener onCalendarClick()
    {
        final SharedPreferences sharedPreferences = CruApplication.getSharedPreferences();

        //REVIEW Observer could more efficiently written with Observers.create()
        Observer<Pair<String, Long>> onCalendarWrittenObserver = new Observer<Pair<String, Long>>()
        {
            @Override
            public void onCompleted()
            {
            }

            @Override
            public void onError(Throwable e)
            {
            }

            @Override
            public void onNext(Pair<String, Long> eventInfo)
            {
                String cruEventId = eventInfo.first;
                long calendarId = eventInfo.second;
                if (eventInfo.second > -1)
                {
                    /*Toast.makeText(getActivity(), "EventID: " + Long.toString(calendarId) + " added to default calendar",
                            Toast.LENGTH_LONG).show();*/
                    sharedPreferences.edit().putLong(cruEventId, calendarId).commit();
                } else
                {
                    sharedPreferences.edit().remove(cruEventId).commit();
                }

                addedToCalendar = sharedPreferences.contains(cruEvent.id);
                localEventId = sharedPreferences.getLong(cruEvent.id, -1);
            }
        };

        return v -> {
            CruEvent selectedEvent = cruEvent;
            final boolean adding = !addedToCalendar;
            //REVIEW magic strings
            String operation = adding ? "Add " : "Remove ";
            AlertDialog confirmDialog = new AlertDialog.Builder(v.getContext())
                    .setTitle(operation + selectedEvent.name + " to your calendar?")
                    .setNegativeButton("NOPE", (dialog, which) -> {
                    })
                    .setPositiveButton("SURE", (dialog, which) -> {
                        if (adding)
                            CalendarProvider.getInstance().addEventToCalendar(v.getContext(), selectedEvent, onCalendarWrittenObserver);
                        else
                            CalendarProvider.getInstance().removeEventFromCalendar(v.getContext(), selectedEvent, localEventId, onCalendarWrittenObserver);
                    })
                    .create();
            confirmDialog.show();
        };
    }

    public View.OnClickListener onMapClick()
    {
        return v -> {
            CruEvent selectedEvent = cruEvent;
            Location loc = selectedEvent.location;
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
        };
    }

    public View.OnClickListener onFacebookClick()
    {
        return v -> {
            CruEvent selectedEvent = cruEvent;
            Intent openInFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedEvent.url));
            openInFacebook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            RsvpDialog rsvpDialog = new RsvpDialog(v.getContext(), selectedEvent.url);

            Observer<LoginResult> loginResultObserver = new Observer<LoginResult>()
            {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onNext(LoginResult loginResult) {
                    Set<String> grantedPermissions = FacebookProvider.getInstance().getPermissions();
                    //REVIEW magic strings, AppConstants
                    if(grantedPermissions.contains("rsvp_event"))
                        rsvpDialog.show();
                    else
                        v.getContext().startActivity(openInFacebook);
                }
            };


            AlertDialog loginDialog = new AlertDialog.Builder(v.getContext())
                    .setTitle(R.string.facebook_title)
                    .setNegativeButton(R.string.facebook_no, (dialog, which) -> {
                        v.getContext().startActivity(openInFacebook);
                    })
                    .setPositiveButton(R.string.facebook_yes, (dialog, which) -> {
                        MainActivity.loginWithFacebook();
                        FacebookProvider.getInstance().setupTokenCallback(loginResultObserver);
                    })
                    .setMessage(R.string.facebook_reasoning)
                    .create();

            if(selectedEvent.url != null)
            {
                if(FacebookProvider.getInstance().isTokenValid())
                {
                    if (!FacebookProvider.getInstance().getPermissions().contains("rsvp_event"))
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
                Logger.d("No Facebook URL set");

        };
    }

    public View.OnClickListener onRideShareSharing()
    {
        //TODO Passenger and Driver Activities should have a unified set of Extras
        return v -> {
            if(cruEvent.rideSharingEnabled)
            {
                //REVIEW magic strings
                AlertDialog dialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("Ride Sharing")
                        .setMessage(Html.fromHtml(String.format("For %s, would you like to be a <b>Driver</b> " +
                                "or a <b>Passenger</b>?", cruEvent.name)))
                        .setPositiveButton("PASSENGER", (dialog1, which) -> {

                            Intent passengerIntent = new Intent(eventFragment.getContext(),
                                    PassengerSignupActivity.class);

                            passengerIntent.putExtra(AppConstants.EVENT_KEY, Parcels.wrap(cruEvent));

                            eventFragment.startActivityForResult(passengerIntent, AppConstants.PASSENGER_REQUEST_CODE);
                        })
                        .setNegativeButton("DRIVER", (dialog1, which) -> {

                            Intent driverIntent = new Intent(eventFragment.getContext(),
                                    DriverSignupActivity.class);

//                            driverIntent.putExtra(AppConstants.EVENT_STARTDATE, cruEvent.startDate);
                            Parcelable serializedEvent = Parcels.wrap(cruEvent);
                            driverIntent.putExtra(AppConstants.EVENT_KEY, serializedEvent);

                            eventFragment.startActivityForResult(driverIntent, AppConstants.DRIVER_REQUEST_CODE);
                        })
                        .create();
                dialog.show();
            }
            else
            {
                Snackbar.make(eventFragment.getView(), "Ride Sharing Unavailable!", Snackbar.LENGTH_SHORT)
                        .show();
            }
        };
    }

}

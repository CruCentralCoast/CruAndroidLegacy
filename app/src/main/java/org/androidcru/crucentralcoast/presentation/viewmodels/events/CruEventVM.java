package org.androidcru.crucentralcoast.presentation.viewmodels.events;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.Location;
import org.androidcru.crucentralcoast.presentation.providers.CalendarProvider;
import org.androidcru.crucentralcoast.presentation.providers.FacebookProvider;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;
import org.androidcru.crucentralcoast.presentation.views.dialogs.RsvpDialog;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Set;

import rx.Observer;

@SuppressWarnings("unused")
public class CruEventVM extends BaseObservable
{
    public CruEvent cruEvent;
    public final ObservableBoolean isExpanded = new ObservableBoolean();
    public final ObservableBoolean addedToCalendar = new ObservableBoolean();
    public long localEventId;

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";

    public CruEventVM(CruEvent cruEvent, boolean isExpanded, boolean addedToCalendar, long localEventId)
    {
        this.cruEvent = cruEvent;
        this.isExpanded.set(isExpanded);
        this.addedToCalendar.set(addedToCalendar);
        this.localEventId = localEventId;
    }

    public String getDateTime()
    {
        return cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                + " " + cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER))
                + " - " + cruEvent.mEndDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
    }

    public View.OnClickListener onCalendarClick()
    {
        final SharedPreferences sharedPreferences = CruApplication.getSharedPreferences();

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

                addedToCalendar.set(sharedPreferences.contains(cruEvent.mId));
                localEventId = sharedPreferences.getLong(cruEvent.mId, -1);
            }
        };

        return v -> {
            CruEvent selectedEvent = cruEvent;
            final boolean adding = !addedToCalendar.get();
            String operation = adding ? "Add " : "Remove ";
            AlertDialog confirmDialog = new AlertDialog.Builder(v.getContext())
                    .setTitle(operation + selectedEvent.mName + " to your calendar?")
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
            Location loc = selectedEvent.mLocation;
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
                Toast.makeText(v.getContext(), "Please install Google Maps to view this event's location", Toast.LENGTH_LONG).show();
            }
        };
    }

    public View.OnClickListener onFacebookClick()
    {
        return v -> {
            CruEvent selectedEvent = cruEvent;
            Intent openInFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedEvent.mUrl));
            openInFacebook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            RsvpDialog rsvpDialog = new RsvpDialog(v.getContext(), selectedEvent.mUrl);

            Observer<LoginResult> loginResultObserver = new Observer<LoginResult>()
            {
                @Override
                public void onCompleted() {}

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onNext(LoginResult loginResult) {
                    Set<String> grantedPermissions = FacebookProvider.getInstance().getPermissions();
                    if(grantedPermissions.contains("rsvp_event"))
                        rsvpDialog.show();
                    else
                        v.getContext().startActivity(openInFacebook);
                }
            };


            AlertDialog loginDialog = new AlertDialog.Builder(v.getContext())
                    .setTitle("Log in with Facebook")
                    .setNegativeButton("JUST OPEN IN FACEBOOK", (dialog, which) -> {
                        v.getContext().startActivity(openInFacebook);
                    })
                    .setPositiveButton("SURE", (dialog, which) -> {
                        MainActivity.loginWithFacebook();
                        FacebookProvider.getInstance().setupTokenCallback(loginResultObserver);
                    })
                    .setMessage("If you log in with Facebook, you can set your RSVP directly from inside the Cru app.")
                    .create();

            if(selectedEvent.mUrl != null)
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
}

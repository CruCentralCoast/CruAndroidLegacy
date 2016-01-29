package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.Location;
import org.androidcru.crucentralcoast.presentation.modelviews.CruEventMV;
import org.androidcru.crucentralcoast.presentation.providers.CalendarProvider;
import org.androidcru.crucentralcoast.presentation.providers.FacebookProvider;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.views.dialogs.RsvpDialog;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;

/**
 * EventsAdapter is an RecyclerView adapter binding the Event model to the Event RecyclerView
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.CruEventViewHolder>
{
    private ArrayList<CruEventMV> mEvents;
    private Activity mParent;

    public final static String DATE_FORMATTER = "EEEE MMMM ee,";
    public final static String TIME_FORMATTER = "h:mm a";

    private LinearLayoutManager mLayoutManager;
    private Observer<Pair<String, Long>> mOnCalendarWritten;

    public EventsAdapter(Activity parent, ArrayList<CruEventMV> cruEvents, LinearLayoutManager layoutManager, Observer<Pair<String, Long>> onCalendarWritten)
    {
        this.mParent = parent;
        this.mEvents = cruEvents;
        this.mLayoutManager = layoutManager;
        this.mOnCalendarWritten = onCalendarWritten;
    }

    /**
     * Invoked by the Adapter if a new fresh view needs to be used
     * @param parent Parent view to inflate in, provided by Android
     * @param viewType Integer representer a enumeration of heterogeneous views
     * @return CruEventViewHolder, a representation of the model for the view
     */
    @Override
    public CruEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event, parent, false);
        return new CruEventViewHolder(view);
    }

    //TODO support events spanning multiple days (fall retreat)
    /**
     * Invoked by the Adapter if a fresh view needs configuration or an old view needs to be recycled
     * @param holder CruEventViewHolder returned by onCreateViewHolder()
     * @param position Position in the RecyclerView
     */
    @Override
    public void onBindViewHolder(CruEventViewHolder holder, int position)
    {
        CruEvent cruEvent = mEvents.get(position).mCruEvent;
        CruEventMV cruEventMV = mEvents.get(position);

        if(cruEvent.mImage != null)
            Picasso.with(mParent)
                    .load(cruEvent.mImage.mURL)
                    .placeholder(R.drawable.logo_grey)
                    .fit()
                    .into(holder.banner);
        holder.eventName.setText(cruEvent.mName);
        holder.eventDate.setText(cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                + " " + cruEvent.mStartDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER))
                + " - " + cruEvent.mEndDate.format(DateTimeFormatter.ofPattern(TIME_FORMATTER)));
        holder.eventDescription.setText(cruEvent.mDescription);
        holder.eventDescription.setVisibility(cruEventMV.mIsExpanded ? View.VISIBLE : View.GONE);

        holder.chevView.setImageDrawable(
                ContextCompat.getDrawable(mParent,
                        holder.eventDescription.getVisibility() == View.VISIBLE ? R.drawable.ic_chevron_up_grey600_48dp
                                : R.drawable.ic_chevron_down_grey600_48dp));

        holder.calButton.setSelected(cruEventMV.mAddedToCalendar);
        holder.calButton.setImageDrawable(DrawableUtil.getTintListedDrawable(mParent, cruEventMV.mAddedToCalendar
                ? R.drawable.ic_calendar_check_grey600_48dp
                : R.drawable.ic_calendar_plus_grey600_48dp, R.color.cal_action));

        holder.fbButton.setImageDrawable(DrawableUtil.getTintedDrawable(mParent, R.drawable.ic_facebook_box_grey600_48dp, R.color.fbBlue));
        holder.mapButton.setImageDrawable(DrawableUtil.getTintedDrawable(mParent, R.drawable.ic_map_marker_grey600_48dp, R.color.red600));
    }



    /**
     * Invoked by the Adapter when Android needs to know how many items are in this list
     * @return Number of items in the list
     */
    @Override
    public int getItemCount()
    {
        return mEvents.size();
    }

    /**
     * CruEventViewHolder is a view representation of the model for the list
     */
    public class CruEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.event_banner) ImageView banner;
        @Bind(R.id.fbButton) ImageButton fbButton;
        @Bind(R.id.mapButton) ImageButton mapButton;
        @Bind(R.id.calButton) ImageButton calButton;
        @Bind(R.id.eventName) TextView eventName;
        @Bind(R.id.eventDate) TextView eventDate;
        @Bind(R.id.eventDescription) TextView eventDescription;
        @Bind(R.id.chevView) ImageView chevView;

        public CruEventViewHolder(View rootView) {
            super(rootView);
            ButterKnife.bind(this, rootView);
            rootView.setOnClickListener(this);

            calButton.setOnClickListener(v -> {
                CruEvent selectedEvent = mEvents.get(getAdapterPosition()).mCruEvent;
                final boolean adding = !mEvents.get(getAdapterPosition()).mAddedToCalendar;
                String operation = adding ? "Add " : "Remove ";
                AlertDialog confirmDialog = new AlertDialog.Builder(mParent)
                        .setTitle(operation + selectedEvent.mName + " to your calendar?")
                        .setNegativeButton("NOPE", (dialog, which) -> {
                        })
                        .setPositiveButton("SURE", (dialog, which) -> {
                            if(adding)
                                CalendarProvider.getInstance().addEventToCalendar(mParent, selectedEvent, mOnCalendarWritten);
                            else
                                CalendarProvider.getInstance().removeEventFromCalendar(mParent, selectedEvent, mEvents.get(getAdapterPosition()).mLocalEventId, mOnCalendarWritten);
                        })
                        .create();
                confirmDialog.show();
            });

            mapButton.setOnClickListener(v -> {
                CruEvent selectedEvent = mEvents.get(getAdapterPosition()).mCruEvent;
                Location loc = selectedEvent.mLocation;
                String uri = String.format("geo:0,0?q=%s", loc.toString());
                //Uri gmmIntentUri = Uri.parse(String.format("geo:0,0?q=%s"), loc.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try
                {
                    mParent.startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    Toast.makeText(mParent, "Please install Google Maps to view this event's location", Toast.LENGTH_LONG).show();
                }
            });

            fbButton.setOnClickListener(v -> {
                CruEvent selectedEvent = mEvents.get(getAdapterPosition()).mCruEvent;
                Intent openInFacebook = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedEvent.mUrl));
                openInFacebook.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                RsvpDialog rsvpDialog = new RsvpDialog(mParent, selectedEvent.mUrl);

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
                            mParent.startActivity(openInFacebook);
                    }
                };


                AlertDialog loginDialog = new AlertDialog.Builder(mParent)
                        .setTitle("Log in with Facebook")
                        .setNegativeButton("JUST OPEN IN FACEBOOK", (dialog, which) -> {
                            mParent.startActivity(openInFacebook);
                        })
                        .setPositiveButton("SURE", (dialog, which) -> {
                                LoginManager.getInstance().logInWithPublishPermissions(mParent, Collections.singletonList("rsvp_event"));
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

            });
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
            int visibility;
            if(eventDescription.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            eventDescription.setVisibility(visibility);

            mEvents.get(getAdapterPosition()).mIsExpanded = (View.VISIBLE == visibility);
            notifyItemChanged(getAdapterPosition());
            mLayoutManager.scrollToPosition(getAdapterPosition());
        }
    }
}

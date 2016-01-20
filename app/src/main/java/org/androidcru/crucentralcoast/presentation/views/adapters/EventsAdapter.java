package org.androidcru.crucentralcoast.presentation.views.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
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

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.presentation.modelviews.CruEventMV;
import org.androidcru.crucentralcoast.presentation.providers.CalendarProvider;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

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

    public final static String DATE_FORMATTER = "EE MMMM ee,";
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
                        holder.eventDescription.getVisibility() == View.VISIBLE ? R.drawable.ic_chevron_up_grey600_36dp
                                : R.drawable.ic_chevron_down_grey600_36dp));

        holder.calButton.setSelected(cruEventMV.mAddedToCalendar);
        holder.calButton.setImageDrawable(getTintListedDrawable(cruEventMV.mAddedToCalendar
                ? R.drawable.ic_calendar_check_grey600_36dp
                : R.drawable.ic_calendar_plus_grey600_36dp, R.color.cal_action));

        holder.fbButton.setImageDrawable(getTintedDrawable(R.drawable.ic_facebook_box_grey600_36dp, R.color.fbBlue));
        holder.mapButton.setImageDrawable(getTintedDrawable(R.drawable.ic_map_marker_grey600_36dp, R.color.red600));
    }

    private Drawable getTintedDrawable(int drawableId, int colorId)
    {
        Drawable coloredCal = DrawableCompat.wrap(ContextCompat.getDrawable(mParent, drawableId));
        DrawableCompat.setTint(coloredCal, ContextCompat.getColor(mParent, colorId));
        return coloredCal;
    }

    private Drawable getTintListedDrawable(int drawableId, int tintListId)
    {
        Drawable coloredCal = DrawableCompat.wrap(ContextCompat.getDrawable(mParent, drawableId));
        DrawableCompat.setTintList(coloredCal, ContextCompat.getColorStateList(mParent, tintListId));
        return coloredCal;
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

            fbButton.setOnClickListener(v -> {
                CruEvent selectedEvent = mEvents.get(getAdapterPosition()).mCruEvent;
                AlertDialog loginDialog = new AlertDialog.Builder(mParent)
                        .setTitle("Log in with Facebook")
                        .setNegativeButton("JUST OPEN IN FACEBOOK", (dialog, which) -> {
                            mParent.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(selectedEvent.mUrl)));
                        })
                        .setPositiveButton("SURE", (dialog, which) -> {
                            /*LoginManager.getInstance().logInWithReadPermissions(mParent, Collections.singletonList("rsvp_events"));
                            FacebookProvider.getInstance().setupTokenCallback().subscribe(loginResult -> {

                            });*/
                        })
                        .setMessage("If you log in with Facebook, you can set your RSVP directly from inside the Cru app.")
                        .create();
                if(selectedEvent.mUrl != null)
                    loginDialog.show();
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

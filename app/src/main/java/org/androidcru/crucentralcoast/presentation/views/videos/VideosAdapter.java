package org.androidcru.crucentralcoast.presentation.views.videos;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.api.services.youtube.model.SearchResult;
import com.squareup.picasso.Picasso;
import org.androidcru.crucentralcoast.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.CruVideoViewHolder>
{
    // Constants to represent the state of expansion of a view.
    public enum ViewState
    {
        // Retracted state
        RETRACTED ("Show Description"),

        // Expanded state
        EXPANDED ("Hide Description");

        private final String state;

        private ViewState(String state)
        {
            this.state = state;
        }

        public String toString()
        {
            return this.state;
        }
    }

    // Search results from Cru's YouTube channel
    private ArrayList<SearchResult> videos;

    // Keeps the state of expansion of a view
    private ArrayList<Boolean> viewExpandedStates;

    private LinearLayoutManager layoutManager;

    public VideosAdapter(List<SearchResult> videos, LinearLayoutManager layoutManager)
    {
        this.videos = (ArrayList)videos;

        // Initialize expansion states to retracted for every view
        viewExpandedStates = new ArrayList<>();
        for(int i = 0; i < videos.size(); ++i)
        {
            viewExpandedStates.add(false);
        }

        this.layoutManager = layoutManager;
    }

    @Override
    public CruVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CruVideoViewHolder(inflater.inflate(R.layout.card_video, parent, false));
    }

    @Override
    public int getItemCount()
    {
        return videos.size();
    }



    @Override
    public void onBindViewHolder(CruVideoViewHolder holder, int position)
    {
        SearchResult searchResult = videos.get(position);
        Boolean isExpanded = viewExpandedStates.get(position);

        // Set the card title to the video title
        holder.videoTitle.setText(searchResult.getSnippet().getTitle());
        Context context = holder.videoThumb.getContext();

        // Set the Card's vID to the id of the video
        holder.vID = searchResult.getId().getVideoId();

        // Set the text of the TextView that is selected to toggle the
        // expansion state of a view
        holder.toggleVideoDescription
                .setText(isExpanded ?
                        ViewState.EXPANDED.toString() : ViewState.RETRACTED.toString());

        // Set the video thumbnail with the thumbnail URL
        Picasso.with(context)
                .load(searchResult.getSnippet().getThumbnails().getHigh().getUrl())
                .fit()
                .into(holder.videoThumb);

        // Play the video corresponding with the selected thumbnail
        holder.videoThumb.setOnClickListener((View v) ->
        {
            //TODO not sure what to do if player cant resolve video, so I make toast
            if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(context))
            {
                context.startActivity(YouTubeIntents
                        .createPlayVideoIntentWithOptions(context, holder.vID, true, true));
            }
            else
            {
                Toast.makeText(context, "Unable to play video.", Toast.LENGTH_SHORT)
                    .show();
            }
        });

        // Set the video description to the card's TextView
        holder.videoDescription.setText(searchResult.getSnippet().getDescription());
        holder.videoDescription.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // If the description is selected, retract the view
        holder.videoDescription.setOnClickListener((View v) ->
        {
            holder.videoDescription.setVisibility(View.GONE);
            viewExpandedStates.set(position, false);
            notifyItemChanged(holder.getAdapterPosition());
            layoutManager.scrollToPosition(holder.getAdapterPosition());

            holder.toggleVideoDescription.setText(ViewState.EXPANDED.toString());
        });

        // Toggle the expansion of a view on the selection of the video
        // description toggle button
        holder.toggleVideoDescription.setOnClickListener((View v) ->
        {
            int visibility;
            if (holder.videoDescription.getVisibility() == View.VISIBLE)
            {
                visibility = View.GONE;
            }
            else
            {
                visibility = View.VISIBLE;
            }
            holder.videoDescription.setVisibility(visibility);

            holder.toggleVideoDescription
                    .setText(visibility == View.VISIBLE ?
                            ViewState.RETRACTED.toString() : ViewState.EXPANDED.toString());
            viewExpandedStates.set(position, visibility == View.VISIBLE);
            notifyItemChanged(holder.getAdapterPosition());
            layoutManager.scrollToPosition(holder.getAdapterPosition());
        });
    }

    public class CruVideoViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.video_title) TextView videoTitle;
        @Bind(R.id.video_thumb) ImageView videoThumb;
        @Bind(R.id.video_description) TextView videoDescription;
        @Bind(R.id.toggle_description) TextView toggleVideoDescription;
        String vID;

        public CruVideoViewHolder(View rootView)
        {
            super(rootView);
            ButterKnife.bind(this, rootView);
        }
    }
}

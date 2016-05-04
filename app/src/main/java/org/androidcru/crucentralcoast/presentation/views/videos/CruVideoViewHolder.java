package org.androidcru.crucentralcoast.presentation.views.videos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeIntents;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.DatedVideo;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CruVideoViewHolder extends RecyclerView.ViewHolder
{
    @Bind(R.id.video_title) TextView videoTitle;
    @Bind(R.id.video_thumb) ImageView videoThumb;
    @Bind(R.id.video_description) TextView videoDescription;
    @Bind(R.id.video_chev) ImageView videoChev;
    @Bind(R.id.video_id_and_views) TextView videoIdAndViews;
    @Bind(R.id.video_expand_description_layout) RelativeLayout videoExpandDescriptionLayout;

    public SearchResult model;

    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    public CruVideoViewHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager)
    {
        super(rootView);
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        ButterKnife.bind(this, rootView);
    }
    
    public void bindSearchResult(ExpandableState<SearchResult> state)
    {
        this.model = state.model;
        bindUI(state);
    }
    
    public void bindDatedVideo(ExpandableState<DatedVideo> state)
    {
        this.model = state.model.getVideo();
        bindUI(state);
    }

    private void bindUI(ExpandableState state)
    {
        SearchResultSnippet snippet = model.getSnippet();

        // Set the card title to the video title
        videoTitle.setText(snippet.getTitle());

        // Set this video's date and number of views
        videoIdAndViews.setText(
                DateUtils.getRelativeTimeSpanString(snippet.getPublishedAt().getValue()));

        Context context = videoThumb.getContext();

        ViewUtil.setSource(videoThumb, model.getSnippet().getThumbnails().getHigh().getUrl(), ViewUtil.SCALE_TYPE.FIT);

        // Set the chevron to up or down depending on if the view is expanded or not
        videoChev.setImageDrawable(state.isExpanded
                ? DrawableUtil.getDrawable(context, R.drawable.ic_chevron_up_grey600)
                : DrawableUtil.getDrawable(context, R.drawable.ic_chevron_down_grey600));

        // Play the video when the video thumbnail is clicked.
        videoThumb.setOnClickListener((View v) ->
        {
            // not sure what to do if player cant resolve video, so I make toast
            if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(context)) {
                context.startActivity(YouTubeIntents
                        .createPlayVideoIntentWithOptions(context, model.getId().getVideoId(), true, true));
            } else {
                Toast.makeText(context, AppConstants.VIDEO_PLAY_FAILED_MESSAGE,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Set the video description to the card's TextView
        videoDescription.setText(model.getSnippet().getDescription());
        if(videoDescription.getText().length() == 0)
            videoDescription.setText(R.string.videos_no_description);

        videoDescription.setVisibility(state.isExpanded ? View.VISIBLE : View.GONE);

        // If the description is selected, retract the view
        videoDescription.setOnClickListener((View v) ->
        {
            videoDescription.setVisibility(View.GONE);
            state.isExpanded = false;
            adapter.notifyItemChanged(getAdapterPosition());
            layoutManager.scrollToPosition(getAdapterPosition());

        });

        // Toggle the expansion of a view on the selection of the video
        // description toggle button
        videoExpandDescriptionLayout.setOnClickListener((View v) ->
        {
            int visibility;
            if (videoDescription.getVisibility() == View.VISIBLE) {
                visibility = View.GONE;
            } else {
                visibility = View.VISIBLE;
            }
            videoDescription.setVisibility(visibility);

            videoChev.setImageDrawable(visibility == View.VISIBLE
                    ? DrawableUtil.getDrawable(context, R.drawable.ic_chevron_up_grey600)
                    : DrawableUtil.getDrawable(context, R.drawable.ic_chevron_down_grey600));

            state.isExpanded = visibility == View.VISIBLE;
            adapter.notifyItemChanged(getAdapterPosition());
            layoutManager.scrollToPosition(getAdapterPosition());
        });
    }
}

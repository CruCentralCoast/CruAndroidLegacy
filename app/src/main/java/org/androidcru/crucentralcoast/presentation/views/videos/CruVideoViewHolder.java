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
import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CruVideoViewHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.video_title) TextView videoTitle;
    @BindView(R.id.video_thumb) ImageView videoThumb;
    @BindView(R.id.video_description) TextView videoDescription;
    @BindView(R.id.video_chev) ImageView videoChev;
    @BindView(R.id.video_id_and_views) TextView videoIdAndViews;
    @BindView(R.id.video_expand_description_layout) RelativeLayout videoExpandDescriptionLayout;

    public Snippet model;

    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    public CruVideoViewHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager)
    {
        super(rootView);
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        ButterKnife.bind(this, rootView);
    }
    
    public void bindSnippet(ExpandableState<Snippet> state)
    {
        this.model = state.model;
        bindUI(state);
    }
    
    public void bindDatedVideo(ExpandableState<Snippet> state)
    {
        this.model = state.model;
        bindUI(state);
    }

    private void bindUI(ExpandableState state)
    {
        // Set the card title to the video title
        videoTitle.setText(model.title);

        // Set this video's date and number of views
        videoIdAndViews.setText(
                DateUtils.getRelativeTimeSpanString(model.getDate().toInstant().toEpochMilli()));

        Context context = videoThumb.getContext();

        ViewUtil.setSource(videoThumb, model.getHigh().url, ViewUtil.SCALE_TYPE.FIT);

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
                        .createPlayVideoIntentWithOptions(context, model.getVideoId(), true, true));
            } else {
                Toast.makeText(context, AppConstants.VIDEO_PLAY_FAILED_MESSAGE,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Set the video description to the card's TextView
        videoDescription.setText(model.description);
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

package org.androidcru.crucentralcoast.presentation.views.videos;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeIntents;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CruVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.card_video_view)
    CardView cardView;
    @BindView(R.id.videos_divider)
    View divider;
    @BindView(R.id.video_title)
    TextView videoTitle;
    @BindView(R.id.video_thumb)
    ImageView videoThumb;
    @BindView(R.id.video_description)
    TextView videoDescription;
    @BindView(R.id.video_chev)
    ImageView videoChev;
    @BindView(R.id.video_id_and_views)
    TextView videoIdAndViews;
    @BindView(R.id.video_expand_description_layout)
    RelativeLayout videoExpandDescriptionLayout;
    @BindView(R.id.animating_layout)
    public LinearLayout animatingLayout;

    public Snippet model;
    private ExpandableState<Snippet> state;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;
    public View rootView;

    public CruVideoViewHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager, Boolean isFeed) {
        this(rootView, adapter, layoutManager);
    }

    public CruVideoViewHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        super(rootView);
        this.rootView = rootView;
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        ButterKnife.bind(this, rootView);
        animatingLayout.setOnClickListener(this);
        ViewUtil.debounceExpandingView(animatingLayout, this);
    }

    public void bindSnippet(ExpandableState<Snippet> state) {
        this.model = state.model;
        this.state = state;
        bindUI(state);
    }

    private void bindUI(ExpandableState state) {
        // Set the card title to the video title
        videoTitle.setText(model.title);

        // Set this video's date and number of views
        videoIdAndViews.setText(
                DateUtils.getRelativeTimeSpanString(model.getDate().toInstant().toEpochMilli()));

        Context context = videoThumb.getContext();

        ViewUtil.setSource(videoThumb, model.getHigh().url, 0, null, null, ViewUtil.SCALE_TYPE.FIT);

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
            }
            else {
                Toast.makeText(context, AppConstants.VIDEO_PLAY_FAILED_MESSAGE,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Set the video description to the card's TextView
        videoDescription.setText(model.description);
        if (videoDescription.getText().length() == 0)
            videoDescription.setText(R.string.videos_no_description);

        videoDescription.setVisibility(state.isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        int visibility;
        if (videoDescription.getVisibility() == View.VISIBLE) {
            visibility = View.GONE;
        }
        else {
            visibility = View.VISIBLE;
        }
        videoDescription.setVisibility(visibility);
        Context context = videoChev.getContext();
        videoChev.setImageDrawable(visibility == View.VISIBLE
                ? DrawableUtil.getDrawable(context, R.drawable.ic_chevron_up_grey600)
                : DrawableUtil.getDrawable(context, R.drawable.ic_chevron_down_grey600));

        state.isExpanded = visibility == View.VISIBLE;
        adapter.notifyItemChanged(getAdapterPosition());
        layoutManager.scrollToPosition(getAdapterPosition());
    }
}

package org.androidcru.crucentralcoast.presentation.views.videos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;

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

    String vID;

    public CruVideoViewHolder(View rootView)
    {
        super(rootView);
        ButterKnife.bind(this, rootView);
    }
}

package org.androidcru.crucentralcoast.presentation.views.videos;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.youtube.model.SearchResult;
import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mitch on 3/2/16.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.CruVideoViewHolder>
{
    private List<SearchResult> videos;
    private LinearLayoutManager layoutManager;

    public VideosAdapter(List<SearchResult> videos, LinearLayoutManager layoutManager)
    {
        this.videos = videos;
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
        holder.videoTitle.setText(searchResult.getSnippet().getTitle());
        Context context = holder.videoThumb.getContext();

        Picasso.with(context)
                .load(searchResult.getSnippet().getThumbnails().getStandard().getUrl())
                .fit()
                .into(holder.videoThumb);
    }

    public class CruVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @Bind(R.id.videoTitle) TextView videoTitle;
        @Bind(R.id.videoThumb) ImageView videoThumb;

        public CruVideoViewHolder(View rootView)
        {
            super(rootView);
            ButterKnife.bind(this, rootView);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // TODO play video on click here
        }
    }
}

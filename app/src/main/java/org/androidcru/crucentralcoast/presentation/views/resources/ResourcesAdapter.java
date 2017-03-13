package org.androidcru.crucentralcoast.presentation.views.resources;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;

import java.util.ArrayList;
import java.util.List;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourceViewHolder>
{
    private ArrayList<Resource> resources;

    public ResourcesAdapter(ArrayList<Resource> resources)
    {
        this.resources = new ArrayList<>(resources);
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ResourceViewHolder(inflater.inflate(R.layout.item_resource, parent, false));
    }

    public void onBindViewHolder(ResourceViewHolder holder, int position)
    {
        holder.bind(resources.get(position));
    }

    @Override
    public int getItemCount()
    {
        return resources.size();
    }

    public void refreshResources(List<Resource> resources) {
        this.resources.clear();
        this.resources.addAll(resources);
        notifyDataSetChanged();
    }
}

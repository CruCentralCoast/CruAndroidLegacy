package com.crucentralcoast.app.presentation.views.conttactcards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CruUser;

import java.util.List;

public class UserContactCardsAdapter extends RecyclerView.Adapter<UserContactCardVH>
{
    private List<CruUser> users;

    public UserContactCardsAdapter(List<CruUser> users)
    {
        this.users = users;
    }

    @Override
    public UserContactCardVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new UserContactCardVH(inflater.inflate(R.layout.contact, parent, false));
    }

    @Override
    public void onBindViewHolder(UserContactCardVH holder, int position)
    {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }
}

package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.databinding.ItemDriverResultBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideResultVM;

import java.util.ArrayList;
import org.androidcru.crucentralcoast.BR;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContent;

public class DriverResultsAdapter extends RecyclerView.Adapter<DriverResultsAdapter.DriverResultViewHolder>
{

    private Activity mParent;
    private ArrayList<Ride> rides;

    public DriverResultsAdapter(Activity parent, ArrayList<Ride> rides)
    {
        this.mParent = parent;
        this.rides = rides;
    }

    @Override
    public DriverResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDriverResultBinding binding = ItemDriverResultBinding.inflate(inflater, parent, false);

        return new DriverResultViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(DriverResultViewHolder holder, int position)
    {
        RideResultVM rideResultVM = new RideResultVM(rides.get(position));
        holder.getBinding().setVariable(BR.event, rideResultVM);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount()
    {
        return rides.size();
    }

    public class DriverResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public DriverResultViewHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        public ItemDriverResultBinding getBinding() {
            return DataBindingUtil.getBinding(itemView);
        }

        @Override
        public void onClick(View v)
        {
            ((FormContent) mParent).onNext();
        }
    }
}

package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.BR;
import org.androidcru.crucentralcoast.data.models.Ride;
import org.androidcru.crucentralcoast.databinding.ItemDriverResultBinding;
import org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing.RideResultVM;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContent;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

import java.util.List;

public class DriverResultsAdapter extends RecyclerView.Adapter<DriverResultsAdapter.DriverResultViewHolder>
{

    private FormContent formContent;
    private List<Ride> rides;
    private FormHolder formHolder;

    public DriverResultsAdapter(FormContent formContent, FormHolder holder, List<Ride> rides)
    {
        this.formContent = formContent;
        this.rides = rides;
        this.formHolder = holder;
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
        holder.getBinding().setVariable(BR.rideResultVM, rideResultVM);
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
            formHolder.addDataObject(getBinding().getRideResultVM().ride);
            formContent.onNext();
        }
    }
}

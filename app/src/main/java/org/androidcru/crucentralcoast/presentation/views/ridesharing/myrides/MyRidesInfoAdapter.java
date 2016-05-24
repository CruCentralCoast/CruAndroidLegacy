package org.androidcru.crucentralcoast.presentation.views.ridesharing.myrides;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import timber.log.Timber;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Passenger;
import org.androidcru.crucentralcoast.data.providers.RideProvider;
import org.androidcru.crucentralcoast.presentation.util.AlertDialogCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.observers.Observers;


public class MyRidesInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<Passenger> passengers;
    private MyRidesInfoActivity parent;
    private AlertDialogCreator alertDialog;
    private String rideID;
    private String selectedPassengerID;

    public MyRidesInfoAdapter(MyRidesInfoActivity parent, List<Passenger> passengers, String rideID)
    {
        this.parent = parent;
        this.rideID = rideID;
        this.passengers = (passengers == null) ? new ArrayList<Passenger>() : passengers;
        selectedPassengerID = null;
        initAlertDialog();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PassengerInfoHolder(inflater.inflate(R.layout.card_rideinfopassenger, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        PassengerInfoHolder passengerInfoHolder = (PassengerInfoHolder) holder;
        passengerInfoHolder.passengerName.setText(passengers.get(position).name);
        passengerInfoHolder.passengerPhone.setText(passengers.get(position).phone);
    }

    @Override
    public int getItemCount()
    {
        return passengers.size();
    }

    public class PassengerInfoHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.passengerName) TextView passengerName;
        @BindView(R.id.passengerPhoneNum) TextView passengerPhone;
        @BindView(R.id.kickPassenger) Button kickPassenger;

        public PassengerInfoHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            kickPassenger.setOnClickListener(kickPassengerButton());
        }

        private void setSelectedPassenger(String phone) {
            for (int iter = 0; iter < passengers.size(); iter++) {
                if (phone.equals(passengers.get(iter).phone.toString()))
                    selectedPassengerID = passengers.get(iter).id;
            }
        }

        private View.OnClickListener kickPassengerButton() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.setMessage(CruApplication.getContext().getString(R.string.alert_dialog_kick_msg)
                            + " " + passengerName.getText().toString() + "?");
                    setSelectedPassenger(passengerPhone.getText().toString());
                    alertDialog.show();
                }
            };
        }

    }
    private void initAlertDialog() {

        alertDialog = new AlertDialogCreator(parent,
                CruApplication.getContext().getString(R.string.alert_dialog_kick_title),
                CruApplication.getContext().getString(R.string.alert_dialog_kick_msg),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RideProvider.dropPassengerFromRide(parent, Observers.create(v -> {}, e -> {}, () -> parent.forceUpdate()), rideID, selectedPassengerID);
                    }
                });
    }
}

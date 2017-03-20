package com.crucentralcoast.app.presentation.views.ridesharing.myrides;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.presentation.util.DrawableUtil;
import com.crucentralcoast.app.presentation.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.observers.Observers;


public class MyRidesInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<Passenger> passengers;
    private MyRidesInfoActivity parent;
    private AlertDialog alertDialog;
    private String rideID;
    private String selectedPassengerID;

    private ColorGenerator generator = ColorGenerator.MATERIAL;

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
        return new PassengerInfoHolder(inflater.inflate(R.layout.contact, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        PassengerInfoHolder passengerInfoHolder = (PassengerInfoHolder) holder;
        passengerInfoHolder.bind(passengers.get(position));
    }

    @Override
    public int getItemCount()
    {
        return passengers.size();
    }

    public class PassengerInfoHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.icon) ImageView icon;
        @BindView(R.id.title) TextView passengerName;
        @BindView(R.id.subtitle) TextView passengerPhone;
        @BindView(R.id.action1) ImageButton addToContacts;
        @BindView(R.id.action2) ImageButton kickPassenger;

        private Context context;
        private Passenger model;

        public PassengerInfoHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = itemView.getContext();
            addToContacts.setVisibility(View.VISIBLE);
            kickPassenger.setVisibility(View.VISIBLE);
            addToContacts.setImageDrawable(DrawableUtil.getDrawable(itemView.getContext(), R.drawable.contact_mail));
            kickPassenger.setImageDrawable(DrawableUtil.getDrawable(itemView.getContext(), R.drawable.ic_close_grey600));;
        }

        private void setSelectedPassenger(String phone) {
            for (int iter = 0; iter < passengers.size(); iter++) {
                if (phone.equals(passengers.get(iter).phone.toString()))
                    selectedPassengerID = passengers.get(iter).id;
            }
        }

        public void bind(Passenger passenger)
        {
            this.model = passenger;
            bindUI();
        }

        private void bindUI()
        {
            passengerName.setText(model.name);
            passengerPhone.setText(model.phone);

            int color = generator.getColor(model.name);


            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .useFont(ViewUtil.getFont(addToContacts.getContext(), AppConstants.FREIG_SAN_PRO_LIGHT))
                    .endConfig()
                    .buildRound(String.valueOf(model.name.toUpperCase().charAt(0)), color);

            icon.setImageDrawable(drawable);
        }

        @OnClick(R.id.action2)
        protected void kickPassengerButton()
        {
                alertDialog.setMessage(CruApplication.getContext().getString(R.string.alert_dialog_kick_msg)
                        + " " + passengerName.getText().toString() + "?");
                setSelectedPassenger(passengerPhone.getText().toString());
                alertDialog.show();
        }

        @OnClick(R.id.action1)
        protected void onAddToContact()
        {
            context.startActivity(ViewUtil.insertOrEditContact(model.name, model.phone));
        }

    }
    private void initAlertDialog() {
        alertDialog = new AlertDialog.Builder(parent)
            .setTitle(CruApplication.getContext().getString(R.string.alert_dialog_kick_title))
            .setMessage(CruApplication.getContext().getString(R.string.alert_dialog_kick_msg))
            .create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
            CruApplication.getContext().getString(R.string.alert_dialog_yes),
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    RideProvider.dropPassengerFromRide(parent, Observers.create(v -> {}, e -> {}, () -> parent.forceUpdate()), rideID, selectedPassengerID);
                }
            });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
            CruApplication.getContext().getString(R.string.alert_dialog_no),
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
    }
}

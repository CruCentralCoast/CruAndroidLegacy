package com.crucentralcoast.app.presentation.viewmodels.ridesharing;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.CruApplication;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.Passenger;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.providers.RideProvider;
import com.crucentralcoast.app.presentation.views.ridesharing.myrides.MyRidesPassengerFragment;
import com.crucentralcoast.app.util.SharedPreferencesUtil;

import org.threeten.bp.format.DateTimeFormatter;

import rx.observers.Observers;

public class MyRidesPassengerVM {

    public Ride ride;
    public boolean isExpanded;
    private MyRidesPassengerFragment parent;

    AlertDialog alertDialog;

    public MyRidesPassengerVM(MyRidesPassengerFragment parent, Ride ride, boolean isExpanded)
    {
        this.ride = ride;
        this.isExpanded = isExpanded;
        this.parent = parent;
        initAlertDialog();
    }

    public String getDateTime() {
        return ride.time.format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMATTER))
                + " " + ride.time.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMATTER));
    }

    public String getLocation() {
        return ride.location.toString();
    }

    public String getDriverInfo() {
        return CruApplication.getContext().getString(R.string.myrides_passenger_list_name)
                + " " + ride.driverName + "\n"
                + CruApplication.getContext().getString(R.string.myrides_passenger_list_phone)
                + " " + ride.driverNumber;
    }

    private void initAlertDialog()
    {
        alertDialog = new AlertDialog.Builder(parent.getContext()).create();
        alertDialog.setTitle(CruApplication.getContext().getString(R.string.alert_dialog_title));
        alertDialog.setMessage(CruApplication.getContext().getString(R.string.alert_dialog_passenger_msg));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                CruApplication.getContext().getString(R.string.alert_dialog_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Passenger p : ride.passengers) {
                            if (p.fcmId.equals(SharedPreferencesUtil.getFCMID())) {
                                RideProvider.dropPassengerFromRide(parent, Observers.create(v -> {}, e -> {}, () -> parent.forceUpdate()), ride.id, p.id);
                            }
                        }
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                CruApplication.getContext().getString(R.string.alert_dialog_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
    }

    public View.OnClickListener onCancelOfferingClicked()
    {
        return new View.OnClickListener(){
            @Override
            public void onClick(View v)
          {
              alertDialog.show();
          }
        };
    }
}

package org.androidcru.crucentralcoast.presentation.viewmodels.ridesharing;

import android.telephony.PhoneNumberFormattingTextWatcher;

import org.androidcru.crucentralcoast.data.models.Passenger;

public class PassengerVM
{
    public Passenger passenger;

    public PassengerVM(Passenger passenger)
    {
        this.passenger = passenger;
    }

    public void onNameEntered(CharSequence s, int start, int before, int count)
    {
        passenger.name = s.toString();
    }

    public void onPhoneNumberEntered(CharSequence s, int start, int before, int count)
    {
        passenger.phone = s.toString();
    }

    public PhoneNumberFormattingTextWatcher getPhoneNumberTextWatcher()
    {
        return new PhoneNumberFormattingTextWatcher();
    }
}

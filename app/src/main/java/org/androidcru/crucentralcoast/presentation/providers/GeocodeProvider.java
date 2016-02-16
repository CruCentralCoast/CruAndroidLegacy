package org.androidcru.crucentralcoast.presentation.providers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import rx.Observable;
import rx.schedulers.Schedulers;

public class GeocodeProvider
{
    public static Observable<Address> getLatLng(Context context, String location)
    {
        if(Geocoder.isPresent())
        {
            Geocoder geocoder = new Geocoder(context);
            try
            {
                return Observable.from(geocoder.getFromLocationName(location, 1)).subscribeOn(Schedulers.io());
            }
            catch (IOException e)
            {
                return Observable.empty();
            }
        }
        else
        {
            Logger.e("Geocoder not implemented on this device!");
            return Observable.empty();
        }
    }
}

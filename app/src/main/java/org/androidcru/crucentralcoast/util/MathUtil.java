package org.androidcru.crucentralcoast.util;

public class MathUtil
{
    private static final double MILE_METER_CONV = 1609.34;
    private static final double METER_MILE_CONV = 0.000621371192;
    private static final double RADIUS_EARTH = 6378000;

    public static double convertMeterToMiles(double meters)
    {
        return meters * METER_MILE_CONV;
    }

    public static double convertMilesToMeters(double miles)
    {

        return miles * MILE_METER_CONV;
    }

    public static double addMetersToLatitude(double originalLatitude, double meters)
    {
        return originalLatitude  + (meters / RADIUS_EARTH) * (180 / Math.PI);
    }
}

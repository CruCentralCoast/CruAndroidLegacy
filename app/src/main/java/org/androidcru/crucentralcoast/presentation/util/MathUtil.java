package org.androidcru.crucentralcoast.presentation.util;

public class MathUtil
{
    private static final double MILE_METER_CONV = 1609.34;

    public static double convertMilesToMeters(double miles)
    {

        return miles * MILE_METER_CONV;
    }
}

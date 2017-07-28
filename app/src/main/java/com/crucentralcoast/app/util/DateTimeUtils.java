package com.crucentralcoast.app.util;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

public class DateTimeUtils
{
    public static boolean within(ZonedDateTime original, ZonedDateTime target, int days, int hours)
    {
        return Math.abs(ChronoUnit.DAYS.between(original, target)) <= days && Math.abs(ChronoUnit.HOURS.between(original, target)) <= hours;
    }


}

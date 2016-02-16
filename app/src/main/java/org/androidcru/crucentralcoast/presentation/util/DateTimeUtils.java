package org.androidcru.crucentralcoast.presentation.util;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

public class DateTimeUtils
{
    public static boolean within(ZonedDateTime original, ZonedDateTime target, int days, int hours)
    {
        return ChronoUnit.DAYS.between(original, target) <= days && ChronoUnit.HOURS.between(original, target) <= hours;
    }
}

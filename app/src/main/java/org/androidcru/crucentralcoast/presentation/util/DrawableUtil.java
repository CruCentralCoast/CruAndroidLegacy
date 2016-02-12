package org.androidcru.crucentralcoast.presentation.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableUtil
{
    public static Drawable getTintedDrawable(Context context, int drawableId, int colorId)
    {
        Drawable coloredCal = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableId));
        DrawableCompat.setTint(coloredCal, ContextCompat.getColor(context, colorId));
        return coloredCal;
    }

    public static Drawable getTintListedDrawable(Context context, int drawableId, int tintListId)
    {
        Drawable coloredCal = DrawableCompat.wrap(ContextCompat.getDrawable(context, drawableId));
        DrawableCompat.setTintList(coloredCal, ContextCompat.getColorStateList(context, tintListId));
        return coloredCal;
    }

    public static Drawable getTintedDrawable(Context context, Drawable drawable, int color)
    {
        Drawable coloredCal = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(coloredCal, color);
        return coloredCal;
    }

    public static Drawable getTintListedDrawable(Context context, Drawable drawable, ColorStateList tintList)
    {
        Drawable coloredCal = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(coloredCal, tintList);
        return coloredCal;
    }
}

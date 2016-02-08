package org.androidcru.crucentralcoast.presentation.util;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.androidcru.crucentralcoast.R;

@SuppressWarnings("unused")
public class BindingAdapters
{
    @BindingAdapter("bind:src")
    public static void setSource(ImageView view, String url)
    {
        if(url.isEmpty())
        {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.logo_grey));
        }
        else
        {
            Picasso.with(view.getContext())
                    .load(url)
                    .placeholder(R.drawable.logo_grey)
                    .fit()
                    .into(view);
        }
    }

    @BindingAdapter({"android:src", "bind:tint"})
    public static void setTintedSource(ImageButton view, Drawable drawable, int tintColor)
    {
        view.setImageDrawable(DrawableUtil.getTintedDrawable(view.getContext(), drawable, tintColor));
    }

    @BindingAdapter({"bind:selected", "bind:selectedDrawable", "bind:unselectedDrawable", "bind:selectionTint"})
    public static void setSelected(ImageView view, boolean selected, Drawable selectedDrawable, Drawable unselectedDrawable, ColorStateList selectionTint)
    {
        view.setSelected(selected);
        view.setImageDrawable(DrawableUtil.getTintListedDrawable(view.getContext(), selected ? selectedDrawable : unselectedDrawable, selectionTint));
    }
}

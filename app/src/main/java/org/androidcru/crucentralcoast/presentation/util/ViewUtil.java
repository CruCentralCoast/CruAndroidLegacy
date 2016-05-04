package org.androidcru.crucentralcoast.presentation.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;

import java.util.WeakHashMap;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

public class ViewUtil
{

    private static WeakHashMap<String, Typeface> fontCache = new WeakHashMap<>();
    private static CustomTabsIntent intent;
    private static final String fontsDir = "fonts/";


    public static void setFont(TextView view, String fontFileName)
    {
        if(!fontCache.containsKey(fontFileName))
        {
            Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), fontsDir + fontFileName);
            view.setTypeface(fontCache.put(fontFileName, typeface));
        }
        view.setTypeface(fontCache.get(fontFileName));
    }

    public static void setSource(ImageView view, String url, SCALE_TYPE scaleType)
    {
        setSource(view, url, 0, null, null);
    }

    public static void setSource(ImageView view, String url, int tintColor, SCALE_TYPE scaleType)
    {
        setSource(view, url, tintColor, null, null);
    }

    public static void setSource(ImageView view, String url, int tintColor, Drawable placeholder, SCALE_TYPE scaleType)
    {
        if(url == null || url.isEmpty())
        {
            view.setImageDrawable(placeholder != null ? placeholder : DrawableUtil.getDrawable(view.getContext(), R.drawable.cru_logo_grey600));
        }
        else
        {
            RequestCreator request = Picasso.with(view.getContext()).load(url);

            if(scaleType != null)
            {
                switch (scaleType)
                {
                    case FIT:
                        request.fit();
                        break;
                    case CENTER_INSIDE:
                        request.centerInside();
                        break;
                    case CENTER_CROP:
                        request.centerCrop();
                        break;
                }
            }

            if(placeholder != null)
                request.placeholder(placeholder);

            if(tintColor != 0)
                request.transform(new ColorFilterTransformation(tintColor));

            request.into(view);
        }
    }

    public enum SCALE_TYPE {
        FIT, CENTER_INSIDE, CENTER_CROP;
    }


    public static void setSelected(ImageView view, boolean selected, Drawable selectedDrawable, Drawable unselectedDrawable, ColorStateList colorStateList)
    {
        view.setSelected(selected);
        view.setImageDrawable(DrawableUtil.getTintListedDrawable(view.getContext(), selected ? selectedDrawable : unselectedDrawable, colorStateList));
    }

    public static void setSelected(ImageView view, boolean selected, int selectedDrawableId, int unselectedDrawableId, int colorStateListId)
    {
        Drawable selectedDrawable = DrawableUtil.getDrawable(view.getContext(), selectedDrawableId);
        Drawable unselectedDrawable = DrawableUtil.getDrawable(view.getContext(), unselectedDrawableId);
        ColorStateList colorStateList = ContextCompat.getColorStateList(view.getContext(), colorStateListId);

        setSelected(view, selected, selectedDrawable, unselectedDrawable, colorStateList);
    }

    public static void setSelected(ImageView view, boolean selected, int drawableId, int colorStateListId)
    {
        setSelected(view, selected, drawableId, drawableId, colorStateListId);
    }


    public static void setSpinner(Spinner spinner, String[] resources, AdapterView.OnItemSelectedListener onItemSelected, int index)
    {
        Context context = spinner.getContext();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item,
                resources);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(index);
        if(onItemSelected != null)
            spinner.setOnItemSelectedListener(onItemSelected);
    }

    public static CustomTabsIntent getCustomTabsIntent(Context context) {
        if(intent == null)
        {

            CustomTabsIntent.Builder customTabsIntentBuilder = new CustomTabsIntent.Builder();

            int color = ContextCompat.getColor(CruApplication.getContext(), R.color.colorPrimary);
            customTabsIntentBuilder.setToolbarColor(color);

            customTabsIntentBuilder.setShowTitle(true);

            intent = customTabsIntentBuilder.build();
        }
        return intent;
    }
}

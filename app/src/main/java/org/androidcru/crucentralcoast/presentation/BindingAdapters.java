package org.androidcru.crucentralcoast.presentation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;

import java.util.WeakHashMap;

import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

@SuppressWarnings("unused")
public class BindingAdapters
{

    private static WeakHashMap<String, Typeface> fontCache = new WeakHashMap<>();
    private static final String fontsDir = "fonts/";

    @BindingAdapter("bind:font")
    public static void setFont(TextView view, String fontFileName)
    {
        if(!fontCache.containsKey(fontFileName))
        {
            Typeface typeface = Typeface.createFromAsset(view.getContext().getAssets(), fontsDir + fontFileName);
            view.setTypeface(fontCache.put(fontFileName, typeface));
        }
        view.setTypeface(fontCache.get(fontFileName));
    }


    @BindingAdapter({"android:src", "bind:tint"})
    public static void setTintedSource(ImageButton view, Drawable drawable, int tintColor)
    {
        view.setImageDrawable(DrawableUtil.getTintedDrawable(view.getContext(), drawable, tintColor));
    }

    @BindingAdapter(value = {"bind:src", "bind:tint", "bind:placeholder", "bind:scaleType"}, requireAll = false)
    public static void setSource(ImageView view, String url, int tintColor, Drawable placeholder, String scaleType)
    {
        if(url == null || url.isEmpty())
        {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.logo_grey));
        }
        else
        {
            RequestCreator request = Picasso.with(view.getContext()).load(url);

            if(scaleType != null)
            {
                switch (scaleType)
                {
                    case "fit":
                        request.fit();
                        break;
                    case "centerInside":
                        request.centerInside();
                        break;
                    case "centerCrop":
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

    @BindingAdapter({"bind:selected", "bind:selectedDrawable", "bind:unselectedDrawable", "bind:selectionTint"})
    public static void setSelected(ImageView view, boolean selected, Drawable selectedDrawable, Drawable unselectedDrawable, ColorStateList selectionTint)
    {
        view.setSelected(selected);
        view.setImageDrawable(DrawableUtil.getTintListedDrawable(view.getContext(), selected ? selectedDrawable : unselectedDrawable, selectionTint));
    }

    @BindingAdapter({"bind:resources", "bind:onItemSelected"})
    public static void setSpinner(Spinner spinner, String[] resources, AdapterView.OnItemSelectedListener onItemSelected)
    {
        Context context = spinner.getContext();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item,
                resources);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(onItemSelected);
    }

    @BindingAdapter("bind:onKey")
    public static void setKeyListener(EditText editText, KeyListener keyListener)
    {
        editText.setKeyListener(keyListener);
    }

    @BindingAdapter("bind:textWatcher")
    public static void setKeyListener(EditText editText, TextWatcher textWatcher)
    {
        editText.addTextChangedListener(textWatcher);
    }


}

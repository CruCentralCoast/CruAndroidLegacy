package org.androidcru.crucentralcoast.presentation.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
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

    public static Typeface getFont(Context context, String fontFileName)
    {
        if(!fontCache.containsKey(fontFileName))
        {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontsDir + fontFileName);
            return fontCache.put(fontFileName, typeface);
        }
        else
            return fontCache.get(fontFileName);
    }

    public static void setSource(ImageView view, String url, int tintColor, Drawable placeholder, Drawable error, SCALE_TYPE scaleType)
    {
        //Picasso can handle null images (uses placeholder image), but throws exception on empty
        if (url != null && url.isEmpty())
            url = null;

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

        //Set a placeholder image
        if(placeholder == null)
            request.placeholder(DrawableUtil.getDrawable(view.getContext(), R.drawable.cru_logo_grey600));
        else
            request.placeholder(placeholder);

        //Set an image in case of network error
        if(error == null)
            request.placeholder(DrawableUtil.getDrawable(view.getContext(), R.drawable.cru_logo_grey600));
        else
            request.placeholder(error);

        if(tintColor != 0)
            request.transform(new ColorFilterTransformation(tintColor));

        request.into(view);
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
        if(onItemSelected != null)
            spinner.setOnItemSelectedListener(onItemSelected);
        spinner.setSelection(index);
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

    public static Intent insertOrEditContact(String name, String phone)
    {
        // Creates a new Intent to insert or edit a contact
        Intent intentInsertEdit = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        // Sets the MIME type
        intentInsertEdit.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        intentInsertEdit.putExtra(ContactsContract.Intents.Insert.PHONE, phone)
               .putExtra(ContactsContract.Intents.Insert.NAME, name);
        return intentInsertEdit;
    }
}

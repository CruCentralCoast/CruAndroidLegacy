package org.androidcru.crucentralcoast.presentation.customviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.util.DisplayMetricsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CruSupportPlaceAutocompleteFragment extends SupportPlaceAutocompleteFragment
{
    private Unbinder unbinder;

    @BindView(com.google.android.gms.R.id.place_autocomplete_search_button) ImageButton searchButton;
    @BindView(com.google.android.gms.R.id.place_autocomplete_search_input) public EditText editText;
    @BindView(com.google.android.gms.R.id.place_autocomplete_clear_button) ImageButton clear;
    public Place place;
    private PlaceSelectionListener listener;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        searchButton.setAdjustViewBounds(true);
        searchButton.setImageDrawable(DrawableUtil.getDrawable(getContext(), R.drawable.ic_map_marker_grey600));
        int dp48 = DisplayMetricsUtil.dpToPx(getContext(), 48);
        searchButton.getLayoutParams().width = dp48;
        searchButton.getLayoutParams().height = dp48;
        int dp8 = DisplayMetricsUtil.dpToPx(getContext(), 8);
        searchButton.setPadding(dp8, dp8, dp8, dp8);

        editText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.grey600));

        view.requestLayout();
        view.invalidate();
    }

    public boolean validate()
    {
        boolean result = !editText.getText().toString().isEmpty();
        if(!result)
            editText.setError("Enter a location");
        else
            editText.setError(null);
        getView().requestLayout();
        getView().invalidate();
        return result;
    }

    @Override
    public void setOnPlaceSelectedListener(PlaceSelectionListener listener)
    {
        super.setOnPlaceSelectedListener(listener);
        this.listener = listener;
    }

    public void restore(String text, Place p)
    {
        place = p;
        listener.onPlaceSelected(p);
        editText.setText(text);
        clear.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        place = PlaceAutocomplete.getPlace(getContext(), data);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }
}

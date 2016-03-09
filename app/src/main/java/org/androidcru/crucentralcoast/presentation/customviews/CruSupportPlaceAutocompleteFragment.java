package org.androidcru.crucentralcoast.presentation.customviews;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.util.DisplayMetricsUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CruSupportPlaceAutocompleteFragment extends SupportPlaceAutocompleteFragment
{
    @Bind(com.google.android.gms.R.id.place_autocomplete_search_button) AppCompatImageButton searchButton;
    @Bind(com.google.android.gms.R.id.place_autocomplete_search_input) AppCompatEditText editText;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        searchButton.setAdjustViewBounds(true);
        searchButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_map_marker_grey600_48dp));
        int dp48 = DisplayMetricsUtil.dpToPx(getContext(), 48);
        searchButton.getLayoutParams().width = dp48;
        searchButton.getLayoutParams().height = dp48;
        int dp8 = DisplayMetricsUtil.dpToPx(getContext(), 8);
        searchButton.setPadding(dp8, dp8, dp8, dp8);

        editText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.grey600));

        view.requestLayout();
        view.invalidate();
    }
}

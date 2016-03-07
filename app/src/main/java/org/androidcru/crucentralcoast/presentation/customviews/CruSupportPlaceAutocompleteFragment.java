package org.androidcru.crucentralcoast.presentation.customviews;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import org.androidcru.crucentralcoast.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CruSupportPlaceAutocompleteFragment extends SupportPlaceAutocompleteFragment
{
    @Bind(com.google.android.gms.R.id.place_autocomplete_search_button) ImageButton searchButton;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        searchButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_map_marker_grey600_36dp));
    }
}

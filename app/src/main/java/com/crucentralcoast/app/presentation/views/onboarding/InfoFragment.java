package com.crucentralcoast.app.presentation.views.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by brittanyberlanga on 3/20/17.
 */

public class InfoFragment extends Fragment {
    public static final String CRU_INFO_EXTRA = "cru_info";
    public static final String TITLE_ID_EXTRA = "title_id_extra";
    public static final String DESC_ID_EXTRA = "desc_id_extra";
    public static final String IMG_ID_EXTRA = "img_id_extra";

    @BindView(R.id.title)
    protected TextView titleText;
    @BindView(R.id.description)
    protected TextView descriptionText;
    @BindView(R.id.image)
    protected ImageView imageView;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view;
        if (getArguments() != null && getArguments().getBoolean(CRU_INFO_EXTRA, false)) {
            view = inflater.inflate(R.layout.fragment_cru_info, container, false);
            unbinder = ButterKnife.bind(this, view);
        }
        else {
            view = inflater.inflate(R.layout.fragment_template_info, container, false);
            unbinder = ButterKnife.bind(this, view);
            Bundle args = getArguments();
            titleText.setText(getResources().getText(args.getInt(TITLE_ID_EXTRA)));
            descriptionText.setText(getResources().getText(args.getInt(DESC_ID_EXTRA)));
            imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), args.getInt(IMG_ID_EXTRA)));
        }
        ViewUtil.setFont(titleText, AppConstants.FREIG_SAN_PRO_LIGHT);
        ViewUtil.setFont(descriptionText, AppConstants.FREIG_SAN_PRO_LIGHT);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

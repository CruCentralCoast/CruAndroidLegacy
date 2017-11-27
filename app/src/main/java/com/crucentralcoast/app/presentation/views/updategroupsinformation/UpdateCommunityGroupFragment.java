package com.crucentralcoast.app.presentation.views.updategroupsinformation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.providers.CommunityGroupProvider;

import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;
import com.crucentralcoast.app.presentation.views.base.ListFragment;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observer;
import rx.observers.Observers;
import timber.log.Timber;


/**
 * Created by Dylan on 11/25/2017.
 */

public class UpdateCommunityGroupFragment extends BaseSupportFragment {

    private static String communityGroupID;
    private static CommunityGroup communityGroup;
    public static Observer<CommunityGroup> communityGroupObserver;

    @BindView(R.id.update_community_group_button)
    protected Button updateButton;
    @BindView(R.id.update_community_group_cancel_button)
    protected Button cancelButton;
    @BindView(R.id.update_day_of_week_field)
    protected Spinner dayOfWeek;
    @BindView(R.id.update_description_field)
    protected EditText description;
    @BindView(R.id.update_group_type)
    protected Spinner genderSpinner;
    @BindView(R.id.update_name_field)
    protected EditText groupName;

    private Unbinder unbinder;

    public UpdateCommunityGroupFragment newInstance() {
        return new UpdateCommunityGroupFragment();
    }

    public UpdateCommunityGroupFragment(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupCommunityGroupObserver();

        if (!getArguments().isEmpty())
            communityGroupID = getArguments().getString("groupID");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_update_community_group, container, false);
        CommunityGroupProvider.getCommunityGroup(UpdateCommunityGroupFragment.this, communityGroupObserver, communityGroupID);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("in view created");

        ViewUtil.setFont(cancelButton, AppConstants.FREIG_SAN_PRO_LIGHT);
        ViewUtil.setFont(updateButton, AppConstants.FREIG_SAN_PRO_LIGHT);


        if (communityGroup == null) {
            System.out.println("here");
        }


    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }

    public void setupCommunityGroupObserver() {
        communityGroupObserver = new Observer<CommunityGroup>() {
            @Override
            public void onCompleted() {
                Timber.d("");
            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e, "Failed to retrieve Community Group");
            }

            @Override
            public void onNext(CommunityGroup retrievedCommunityGroup) {
                communityGroup = retrievedCommunityGroup;
            }
        };

    }
}




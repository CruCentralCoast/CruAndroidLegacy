package com.crucentralcoast.app.presentation.views.updategroupsinformation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.providers.CommunityGroupProvider;

import com.crucentralcoast.app.data.providers.UpdateGroupsInformationProvider;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;
import com.crucentralcoast.app.presentation.views.base.ListFragment;
import com.crucentralcoast.app.presentation.views.settings.CreateAccountActivity;


import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
    @BindView(R.id.update_name_field)
    protected EditText groupName;
    @BindView(R.id.update_description_field)
    protected EditText description;
    @BindView(R.id.update_day_of_week_field)
    protected Spinner dayOfWeekSpinner;
    @BindView(R.id.update_group_type)
    protected Spinner genderSpinner;

    private static String cgID;
    private static String cgMinistry;
    private static String cgName;
    private static String cgDescription;
    private static ZonedDateTime cgMeetingTime;
    private static DayOfWeek cgDayOfWeek;
    private static ArrayList<String> cgLeaders;
    private static int cgGender;

    private Unbinder unbinder;
    private ArrayAdapter<String> dayOfWeekAdapter;
    private ArrayAdapter<String> genderAdapter;

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
        unbinder = ButterKnife.bind(this, view);
        dayOfWeekAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.days_of_week));
        genderAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.genders));
//        {
//            @Override
//            public boolean isEnabled(int position) {
//                if (position == 0) {
//                    return false;
//                }
//                else {
//                    return true;
//                }
//            }
//
//            @Override
//            public View getDropDownView(int position, View convertView, ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if(position==0) {
//                    tv.setTextColor(getResources().getColor(R.color.grey600));
//                }
//                else {
//                    tv.setTextColor(getResources().getColor(android.R.color.black));
//                }
//                return view;
//            }
//        }
        ;

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("in view created");


        CommunityGroupProvider.getCommunityGroup(UpdateCommunityGroupFragment.this, communityGroupObserver, communityGroupID);
        ViewUtil.setFont(cancelButton, AppConstants.FREIG_SAN_PRO_LIGHT);
        ViewUtil.setFont(updateButton, AppConstants.FREIG_SAN_PRO_LIGHT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setupCommunityGroupObserver() {
        communityGroupObserver = new Observer<CommunityGroup>() {
            @Override
            public void onCompleted() {
                Timber.d("");
                setFieldText();
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

    private void setFieldText() {
        int spinnerPosition;
        cgID = communityGroupID;
        cgMinistry = communityGroup.ministry;
        cgName = communityGroup.name;
        cgDescription = communityGroup.description;
        cgMeetingTime = communityGroup.meetingTime;
        cgDayOfWeek = communityGroup.dayOfWeek;
        cgGender = communityGroup.gender;

        groupName.setText(cgName);
        description.setText(cgDescription);
        dayOfWeekSpinner.setAdapter(dayOfWeekAdapter);
        genderSpinner.setAdapter(genderAdapter);

        if(!cgDayOfWeek.equals(null)) {
            spinnerPosition = dayOfWeekAdapter.getPosition(cgDayOfWeek.toString());
            dayOfWeekSpinner.setSelection(spinnerPosition);
        }
        if (cgGender == 0 || cgGender == 1) {
            spinnerPosition = genderAdapter.getPosition(String.valueOf(cgGender));
            genderSpinner.setSelection(spinnerPosition);
        }


    }


    @OnClick(R.id.update_community_group_button)
    public void onClickUpdateCommunityGroupButton() {
//        if(validateUpdateFields())
        String cgNameString = groupName.getText().toString();
        String cgDescriptionString = description.getText().toString();


        CommunityGroup updateCommunityGroup = new CommunityGroup(cgID, cgMinistry, cgNameString, cgDescriptionString, cgMeetingTime, cgDayOfWeek, cgGender);
        System.out.println(cgName);
        UpdateGroupsInformationProvider.updateCommunityGroup(communityGroupID,  updateCommunityGroup)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            getActivity().finish();
                            Toast.makeText(getActivity(), getString(R.string.create_account_success), Toast.LENGTH_LONG).show();
                        },
                        error -> {
                            Timber.e(error);
                        }
                );
    }

    @OnClick(R.id.update_community_group_cancel_button)
    public void onCLickUpdateCommunityGroupCancelButton() {
        createAlertDialog(getString(R.string.create_account_cancel), getString(R.string.create_account_cancel_message), getString(R.string.alert_dialog_yes), getString(R.string.alert_dialog_no),
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();

                    }
                },
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );

    }

    public void createAlertDialog (String title, String message, String postiveText, String negativeText, DialogInterface.OnClickListener positveDialogListener, DialogInterface.OnClickListener negativeDialogListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);

        if (negativeText != null) {
            alertBuilder.setNegativeButton(negativeText,
                    negativeDialogListener);
        }
        alertBuilder.setPositiveButton( postiveText,
                positveDialogListener);

        alertBuilder.show();
    }


}




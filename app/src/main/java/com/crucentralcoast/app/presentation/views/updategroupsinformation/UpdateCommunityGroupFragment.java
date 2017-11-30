package com.crucentralcoast.app.presentation.views.updategroupsinformation;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.squareup.picasso.Picasso;


import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Observers;
import timber.log.Timber;


/**
 * Created by Dylan on 11/25/2017.
 */

public class UpdateCommunityGroupFragment extends BaseSupportFragment {

    private static String communityGroupID;
    private static CommunityGroup communityGroup = null;
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
    @BindView(R.id.update_group_type_field)
    protected Spinner typeSpinner;
    @BindView(R.id.update_group_title_field)
    protected TextView groupTitle;
//    @BindView(R.id.update_meeting_time_field)
//    protected Spinner meetingTimeSpiner;

    private static String cgID;
    private static String cgMinistry;
    private static String cgName;
    private static String cgDescription;
    private static ZonedDateTime cgMeetingTime;
    private static DayOfWeek cgDayOfWeek;
    private static ArrayList<CruUser> cgLeaders;
    private static int cgGender;
    private static String cgType;

    private Unbinder unbinder;
    private ArrayAdapter<String> dayOfWeekAdapter;
    private ArrayAdapter<String> typeAdapter;
//    private ArrayAdapter<String> meetingTimeAdapter;


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
        dayOfWeekAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_update_group_item, getResources().getStringArray(R.array.days_of_week));
        typeAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_update_group_item, getResources().getStringArray(R.array.community_group_types));
//        meetingTimeAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_update_group_item, getResources().getStringArray(R.array.meeting_time_types));
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
//        cgMeetingTime = communityGroup.meetingTime;
        cgDayOfWeek = communityGroup.dayOfWeek;
        cgType = communityGroup.type;
        cgLeaders = communityGroup.leaders;

        try {
            groupTitle.setText(getGroupTitle(cgLeaders, cgName));
        }
        catch (Exception e){
            Timber.e(e, e.getMessage());

        }


        groupName.setText(cgName);
        description.setText(cgDescription);
        dayOfWeekSpinner.setAdapter(dayOfWeekAdapter);
        typeSpinner.setAdapter(typeAdapter);
//        meetingTimeSpiner.setAdapter(meetingTimeAdapter);

        if(!cgDayOfWeek.equals(null)) {
            spinnerPosition = dayOfWeekAdapter.getPosition(getDayFromObjectAsString(cgDayOfWeek));
            dayOfWeekSpinner.setSelection(spinnerPosition);
        }
        if(!cgType.equals(null)) {
            spinnerPosition = typeAdapter.getPosition(cgType);
            typeSpinner.setSelection(spinnerPosition);
        }

//
//        System.out.println("time is: " + cgMeetingTime.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMAT)));
//        if(!cgMeetingTime.equals(null)) {
////            spinnerPosition = meetingTimeAdapter.getPosition
//            try {
//                getMeetingTimeFromObjectAsString(cgMeetingTime);
//            }
//            catch (Exception e) {
//                Timber.e("errorrrrr: ", e.toString());
//            }
//        }
    }

    @OnClick(R.id.update_community_group_button)
    public void onClickUpdateCommunityGroupButton() {
        String cgNameString = groupName.getText().toString();
        String cgDescriptionString = description.getText().toString();
        DayOfWeek cgDayOfWeek = getDayOfWeek();
        String cgType = typeSpinner.getSelectedItem().toString();
//        ZonedDateTime cgMeetingTime = getMeetingTimeFromObjectAsString(meetingTimeSpiner.getSelectedItem().toString());
        System.out.println("cg type: " + cgType);

        CommunityGroup updatedCommunityGroup = new CommunityGroup(cgID, cgMinistry, cgNameString, cgDescriptionString, cgMeetingTime, cgDayOfWeek, cgType, cgLeaders);
        UpdateGroupsInformationProvider.updateCommunityGroup(communityGroupID,  updatedCommunityGroup)
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

    private String getDayFromObjectAsString(DayOfWeek dayOfWeek) {
        String day = dayOfWeek.name();
        day = day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
        return day;
    }

//    private void getMeetingTimeFromObjectAsString(ZonedDateTime cgMeetingTime) {
//
//        System.out.println("the time: " +  cgMeetingTime.format(DateTimeFormatter.ofPattern(AppConstants.TIME_FORMAT)));
////        return time;
//    }

    private DayOfWeek getDayOfWeek() {
        // adds 1 because DayOfWeek Enum starts at 1 while spinner array starts at 0
        return DayOfWeek.of(dayOfWeekSpinner.getSelectedItemPosition() + 1);
    }

    private String getGroupTitle(ArrayList<CruUser>cgLeaders, String cgName) {
        String title;
        title = "Updating ";
        if(!cgLeaders.isEmpty()){

            for (int i = 0; i < cgLeaders.size(); i++) {

                title += cgLeaders.get(i).name.firstName;
                title += cgLeaders.size() > 1? " " : "";
            }
            title += "'s Community Group";


        }
        else if(!cgName.isEmpty()) {
            title += cgName;
        }
        System.out.println("title is: " + title);
        return title;
    }

    public boolean isCGLeader(CruUser accessUser) {
        boolean isCGLeader = false;
        for(CruUser user: cgLeaders ) {
            if(accessUser.id.equals(user.id)) {
                isCGLeader = true;
                break;
            }
        }
        return isCGLeader;
    }


}




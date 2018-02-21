package com.crucentralcoast.app.presentation.views.updategroupsinformation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.models.MinistryTeam;
import com.crucentralcoast.app.data.providers.CommunityGroupProvider;
import com.crucentralcoast.app.data.providers.MinistryTeamProvider;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Dylan on 2/15/18.
 */

//USE S3 CONTAINER PHOTOS

public class UpdateMinistryTeamFragment extends BaseSupportFragment {

   @BindView(R.id.update_community_group_button)
   protected Button updateButton;
   @BindView(R.id.update_community_group_cancel_button)
   protected Button cancelButton;
   @BindView(R.id.update_group_title_field)
   protected TextView groupTitle;


   private static String ministryTeamID;
   private static MinistryTeam ministryTeam = null;
   public static Observer<MinistryTeam> ministryTeamObserver;



   private CompositeSubscription compSub;

   public UpdateMinistryTeamFragment newInstance() {
      return new UpdateMinistryTeamFragment();
   }

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      compSub = new CompositeSubscription();
      setupMinistryTeamObserver();
      if (!getArguments().isEmpty())
         ministryTeamID = getArguments().getString("groupID");
   }

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      View view =  inflater.inflate(R.layout.fragment_update_community_group, container, false);
      unbinder = ButterKnife.bind(this, view);
//      dayOfWeekAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_update_group_item, getResources().getStringArray(R.array.days_of_week));
//      typeAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_update_group_item, getResources().getStringArray(R.array.community_group_types));
////        meetingTimeAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_update_group_item, getResources().getStringArray(R.array.meeting_time_types));
      return view;
   }

   @Override
   public void onViewCreated(View view, Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      System.out.println("in view created");


//      CommunityGroupProvider.g(UpdateMinistryTeamFragment.this, ministryTeamObserver, ministryTeamID);

      MinistryTeamProvider.getMinistryTeam(ministryTeamID);
      ViewUtil.setFont(cancelButton, AppConstants.FREIG_SAN_PRO_LIGHT);
      ViewUtil.setFont(updateButton, AppConstants.FREIG_SAN_PRO_LIGHT);
      groupTitle.setText("Updating " + ministryTeam.name);
   }


   public void setupMinistryTeamObserver() {
      ministryTeamObserver = new Observer<MinistryTeam>() {
         @Override
         public void onCompleted() {
            Timber.d("");

            //setFieldText();
         }

         @Override
         public void onError(Throwable e) {
            Timber.e(e, e.toString());
         }

         @Override
         public void onNext(MinistryTeam retrievedCommunityGroup) {
            ministryTeam = retrievedCommunityGroup;

         }
      };

   }
}

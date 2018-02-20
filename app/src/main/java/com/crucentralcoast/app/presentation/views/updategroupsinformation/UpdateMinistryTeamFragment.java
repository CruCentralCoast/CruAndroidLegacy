package com.crucentralcoast.app.presentation.views.updategroupsinformation;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.MinistryTeam;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;

import rx.Observer;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by Dylan on 2/15/18.
 */

public class UpdateMinistryTeamFragment extends BaseSupportFragment {

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

      System.out.println("ministry team id: " + ministryTeamID);

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

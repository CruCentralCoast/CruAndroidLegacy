package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.CreateAccount;
import com.crucentralcoast.app.data.models.MinistryTeam;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;

import rx.Observable;
import rx.Observer;
import rx.observers.Observers;

/**
 * Created by Dylan on 11/25/2017.
 */

public class UpdateGroupsInformationProvider{

    private static CruApiService cruApiService = CruApiProvider.getService();

    public static Observable<CommunityGroup> updateCommunityGroup(String groupID, CommunityGroup communityGroup) {
        return cruApiService.updateCommunityGroup(groupID, communityGroup);
    }


//    public static Observable<MinistryTeam> updateMinistryTeam(MinistryTeam ministryTeam) {
//        return cruApiService.updateMinistryTeam(ministryTeam);
//    }

}

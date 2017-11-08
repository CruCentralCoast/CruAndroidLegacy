package com.crucentralcoast.app.data.providers;

import com.crucentralcoast.app.data.models.CreateAccount;
import com.crucentralcoast.app.data.models.CruName;
import com.crucentralcoast.app.data.models.CruUser;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.providers.api.CruApiProvider;
import com.crucentralcoast.app.data.providers.util.RxComposeUtil;
import com.crucentralcoast.app.data.services.CruApiService;

import rx.Observable;
import rx.Observer;

/**
 * Created by Dylan Sun on 11/4/17.
 */

public class CreateAccountProvider {

   private static CruApiService cruApiService = CruApiProvider.getService();

   public static Observable<CreateAccount> createNewUser(CreateAccount userAccount) {
      return cruApiService.createNewUser(userAccount);
   }

}



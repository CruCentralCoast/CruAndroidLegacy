package com.crucentralcoast.app.presentation.views.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.util.ViewUtil;
import com.crucentralcoast.app.presentation.views.subscriptions.SubscriptionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dylan Sun on 10/7/17.
 */

public class TermsAndConditionsActivity extends AppCompatActivity {
   @BindView(R.id.accept_button)
   protected Button acceptButton;
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState)
   {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_terms_and_conditions);
      ButterKnife.bind(this);

      ViewUtil.setFont(acceptButton, AppConstants.FREIG_SAN_PRO_LIGHT);
      TextView infoView = findViewById(R.id.terms_and_conditions_text_view);
      TextView titleView = findViewById(R.id.service_and_condition_title);
      ViewUtil.setFont(titleView, AppConstants.FREIG_SAN_PRO_MEDIUM);
      ViewUtil.setFont(infoView, AppConstants.FREIG_SAN_PRO_LIGHT);
      titleView.setText("Terms of Agreement");
      infoView.setText(getString(R.string.terms_and_conditions));


   }

   @OnClick(R.id.accept_button)
   public void continueToSubscriptions() {
        Intent intent = new Intent(this, SubscriptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
   }

}

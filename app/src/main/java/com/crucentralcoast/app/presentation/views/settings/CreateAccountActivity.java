package com.crucentralcoast.app.presentation.views.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.crucentralcoast.app.AppConstants;
import com.crucentralcoast.app.R;
import com.crucentralcoast.app.presentation.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dylan on 10/16/17.
 */

public class CreateAccountActivity extends AppCompatActivity {
   @BindView(R.id.cancel_button)
   protected Button cancelButton;
   @BindView(R.id.create_account_button)
   protected Button createButton;


   private String FirstName;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_create_account);
      ButterKnife.bind(this);

      ViewUtil.setFont(cancelButton, AppConstants.FREIG_SAN_PRO_LIGHT);
      ViewUtil.setFont(createButton, AppConstants.FREIG_SAN_PRO_LIGHT);

   }

   @OnClick(R.id.create_account_button)
   public void onClickCreateAccountButton() {
      System.out.println("here");

   }

}

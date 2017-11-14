package com.crucentralcoast.app.presentation.views.settings;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.crucentralcoast.app.data.models.CreateAccount;
import com.crucentralcoast.app.data.models.CruName;
import com.crucentralcoast.app.data.providers.CreateAccountProvider;
import com.crucentralcoast.app.presentation.util.ViewUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by Dylan on 10/16/17.
 */

public class CreateAccountActivity extends AppCompatActivity {
   @BindView(R.id.create_account_cancel_button)
   protected Button cancelButton;
   @BindView(R.id.create_account_button)
   protected Button createButton;
   @BindView(R.id.create_account_first_name_field)
   protected EditText firstName;
   @BindView(R.id.create_account_last_name_field)
   protected EditText lastName;
   @BindView(R.id.create_account_gender)
   protected Spinner genderSpinner;
   @BindView(R.id.create_account_phone_number_field)
   protected EditText phoneNumber;
   @BindView(R.id.create_account_email_field)
   protected EditText email;
   @BindView(R.id.create_account_password_field)
   protected EditText password;

   static final String male = "Male";
   static final String female = "Female";
   static final String notApplicable = "Not Applicable";

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_create_account);
      ButterKnife.bind(this);

      ViewUtil.setFont(cancelButton, AppConstants.FREIG_SAN_PRO_LIGHT);
      ViewUtil.setFont(createButton, AppConstants.FREIG_SAN_PRO_LIGHT);

      ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.gender_list)) {
         @Override
         public boolean isEnabled(int position) {
            if (position == 0) {
               return false;
            } else {
               return true;
            }
         }

         @Override
         public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            if(position==0) {
               tv.setTextColor(getResources().getColor(R.color.grey600));
            }
            else {
               tv.setTextColor(getResources().getColor(android.R.color.black));
            }
            return view;
         }
      };

      genderSpinner.setAdapter(genderAdapter);
   }

   @OnClick(R.id.create_account_button)
   public void onClickCreateAccountButton() {
      String firstNameString = firstName.getText().toString();
      String lastNameString = lastName.getText().toString();
      String genderString = genderSpinner.getSelectedItem().toString();
      String emailString = email.getText().toString();
      String passwordString = password.getText().toString();
      String phoneNumberString = phoneNumber.getText().toString();

      if (validateAllCreateAccountFields()){

         CruName name = new CruName(firstNameString, lastNameString);
         CreateAccount createAccountObj = new CreateAccount(name, getGender(genderString), phoneNumberString, emailString, passwordString);

         CreateAccountProvider.createNewUser(createAccountObj)
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(
                 response -> {
                     finish();
                     Toast.makeText(this, getString(R.string.create_account_success), Toast.LENGTH_LONG).show();
                 },
                     error -> {
                        Timber.e(error);
                        createAlertDialog(getString(R.string.create_account_failure_title), getString(R.string.create_account_failure_message), getString(R.string.ok), null,
                              null, null);
                     }
               );
      }

   }


   @OnClick(R.id.create_account_cancel_button)
   public void onClickCancelButton() {
      createAlertDialog(getString(R.string.create_account_cancel), getString(R.string.create_account_cancel_message), getString(R.string.alert_dialog_yes), getString(R.string.alert_dialog_no),

      new DialogInterface.OnClickListener(){
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
            finish();
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
      AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CreateAccountActivity.this);
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

   public boolean validateAllCreateAccountFields(){
      boolean isValid = true;
      EditText[] createAccountFields = {firstName, lastName, email, password, phoneNumber};

      for (int fieldPos = 0; fieldPos < createAccountFields.length; fieldPos++) {
         EditText object = createAccountFields[fieldPos];
         if (object.getText().toString().isEmpty()) {
            object.setError(getString(R.string.empty_field_message));
            isValid = false;
         }

         else if(object == email && !isValidEmail(object.getText().toString()) ) {
            email.setError(getString(R.string.invalid_email));
            isValid = false;
         }
         else if (object == password && !isValidPassword(object.getText().toString())) {
            password.setError(getString(R.string.password_error));
            isValid = false;

         }
         else if (object == phoneNumber && !isValidPhoneNumber(object.getText().toString())) {
            phoneNumber.setError(getString(R.string.phone_number_error));
            isValid = false;
         }
      }
      return isValid;
   }


   public static boolean isValidEmail(final String email){
      return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
   }

   public static boolean isValidPhoneNumber(final String phoneNumber) {
      return !TextUtils.isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches();
   }

   public static boolean isValidPassword(final String password) {
      Pattern pattern;
      Matcher matcher;
      final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
      boolean isValid = false;
      if (password.length() > 6) {
         pattern = Pattern.compile(PASSWORD_PATTERN);
         matcher = pattern.matcher(password);
         isValid = matcher.matches();
      }
      return isValid;
   }

   public static int getGender(String genderString) {
      switch(genderString) {
         case male: return 1;
         case female: return 2;
         case notApplicable: return 9;
         default:
            return 0;
      }
   }

}

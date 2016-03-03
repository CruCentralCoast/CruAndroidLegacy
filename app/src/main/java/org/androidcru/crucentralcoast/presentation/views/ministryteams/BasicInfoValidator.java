package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BasicInfoValidator implements Validator.ValidationListener
{
    @Bind(R.id.name_field) @NotEmpty EditText nameField;
    @Bind(R.id.phone_field) @Optional @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;
    @Bind(R.id.email_field) @Optional @Email EditText emailField;

    private Validator validator;
    private boolean isValid;

    private Context context;

    public BasicInfoValidator(View rootView)
    {
        ButterKnife.bind(this, rootView);
        context = rootView.getContext();
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded()
    {
        isValid = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
        isValid = false;
        for (ValidationError e : errors)
        {
            View v = e.getView();
            if (v instanceof Spinner)
            {
                ((TextView) ((Spinner) v).getSelectedView()).setError(e.getCollatedErrorMessage(context));
            }
            else if (v instanceof EditText)
            {
                if(v.getParent() instanceof TextInputLayout)
                {
                    TextInputLayout parent = (TextInputLayout) v.getParent();
                    parent.setError(e.getFailedRules().get(0).getMessage(context));
                }
                else
                {
                    ((EditText) v).setError(e.getFailedRules().get(0).getMessage(context));
                }
            }
        }
    }

    public boolean validate()
    {
        validator.validate(false);
        boolean optionalCheck = !phoneField.getText().equals("") || !emailField.getText().equals("");
        if (!optionalCheck)
        {
            phoneField.setError("Must fill in one of these fields.");
            emailField.setError("Must fill in one of these fields.");
        }
        return isValid && optionalCheck;
    }
}

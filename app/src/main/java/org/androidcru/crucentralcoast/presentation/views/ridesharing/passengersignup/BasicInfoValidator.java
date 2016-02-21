package org.androidcru.crucentralcoast.presentation.views.ridesharing.passengersignup;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.databinding.PassengerFormBasicInfoBinding;

import java.util.List;

public class BasicInfoValidator  implements Validator.ValidationListener
{

    @NotEmpty EditText nameField;
    @NotEmpty @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;

    private Validator validator;
    private boolean isValid;

    private PassengerFormBasicInfoBinding binding;
    private Context context;

    public BasicInfoValidator(PassengerFormBasicInfoBinding binding)
    {
        this.binding = binding;
        bindUI();

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void bindUI()
    {
        nameField = binding.nameField;
        phoneField = binding.phoneField;

        context = nameField.getContext();
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
        return isValid;
    }
}

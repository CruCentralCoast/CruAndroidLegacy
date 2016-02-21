package org.androidcru.crucentralcoast.presentation.views.ridesharing.driversignup;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.databinding.ActivityDriverFormBinding;

import java.util.List;

public class DriverSignupValidator implements Validator.ValidationListener
{

    @NotEmpty private EditText name;
    @NotEmpty @Pattern(regex = AppConstants.PHONE_REGEX) private EditText phoneNumber;
    @Select private Spinner gender;
    @Select private Spinner carCapacity;
    @Select private Spinner tripType;
    @NotEmpty private EditText toEventDate;
    @NotEmpty private EditText toEventTime;
    @NotEmpty private EditText fromEventDate;
    @NotEmpty private EditText fromEventTime;

    private ActivityDriverFormBinding binding;
    private Context context;

    private Validator validator;
    private boolean isValid;

    public DriverSignupValidator(ActivityDriverFormBinding binding)
    {
        this.binding = binding;
        bindUI();
        context = name.getContext();

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void bindUI()
    {
        name = binding.nameField;
        phoneNumber = binding.phoneField;
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        gender = binding.genderField;
        carCapacity = binding.carCapacityField;
        tripType = binding.tripTypeField;
        toEventDate = binding.toEventDateField;
        toEventTime = binding.toEventTimeField;
        fromEventDate = binding.fromEventDateField;
        fromEventTime = binding.fromEventTimeField;
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

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
import com.mobsandgeeks.saripaar.annotation.Select;

import org.androidcru.crucentralcoast.databinding.PassengerFormRideInfoBinding;
import org.threeten.bp.LocalDate;

import java.util.List;

public class RideInfoValidator implements Validator.ValidationListener
{
    private PassengerFormRideInfoBinding binding;

    @Select private Spinner gender;
    @Select private Spinner tripType;
    @NotEmpty private EditText toEventDate;
    @NotEmpty private EditText toEventTime;
    @NotEmpty private EditText fromEventDate;
    @NotEmpty private EditText fromEventTime;

    private boolean isValid;
    private Context context;
    private Validator validator;

    public RideInfoValidator(PassengerFormRideInfoBinding binding)
    {
        this.binding = binding;
        bindUI();
        context = gender.getContext();

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void bindUI()
    {
        gender = binding.genderField;
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
        if(isValid)
        {
            valiDATE();
        }
        return isValid;
    }

    private void valiDATE()
    {
        if(fromEventDate.isShown() && toEventDate.isShown())
        {
            LocalDate fromDate = LocalDate.parse(fromEventDate.getText());
            LocalDate toDate = LocalDate.parse(toEventDate.getText());

            if(!fromDate.isEqual(toDate) && !fromDate.isBefore(toDate))
            {
                fromEventDate.setError("From Date isn't before To Date!");
                toEventDate.setError("From Date isn't before To Date!");
                isValid = false;
            }
        }
    }
}

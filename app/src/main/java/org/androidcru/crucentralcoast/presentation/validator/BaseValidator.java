package org.androidcru.crucentralcoast.presentation.validator;

import android.content.Context;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import org.androidcru.crucentralcoast.presentation.viewmodels.BaseVM;

import java.util.List;

public class BaseValidator implements Validator.ValidationListener
{
    protected Validator validator;
    protected Context context;

    private boolean isValid;

    public BaseValidator(BaseVM baseVM)
    {
        context = baseVM.context;
        validator = new Validator(baseVM);
        validator.setValidationListener(this);
        isValid = false;
        baseVM.rebind(this);
    }

    @Override
    public final void onValidationFailed(List<ValidationError> errors)
    {
        isValid = false;
        BaseValidatorUtil.onValidationError(context, errors);
    }

    @Override
    public final void onValidationSucceeded()
    {
        isValid = true;
    }

    public boolean validate()
    {
        validator.validate(false);
        return isValid;
    }
}

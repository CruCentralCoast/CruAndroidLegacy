package org.androidcru.crucentralcoast.presentation.validator;

import android.content.Context;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import org.androidcru.crucentralcoast.presentation.viewmodels.BaseVM;
import org.androidcru.crucentralcoast.presentation.views.base.BaseAppCompatActivity;
import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;

import java.util.List;

public class BaseValidator implements Validator.ValidationListener
{
    public Validator validator;
    protected Context context;

    private boolean isValid;

    public BaseValidator(BaseVM baseVM)
    {
        context = baseVM.context;
        validator = new Validator(baseVM);
        postInit();
    }

    public BaseValidator(BaseAppCompatActivity activity)
    {
        context = activity;
        validator = new Validator(activity);
        postInit();
    }

    public BaseValidator(BaseSupportFragment fragment)
    {
        context = fragment.getContext();
        validator = new Validator(fragment);
        postInit();
    }

    protected void postInit()
    {
        validator.setValidationListener(this);
        isValid = false;
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

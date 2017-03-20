package com.crucentralcoast.app.presentation.validator;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import com.crucentralcoast.app.presentation.viewmodels.BaseVM;

import java.util.List;

public class BaseValidator implements Validator.ValidationListener
{
    public Validator validator;
    protected Context context;

    private boolean isValid;

    public BaseValidator(RecyclerView.ViewHolder holder, View rootView)
    {
        context = rootView.getContext();
        validator = new Validator(holder);
        postInit();
    }

    public BaseValidator(BaseVM baseVM)
    {
        context = baseVM.context;
        validator = new Validator(baseVM);
        postInit();
    }

    public BaseValidator(Activity activity)
    {
        context = activity;
        validator = new Validator(activity);
        postInit();
    }

    public BaseValidator(Fragment fragment)
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

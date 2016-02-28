package org.androidcru.crucentralcoast.presentation.validator;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;

import java.util.List;

public class BaseValidatorUtil
{
    public static void onValidationError(Context context, List<ValidationError> errors)
    {
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
            else
            {
                ((Button) v).setError(e.getFailedRules().get(0).getMessage(context));
            }

        }
    }
}

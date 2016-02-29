package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.support.v4.app.Fragment;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;

import butterknife.Bind;

//TODO left as it's own class because we know we need email or phone validation
public class BasicInfoValidator extends BaseValidator
{
    @Bind(R.id.name_field) @NotEmpty EditText nameField;
    @Bind(R.id.phone_field) @NotEmpty @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;

    public BasicInfoValidator(Fragment fragment)
    {
        super(fragment);
    }
}

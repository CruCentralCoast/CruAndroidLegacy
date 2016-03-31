package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.validator.BaseValidator;

import butterknife.Bind;
import butterknife.ButterKnife;

/* TODO Kyle wanted both phone AND email so this class is unnecessary
   the view references can moved to another class
 */

public class BasicInfoValidator extends BaseValidator
{
    @Bind(R.id.name_field) @NotEmpty EditText nameField;
    @Bind(R.id.phone_field) @Optional @Pattern(regex = AppConstants.PHONE_REGEX) EditText phoneField;
    @Bind(R.id.email_field) @Optional @Email EditText emailField;

    public BasicInfoValidator(View view)
    {
        super(view.getContext());
        ButterKnife.bind(this, view);
    }
}

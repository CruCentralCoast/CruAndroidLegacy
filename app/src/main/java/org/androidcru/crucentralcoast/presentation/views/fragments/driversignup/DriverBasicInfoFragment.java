package org.androidcru.crucentralcoast.presentation.views.fragments.driversignup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.mobsandgeeks.saripaar.annotation.Select;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.fragments.ProvableFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DriverBasicInfoFragment extends ProvableFragment implements Validator.ValidationListener {

    //lol don't ask. SO is God. http://stackoverflow.com/a/124179/1822968
    public static final String PHONE_REGEX = "1?\\s*\\W?\\s*([2-9][0-8][0-9])\\s*\\W?" +
            "\\s*([2-9][0-9]{2})\\s*\\W?\\s*([0-9]{4})(\\se?x?t?(\\d*))?";

    @NotEmpty @Bind(R.id.name_field) EditText nameField;
    @NotEmpty @Pattern(regex = PHONE_REGEX) @Bind(R.id.phone_field) EditText phoneField;
    @Select(defaultSelection = -1) @Bind(R.id.sex_field) Spinner sexField;

    private Validator validator;
    private boolean isValid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.driver_form_basic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        validator = new Validator(this);

        /*set listeners on the phone and sex fields*/
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"Male", "Female"});
        sexAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sexField.setAdapter(sexAdapter);

        validator.setValidationListener(this);
    }

    @Override
    public boolean validate()
    {
        validator.validate();
        return isValid;
    }

    @Override
    public void onValidationSucceeded()
    {
        Logger.d("Successfully validated driver info");
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
                ((TextView) ((Spinner) v).getSelectedView()).setError(e.getCollatedErrorMessage(getContext()));
            }
            else
            {
                ((EditText)v).setError(e.getCollatedErrorMessage(getContext()));
            }

        }
    }
}

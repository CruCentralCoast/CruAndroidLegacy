package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.mobsandgeeks.saripaar.annotation.Select;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DriverFragment extends Fragment implements Validator.ValidationListener
{
    @NotEmpty @Bind(R.id.name_field) EditText mNameField;
    @NotEmpty @Bind(R.id.phone_field) EditText mPhoneField;
    @Select(defaultSelection = -1) @Bind(R.id.sex_field) Spinner mSexField;
    @Select(defaultSelection = -1) @Bind(R.id.car_capacity_field) Spinner mCarCapacityField;
    @Bind(R.id.submit_button) FloatingActionButton mSubmitButton;

    private Validator mValidator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.driver_form, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mValidator = new Validator(this);

        mPhoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"Male", "Female"});
        sexAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSexField.setAdapter(sexAdapter);

        ArrayAdapter<String> carCapacityAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5", "6", "7"});
        carCapacityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mCarCapacityField.setAdapter(carCapacityAdapter);

        mSubmitButton.setOnClickListener(v -> mValidator.validate());
        mSubmitButton.setImageDrawable(DrawableUtil.getTintedDrawable(getContext(), R.drawable.ic_check_grey600_48dp, android.R.color.white));

        mValidator.setValidationListener(this);
    }


    @Override
    public void onValidationSucceeded()
    {
        Logger.d("Successfully validated driver info");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors)
    {
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

package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.androidcru.crucentralcoast.presentation.presenters.MvpPresenter;
import org.androidcru.crucentralcoast.presentation.views.interactors.MvpInteractor;

public abstract class MvpFragment<P extends MvpPresenter> extends android.support.v4.app.Fragment implements MvpInteractor
{

    protected P presenter;

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(false);
        // Create the presenter if needed
        if (presenter == null) {
            presenter = createPresenter();
        }
        presenter.onAttachView(this);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        presenter.onDetachView(getRetainInstance());
        presenter = null;
    }


    protected abstract P createPresenter();
}
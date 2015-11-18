package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.androidcru.crucentralcoast.presentation.presenters.MvpPresenter;
import org.androidcru.crucentralcoast.presentation.views.views.MvpView;

public abstract class MvpFragment<P extends MvpPresenter> extends android.support.v4.app.Fragment implements MvpView {

    protected P presenter;

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create the presenter if needed
        if (presenter == null) {
            presenter = createPresenter();
        }
        presenter.attachView(this);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(getRetainInstance());
        presenter = null;
    }


    protected abstract P createPresenter();
}
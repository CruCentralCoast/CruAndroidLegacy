package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.presentation.views.views.MvpView;

public interface MvpPresenter<V extends MvpView> {

    public void attachView(V view);

    public void detachView(boolean retainInstance);
}
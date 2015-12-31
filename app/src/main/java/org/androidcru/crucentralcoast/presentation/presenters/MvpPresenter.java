package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.presentation.views.views.MvpView;

public interface MvpPresenter<V extends MvpView> {

    void onAttachView(V view);

    void onDetachView(boolean retainInstance);
}
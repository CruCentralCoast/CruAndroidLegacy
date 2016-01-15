package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.presentation.views.interactors.MvpInteractor;

public interface MvpPresenter<V extends MvpInteractor> {

    void onAttachView(V view);

    void onDetachView(boolean retainInstance);
}
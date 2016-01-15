package org.androidcru.crucentralcoast.presentation.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.androidcru.crucentralcoast.presentation.presenters.MvpPresenter;
import org.androidcru.crucentralcoast.presentation.views.interactors.MvpInteractor;


public abstract class MvpActivity<P extends MvpPresenter> extends AppCompatActivity implements MvpInteractor
{
    protected P presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.onAttachView(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.onDetachView(false);
    }

    protected abstract P createPresenter();
}

package org.androidcru.crucentralcoast.presentation.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.androidcru.crucentralcoast.presentation.presenters.MvpPresenter;
import org.androidcru.crucentralcoast.presentation.views.views.MvpView;


public abstract class MvpActivity<P extends MvpPresenter> extends AppCompatActivity implements MvpView
{
    protected P presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
    }

    protected abstract P createPresenter();
}

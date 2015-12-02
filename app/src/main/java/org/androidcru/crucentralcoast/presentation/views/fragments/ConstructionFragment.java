package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.presenters.MvpBasePresenter;
import org.androidcru.crucentralcoast.presentation.views.views.MvpView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConstructionFragment extends MvpFragment<MvpBasePresenter> implements MvpView
{

    @Bind(R.id.tv_title) TextView title;
    @Bind(R.id.tv_subtitle) TextView subtitle;

    @Override
    protected MvpBasePresenter createPresenter()
    {
        return new MvpBasePresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_underconstruction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

}

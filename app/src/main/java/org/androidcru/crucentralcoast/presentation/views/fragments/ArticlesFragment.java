package org.androidcru.crucentralcoast.presentation.views.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.presenters.MvpBasePresenter;
import org.androidcru.crucentralcoast.presentation.util.CustomTabActivityHelper;
import org.androidcru.crucentralcoast.presentation.views.activities.WebviewFallback;
import org.androidcru.crucentralcoast.presentation.views.views.MvpView;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticlesFragment extends MvpFragment<MvpBasePresenter<MvpView>>
{
    private static final String URL = "http://www.crucentralcoast.com/";

    private CustomTabActivityHelper mCustomTabActivityHelper;

    @Override
    protected MvpBasePresenter<MvpView> createPresenter()
    {
        return new MvpBasePresenter<>();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupCustomTabHelper();

    }

    private void setupCustomTabHelper() {
        mCustomTabActivityHelper = new CustomTabActivityHelper();
        mCustomTabActivityHelper.mayLaunchUrl(Uri.parse(URL), null, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_articles, container, false);
    }

    @OnClick(R.id.button)
    public void onLaunchSiteClick(View view) {
        openCustomTab();
    }

    private void openCustomTab() {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        int color = getResources().getColor(R.color.colorPrimary);
        intentBuilder.setToolbarColor(color);

        intentBuilder.setShowTitle(true);

        CustomTabActivityHelper.openCustomTab(
                getActivity(), intentBuilder.build(), Uri.parse(URL), new WebviewFallback());
    }
}

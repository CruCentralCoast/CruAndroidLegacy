package org.androidcru.crucentralcoast.presentation.views.resources.articles;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.util.CustomTabActivityHelper;
import org.androidcru.crucentralcoast.presentation.views.webview.WebviewFallback;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticlesFragment extends Fragment
{
    private static final String URL = "http://www.slocru.com/";

    private CustomTabActivityHelper mCustomTabActivityHelper;

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

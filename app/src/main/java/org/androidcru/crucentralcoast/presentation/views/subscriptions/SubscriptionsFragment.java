package org.androidcru.crucentralcoast.presentation.views.subscriptions;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.AppConstants;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.SubscriptionProvider;
import org.androidcru.crucentralcoast.presentation.util.DrawableUtil;
import org.androidcru.crucentralcoast.presentation.views.MainActivity;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionsFragment#} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionsFragment extends Fragment
{

    private GridLayoutManager mLayoutManager;
    private SubscriptionsAdapter mSubscriptionAdapter;

    @Bind(R.id.subscription_list) RecyclerView subscriptionList;
    @Bind(R.id.fab) FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        fab.setImageDrawable(DrawableUtil.getTintedDrawable(getContext(), R.drawable.ic_check_grey600_48dp, android.R.color.white));
        fab.setOnClickListener(v -> {

            if (!CruApplication.getSharedPreferences().getBoolean(AppConstants.FIRST_LAUNCH, false))
            {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            CruApplication.getSharedPreferences().edit().putBoolean(AppConstants.FIRST_LAUNCH, true).apply();
            getActivity().finish();
        });

        mSubscriptionAdapter = new SubscriptionsAdapter(new HashMap<>());
        subscriptionList.setHasFixedSize(true);
        subscriptionList.setAdapter(mSubscriptionAdapter);

        // use a grid layout manager
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public int getSpanSize(int position)
            {
                return mSubscriptionAdapter.isHeader(position) ? mLayoutManager.getSpanCount() : 1;
            }
        });
        subscriptionList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)


        getCampusMinistryMap();
    }



    public void getCampusMinistryMap()
    {
        SubscriptionProvider.getInstance().requestCampusMinistryMap()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ministries -> {
                    mSubscriptionAdapter = new SubscriptionsAdapter(ministries);
                    subscriptionList.setAdapter(mSubscriptionAdapter);
                });
    }
}

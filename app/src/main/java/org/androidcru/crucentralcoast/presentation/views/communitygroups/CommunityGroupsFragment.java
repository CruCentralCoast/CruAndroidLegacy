package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.views.base.BaseSupportFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunityGroupsFragment extends BaseSupportFragment
{
    public CommunityGroupsFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_community_groups, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.community_groups_button)
    public void onJoinClicked()
    {
        startActivity(new Intent(getContext(), JoinCommunityGroupActivity.class));
    }
}

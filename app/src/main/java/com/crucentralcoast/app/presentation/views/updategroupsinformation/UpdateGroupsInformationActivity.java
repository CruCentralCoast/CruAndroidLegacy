package com.crucentralcoast.app.presentation.views.updategroupsinformation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.providers.UpdateGroupsInformationProvider;
import com.crucentralcoast.app.presentation.views.base.BaseSupportFragment;

import butterknife.OnClick;
import timber.log.Timber;


/**
 * Created by Dylan on 11/25/2017.
 */

public class UpdateGroupsInformationActivity extends AppCompatActivity {

    private String fragmentType;
    private String groupID;
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_groups_information);
        intent = getIntent();
        fragmentType = intent.getStringExtra("fragmentType");
        groupID = intent.getStringExtra("groupID");

        createBundle(groupID);
        startFragment(fragmentType);
    }

    private void startFragment(String fragmentType) {
        switch (fragmentType) {
            case "community_group":
                getSupportFragmentManager().beginTransaction().replace(R.id.content, createCommunityGroupFragment()).commit();
                break;

            case "ministry_team":
                getSupportFragmentManager().beginTransaction().replace(R.id.content, createMinistryTeamFragment()).commit();
                break;
        }
    }

    private void createBundle(String groupID) {
        bundle = new Bundle();
        bundle.putString("groupID", groupID);
    }

    private UpdateCommunityGroupFragment createCommunityGroupFragment() {
        UpdateCommunityGroupFragment fragment = new UpdateCommunityGroupFragment();
        if (bundle.isEmpty()) {
            return null;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    private UpdateMinistryTeamFragment createMinistryTeamFragment() {
        UpdateMinistryTeamFragment fragment = new UpdateMinistryTeamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}

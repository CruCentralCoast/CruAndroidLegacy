package com.crucentralcoast.app.presentation.views.communitygroups;

import android.os.Bundle;

import com.crucentralcoast.app.presentation.views.forms.FormActivity;
import com.crucentralcoast.app.presentation.views.forms.FormContentFragment;

import java.util.ArrayList;

public class JoinCommunityGroupActivity extends FormActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ArrayList<FormContentFragment> fragments = new ArrayList<>();

        // there are multiple basic info fragments, same name, but I'm importing it from the community group package
        fragments.add(new MinistrySelectionFragment());
        fragments.add(new BasicInfoFragment());
        fragments.add(new MinistryQuestionsFragment());
        fragments.add(new ResultsFragment());
        fragments.add(new LeaderInformationFragment());

        setFormContent(fragments);
    }
}

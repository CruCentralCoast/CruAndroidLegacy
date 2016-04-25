package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.os.Bundle;

import org.androidcru.crucentralcoast.presentation.views.forms.FormActivity;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.communitygroups.BasicInfoFragment;
import org.androidcru.crucentralcoast.presentation.views.subscriptions.SubscriptionsFragment;

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

        setFormContent(fragments);
    }
}

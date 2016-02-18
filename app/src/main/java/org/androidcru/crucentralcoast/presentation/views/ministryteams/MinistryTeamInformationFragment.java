package org.androidcru.crucentralcoast.presentation.views.ministryteams;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryTeam;
import org.androidcru.crucentralcoast.databinding.FragmentMinistryTeamInfoBinding;
import org.androidcru.crucentralcoast.presentation.customviews.NonSwipeableViewPager;
import org.androidcru.crucentralcoast.presentation.viewmodels.ministryteams.MinistryTeamVM;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MinistryTeamInformationFragment extends FormContentFragment
{
    private FragmentMinistryTeamInfoBinding binding;
    private MinistryTeamVM ministryTeamVM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentMinistryTeamInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MinistryTeam ministryTeam = (MinistryTeam) formHolder.getDataObject();
        ministryTeamVM = new MinistryTeamVM(getActivity(), ministryTeam);
        binding.setMinistryTeamVM(ministryTeamVM);
    }

    public MinistryTeamInformationFragment()
    {
        super();
    }

    @Override
    public void setupUI()
    {
        formHolder.setTitle(ministryTeamVM.ministryTeam.name);
    }
}

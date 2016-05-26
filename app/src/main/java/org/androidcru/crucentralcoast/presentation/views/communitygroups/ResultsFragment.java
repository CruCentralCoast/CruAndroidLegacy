package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.CommunityGroup;
import org.androidcru.crucentralcoast.data.models.MinistryQuestionAnswer;
import org.androidcru.crucentralcoast.data.providers.CommunityGroupProvider;
import org.androidcru.crucentralcoast.presentation.util.DividerItemDecoration;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentListFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class ResultsFragment extends FormContentListFragment
{
    @BindView(R.id.informational_text) protected TextView informationalText;

    private List<CommunityGroup> results;

    private FormHolder formHolder;
    private Observer<List<CommunityGroup>> cgResultsObserver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        informationalText.setText("No Community Groups Found.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        helper.swipeRefreshLayout.setOnRefreshListener(() -> getCommunityGroups());
    }


    private void getCommunityGroups()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        results.clear();
        CommunityGroupProvider.getCommunityGroups(this, cgResultsObserver, (List<MinistryQuestionAnswer>) formHolder.getDataObject("questionAnswers"));
    }

    private void handleResults(List<CommunityGroup> results)
    {
        this.results = results;
        helper.recyclerView.setAdapter(new ResultsAdapter(results, formHolder));
    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        this.formHolder = formHolder;
        formHolder.setTitle("Pick a Community Group");

        results = new ArrayList<>();
        cgResultsObserver = createListObserver(R.layout.empty_with_alert,
                communityGroups -> {
                    handleResults(communityGroups);
                });

        formHolder.setNavigationVisibility(View.VISIBLE);
        formHolder.setNextVisibility(View.GONE);
        formHolder.setPreviousVisibility(View.VISIBLE);
        getCommunityGroups();
    }
}

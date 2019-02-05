package com.crucentralcoast.app.presentation.views.communitygroups;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.CommunityGroup;
import com.crucentralcoast.app.data.models.MinistryQuestionAnswer;
import com.crucentralcoast.app.data.providers.CommunityGroupProvider;
import com.crucentralcoast.app.presentation.util.DividerItemDecoration;
import com.crucentralcoast.app.presentation.views.forms.FormContentListFragment;
import com.crucentralcoast.app.presentation.views.forms.FormHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class ResultsFragment extends FormContentListFragment
{
    @BindView(R.id.informational_text) protected TextView informationalText;

    private List<CommunityGroup> results;
    private List<CommunityGroup> filteredResults;

    private FormHolder formHolder;
    private Observer<List<CommunityGroup>> cgResultsObserver;
    private AlertDialog dayDialog;
    private boolean[] selectedDays;
    private ResultsAdapter adapter;
    private boolean shouldResume;

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
        shouldResume = false;

        unbinder = ButterKnife.bind(this, view);

        selectedDays = new boolean[7];
        Arrays.fill(selectedDays, true);

        informationalText.setText("No Community Groups Found.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        helper.swipeRefreshLayout.setOnRefreshListener(() -> getCommunityGroups());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldResume) {
            forceUpdate();
        }
        else {
            shouldResume = true;
        }
    }

    private void getCommunityGroups()
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        results.clear();
        filteredResults.clear();
        CommunityGroupProvider.getCommunityGroups(this, cgResultsObserver, (List<MinistryQuestionAnswer>) formHolder.getDataObject("questionAnswers"));
    }

    private void handleResults(List<CommunityGroup> results)
    {
        this.results = results;
        this.filteredResults = new ArrayList<>(results);
        forceUpdate();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Resource type filter menu item
        inflater.inflate(R.menu.community_groups_filter, menu);
        menu.findItem(R.id.filter_by_day).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                displayFilterDayAlertDialog();
                return false;
            }
        });
    }

    //Display dialog for filtering resources by type
    private void displayFilterDayAlertDialog() {
        if(dayDialog == null)
        {
            String[] days = getContext().getResources().getStringArray(R.array.days_of_week);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setMultiChoiceItems(
                    days, selectedDays,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            selectedDays[which] = isChecked;
                        }
                    });
            builder.setTitle("Filter Days");

            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            forceUpdate();
                        }
                    });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            dayDialog = builder.create();
        }

        dayDialog.show();
    }

    private void forceUpdate()
    {
        filteredResults.clear();

        ArrayList<Integer> filteredDays = new ArrayList<>();
        for(int i = 0; i < selectedDays.length; i++)
        {
            if(selectedDays[i])
                filteredDays.add(i);
        }

        for(CommunityGroup cg : results)
        {
            if(filteredDays.contains(cg.dayOfWeek.getValue()))
            {
                filteredResults.add(cg);
            }
        }

        Comparator<CommunityGroup> cgComparator = (lhs, rhs) -> {
            int diff = lhs.dayOfWeek.getValue() - rhs.dayOfWeek.getValue();
            if(diff == 0)
            {
                return lhs.meetingTime.toLocalTime().compareTo(rhs.meetingTime.toLocalTime());
            }
            else
                return diff;
        };

        Collections.sort(filteredResults, cgComparator);
        adapter = new ResultsAdapter(filteredResults, formHolder);
        helper.recyclerView.setAdapter(adapter);
        if(filteredResults.isEmpty())
            helper.onEmpty(R.layout.empty_with_alert);
        else
            showContent();
    }

    @Override
    public void setupData(FormHolder formHolder)
    {
        this.formHolder = formHolder;
        formHolder.setTitle("Pick a Community Group");

        results = new ArrayList<>();
        filteredResults = new ArrayList<>();
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

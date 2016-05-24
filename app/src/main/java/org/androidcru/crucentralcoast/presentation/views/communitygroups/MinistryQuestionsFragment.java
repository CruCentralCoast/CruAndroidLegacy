package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
import org.androidcru.crucentralcoast.data.providers.MinistryQuestionsProvider;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.androidcru.crucentralcoast.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import timber.log.Timber;

public class MinistryQuestionsFragment extends FormContentFragment
{
    @BindView(R.id.recyclerview) RecyclerView questionsList;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_view_stub) ViewStub emptyViewStub;

    private LinearLayoutManager layoutManager;
    private Observer<List<MinistryQuestion>> observer;
    private List<MinistryQuestion> questions;
    private MinistryQuestionsAdapter adapter;
    protected View emptyView;
    private String ministryId;

    public MinistryQuestionsFragment()
    {
        observer = new Observer<List<MinistryQuestion>>()
        {
            @Override
            public void onCompleted() {
                if(questions.isEmpty())
                {
                    emptyView.setVisibility(View.VISIBLE);
                    ((TextView)(MinistryQuestionsFragment.this.getView().findViewById(R.id.informational_text))).setText("No ministry questions were found");
                }
                else
                {
                    emptyView.setVisibility(View.GONE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e)
            {
                Timber.e(e, "Ministry Question Error");
            }

            @Override
            public void onNext(List<MinistryQuestion> ministryQuestions)
            {
                if(questions == null || questions.isEmpty())
                {
                    questions = ministryQuestions;
                    adapter = new MinistryQuestionsAdapter(ministryQuestions, layoutManager, getActivity().getFragmentManager());
                    questionsList.setAdapter(adapter);
                }
                else
                {
                    questions.addAll(ministryQuestions);
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        questions = new ArrayList<>();
        emptyViewStub.setLayoutResource(R.layout.empty_with_alert);
        emptyView = emptyViewStub.inflate();

        questionsList.setHasFixedSize(true);

        setupSwipeRefreshLayout(swipeRefreshLayout);

        layoutManager = new LinearLayoutManager(getContext());
        questionsList.setLayoutManager(layoutManager);
        questionsList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                getQuestions(ministryId);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> forceUpdate());
    }

    public static void setupSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout)
    {
        //issue 77712, workaround until Google fixes it
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        swipeRefreshLayout.setColorSchemeResources(R.color.cruDarkBlue, R.color.cruGold, R.color.cruOrange);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        forceUpdate();
    }

    private void forceUpdate()
    {
        questions.clear();
        adapter = null;
        swipeRefreshLayout.setRefreshing(true);
        getQuestions(ministryId);
    }

    private void getQuestions(String ministryId)
    {
        swipeRefreshLayout.setRefreshing(true);
        MinistryQuestionsProvider.getMinistryQuestions(this, observer, ministryId);
    }

    @Override
    public void setupData(FormHolder formHolder) {
        formHolder.setTitle("Community Group Form");
        formHolder.setSubtitle("");

        ministryId = (String) formHolder.getDataObject("ministry");

        forceUpdate();
    }

    @Override
    public void onNext(FormHolder formHolder)
    {
        // validate data, well kind of
        // write the data and filter on it

        super.onNext(formHolder);
    }

}
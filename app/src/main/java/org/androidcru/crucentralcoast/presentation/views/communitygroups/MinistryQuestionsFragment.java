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

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
import org.androidcru.crucentralcoast.data.models.MinistryQuestionAnswer;
import org.androidcru.crucentralcoast.data.providers.MinistryQuestionsProvider;
import org.androidcru.crucentralcoast.presentation.views.forms.FormContentListFragment;
import org.androidcru.crucentralcoast.presentation.views.forms.FormHolder;
import org.androidcru.crucentralcoast.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;

public class MinistryQuestionsFragment extends FormContentListFragment
{
    @BindView(R.id.informational_text) TextView informationText;

    private LinearLayoutManager layoutManager;
    private Observer<List<MinistryQuestion>> observer;
    private List<MinistryQuestion> questions;
    private MinistryQuestionsAdapter adapter;
    protected View emptyView;
    private String ministryId;
    private FormHolder formHolder;

    public MinistryQuestionsFragment()
    {
        observer = helper.createListObserver( ministryQuestions -> {
            if(questions == null || questions.isEmpty())
            {
                questions = ministryQuestions;
                adapter = new MinistryQuestionsAdapter(ministryQuestions, layoutManager, getActivity().getFragmentManager());
                helper.recyclerView.setAdapter(adapter);
            }
        }, () -> {
            formHolder.setNextVisibility(View.GONE);
            helper.onEmpty(R.layout.empty_with_alert);
        });
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
        inflateEmptyView(view, R.layout.empty_with_alert);
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        informationText.setText("No community groups found");

        questions = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(layoutManager);
        helper.recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                getQuestions(ministryId);
            }
        });

        helper.swipeRefreshLayout.setOnRefreshListener(() -> forceUpdate());
    }

    private void forceUpdate()
    {
        questions.clear();
        adapter = null;
        helper.swipeRefreshLayout.setRefreshing(true);
        getQuestions(ministryId);
    }

    private void getQuestions(String ministryId)
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        MinistryQuestionsProvider.getMinistryQuestions(this, observer, ministryId);
    }

    @Override
    public void setupData(FormHolder formHolder) {
        formHolder.setTitle("Community Group Form");
        formHolder.setSubtitle("");

        this.formHolder = formHolder;

        ministryId = (String) formHolder.getDataObject("ministry");

        forceUpdate();
    }

    @Override
    public void onNext(FormHolder formHolder)
    {
        ArrayList<MinistryQuestionAnswer> questionAnswers = new ArrayList<>();
        for (Map.Entry<MinistryQuestion, String> e : adapter.questionAnswerMap.entrySet())
        {
            questionAnswers.add(new MinistryQuestionAnswer(e.getKey(), e.getValue()));
        }

        formHolder.addDataObject("questionAnswers", questionAnswers);

        super.onNext(formHolder);
    }

}
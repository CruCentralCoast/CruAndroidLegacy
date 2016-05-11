package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinistryQuestionsSelectHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.cgq_select_text) TextView selectDescription;
    @BindView(R.id.cgq_select) Spinner selectionEntry;

    public MinistryQuestion question;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    public MinistryQuestionsSelectHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager)
    {
        super(rootView);
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        ButterKnife.bind(this, rootView);
    }

    public void bindQuestion(MinistryQuestion question)
    {
        this.question = question;
        bindUI(question, null);
    }

    private void bindUI(MinistryQuestion question, List<String> selectOptions)
    {
        selectDescription.setText(this.question.question);
    }
}
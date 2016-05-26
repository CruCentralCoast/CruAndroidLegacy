package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
import org.androidcru.crucentralcoast.presentation.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinistryQuestionsSelectHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.cgq_select_text) TextView selectDescription;
    @BindView(R.id.cgq_select) Spinner selectionEntry;

    public MinistryQuestion question;
    public MinistryQuestionsAdapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    public MinistryQuestionsSelectHolder(View rootView, MinistryQuestionsAdapter adapter, RecyclerView.LayoutManager layoutManager)
    {
        super(rootView);
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        ButterKnife.bind(this, rootView);
    }

    public void bindQuestion(MinistryQuestion question)
    {
        this.question = question;
        bindUI();
    }

    private void bindUI()
    {
        selectDescription.setText(this.question.question);
        String[] selectionOptions = question.getSelectionOptions();

        ViewUtil.setSpinner(selectionEntry, selectionOptions, new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                adapter.questionAnswerMap.put(question, selectionOptions[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        }, 0);
    }
}
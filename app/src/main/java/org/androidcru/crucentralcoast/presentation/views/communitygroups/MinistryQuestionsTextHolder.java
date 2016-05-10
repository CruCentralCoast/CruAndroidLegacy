package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mitch on 5/8/16.
 */
public class MinistryQuestionsTextHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.cgq_text_text) TextView textDescription;
    @BindView(R.id.cgq_text) EditText testEntry;

    public MinistryQuestion question;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    public MinistryQuestionsTextHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager)
    {
        super(rootView);
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        ButterKnife.bind(this, rootView);
    }

    public void bindQuestion(MinistryQuestion question)
    {
        this.question = question;
        bindUI(question);
    }

    private void bindUI(MinistryQuestion question)
    {
        textDescription.setText(this.question.question);
        //TODO Set text entry hint?


    }
}

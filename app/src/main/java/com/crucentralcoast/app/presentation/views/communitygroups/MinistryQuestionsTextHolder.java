package com.crucentralcoast.app.presentation.views.communitygroups;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.MinistryQuestion;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinistryQuestionsTextHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.cgq_text_text) TextView textDescription;
    @BindView(R.id.cgq_text) @NotEmpty EditText textEntry;

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

    public void bindQuestion(MinistryQuestion questionState)
    {
        this.question = questionState;
        textDescription.setText(this.question.question);
        // TODO do we want to set a text hint?
    }
}

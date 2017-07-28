package com.crucentralcoast.app.presentation.views.communitygroups;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.crucentralcoast.app.R;
import com.crucentralcoast.app.data.models.MinistryQuestion;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinistryQuestionsToggleHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cgq_toggle_text) TextView toggleDescription;
    @BindView(R.id.cgq_toggle) Switch tog;

    public MinistryQuestion question;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    public MinistryQuestionsToggleHolder(View rootView, RecyclerView.Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        super(rootView);
        this.adapter = adapter;
        this.layoutManager = layoutManager;

        ButterKnife.bind(this, rootView);
    }

    public void bindQuestion(MinistryQuestion question) {
        this.question = question;
        bindUI(question);
    }

    private void bindUI(MinistryQuestion question) {
        toggleDescription.setText(this.question.question);
        //TODO Set text entry hint?


    }
}
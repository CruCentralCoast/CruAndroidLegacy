package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;

import java.util.List;

public class MinistryQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<MinistryQuestion> questions;
    RecyclerView.LayoutManager layoutManager;

    private final int TEXT = 0;
    private final int SELECT = 1;
    private final int DATETIME = 2;

    public MinistryQuestionsAdapter(List<MinistryQuestion> questions, RecyclerView.LayoutManager layoutManager)
    {
        this.questions = questions;
        this.layoutManager = layoutManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()); // not sure what this does
//        switch (viewType)
//        {
//            case TEXT:
//        }
        //TODO create the rest of the holders, put the return in a switch with the rest of the holders based on the quesiton type
        return new MinistryQuestionsTextHolder(inflater.inflate(R.layout.card_cg_question_text, parent, false), this, layoutManager);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        MinistryQuestionsTextHolder viewHolder = (MinistryQuestionsTextHolder) holder;
        viewHolder.bindQuestion((MinistryQuestion) questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}

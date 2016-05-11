package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.presentation.viewmodels.ExpandableState;
import org.androidcru.crucentralcoast.presentation.views.events.CruEventViewHolder;
import org.androidcru.crucentralcoast.presentation.views.resources.ResourceViewHolder;
import org.androidcru.crucentralcoast.presentation.views.videos.CruVideoViewHolder;

import java.util.List;

public class MinistryQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int TEXT = 0;
    private static final int SELECT = 1;
    private static final int DATETIME= 2;

    List<MinistryQuestion> questions;
    RecyclerView.LayoutManager layoutManager;

    public MinistryQuestionsAdapter(List<MinistryQuestion> questions, RecyclerView.LayoutManager layoutManager)
    {
        this.questions = questions;
        this.layoutManager = layoutManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()); // not sure what this does
        switch (viewType)
        {
            case TEXT:
                return new MinistryQuestionsTextHolder(inflater.inflate(R.layout.card_cg_question_text, parent, false), this, layoutManager);
            case SELECT:
                return new MinistryQuestionsSelectHolder(inflater.inflate(R.layout.card_cg_question_select, parent, false), this, layoutManager);
            default:
                return new MinistryQuestionsToggleHolder(inflater.inflate(R.layout.card_cg_question_toggle, parent, false), this, layoutManager);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder instanceof MinistryQuestionsTextHolder)
        {
            MinistryQuestionsTextHolder viewHolder = (MinistryQuestionsTextHolder) holder;
            viewHolder.bindQuestion(questions.get(position));
        }
        else if(holder instanceof MinistryQuestionsSelectHolder)
        {
            MinistryQuestionsSelectHolder viewHolder = (MinistryQuestionsSelectHolder) holder;
            viewHolder.bindQuestion(questions.get(position));
        }
        else
        {
            MinistryQuestionsToggleHolder viewHolder = (MinistryQuestionsToggleHolder) holder;
            viewHolder.bindQuestion(questions.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if(questions.get(position).type.equals("text"))
        {
            return TEXT;
        }
        else if(questions.get(position).type.equals("select"))
        {
            return SELECT;
        }
        else
        {
            return DATETIME;
        }
    }
}

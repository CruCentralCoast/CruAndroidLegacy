package org.androidcru.crucentralcoast.presentation.views.communitygroups;

import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.MinistryQuestion;

import java.util.HashMap;
import java.util.List;

public class MinistryQuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private static final int TEXT = 0;
    private static final int SELECT = 1;

    public HashMap<MinistryQuestion, String> questionAnswerMap;
    List<MinistryQuestion> questions;
    RecyclerView.LayoutManager layoutManager;
    private FragmentManager fm;

    public MinistryQuestionsAdapter(List<MinistryQuestion> questions, RecyclerView.LayoutManager layoutManager, FragmentManager fm)
    {
        this.questions = questions;
        this.layoutManager = layoutManager;
        this.fm = fm;
        this.questionAnswerMap = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case TEXT:
                return new MinistryQuestionsTextHolder(inflater.inflate(R.layout.card_cg_question_text, parent, false), this, layoutManager);
            default:
                return new MinistryQuestionsSelectHolder(inflater.inflate(R.layout.card_cg_question_select, parent, false), this, layoutManager);
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
        else
        {
            MinistryQuestionsSelectHolder viewHolder = (MinistryQuestionsSelectHolder) holder;
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
        //switch cases are the enums, the return values are ints declared at the top
        //of this file
        switch (questions.get(position).type)
        {
            case TEXT:
                return TEXT;
            default:
                return SELECT;
        }
    }
}

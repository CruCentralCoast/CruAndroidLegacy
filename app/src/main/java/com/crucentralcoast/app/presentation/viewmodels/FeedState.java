package com.crucentralcoast.app.presentation.viewmodels;

import com.crucentralcoast.app.data.models.Dateable;

import org.threeten.bp.ZonedDateTime;

public class FeedState<T extends Dateable> extends ExpandableState<Dateable> implements Dateable
{
    public FeedState(Dateable model)
    {
        super(model);
    }

    @Override
    public ZonedDateTime getDate()
    {
        return model.getDate();
    }
}

package org.androidcru.crucentralcoast.presentation.viewmodels;

import org.androidcru.crucentralcoast.data.models.Dateable;
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

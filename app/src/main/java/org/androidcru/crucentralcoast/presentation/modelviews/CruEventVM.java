package org.androidcru.crucentralcoast.presentation.modelviews;

import org.androidcru.crucentralcoast.data.models.CruEvent;

public class CruEventVM
{
    public CruEvent mCruEvent;
    public boolean mIsExpanded;
    public boolean mAddedToCalendar;
    public long mLocalEventId;

    public CruEventVM(CruEvent mCruEvent, boolean mIsExpanded, boolean mAddedToCalendar, long localEventId)
    {
        this.mCruEvent = mCruEvent;
        this.mIsExpanded = mIsExpanded;
        this.mAddedToCalendar = mAddedToCalendar;
        this.mLocalEventId = localEventId;
    }
}

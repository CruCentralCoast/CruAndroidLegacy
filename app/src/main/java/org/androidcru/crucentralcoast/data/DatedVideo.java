package org.androidcru.crucentralcoast.data;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResult;

import org.androidcru.crucentralcoast.data.models.Dateable;
import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;

import java.util.Date;

public class DatedVideo implements Dateable
{
    public SearchResult getVideo()
    {
        return video;
    }

    private SearchResult video;

    public DatedVideo(SearchResult video)
    {
        this.video = video;
    }

    @Override
    public ZonedDateTime getDate()
    {
        DateTime dateTime = video.getSnippet().getPublishedAt();
        return ZonedDateTime.ofInstant(DateTimeUtils.toInstant(new Date(dateTime.getValue())), ZoneOffset.ofHours(dateTime.getTimeZoneShift()));
    }
}

package org.androidcru.crucentralcoast.data.providers;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.events.VideoDataEvent;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

public final class VideoProvider {

    private static VideoProvider instance;

    private YouTube youtube;
    private YouTube.Videos.List query;

    private VideoProvider()
    {
        youtube = new YouTube.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory.getDefaultInstance(), null)
                .setApplicationName(CruApplication.getContext().getString(R.string.app_name))
                .build();

        try
        {
            query = youtube.videos().list("id,snippet");
            query.setKey(BuildConfig.YOUTUBEBROWSERAPIKEY);
            query.setFields("items(snippet/title,snippet/mDescription,snippet/thumbnails/default/url)");
        }
        catch(IOException e)
        {
            Logger.e(e.getMessage());
        }

    }

    public static VideoProvider getInstance() {
        if(instance == null)
            instance = new VideoProvider();
        return instance;
    }

    public void requestVideo(final String videoId)
    {
        query.setId(videoId);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    VideoListResponse listResponse = query.execute();
                    List<Video> videoList = listResponse.getItems();
                    if(videoList.size() > 0)
                        EventBus.getDefault().post(new VideoDataEvent(videoList.get(0).getSnippet()));
                }
                catch (IOException e)
                {
                    Logger.e(e.getMessage());
                }
            }
        }).start();
    }
}

package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.data.models.VideoList;
import org.androidcru.crucentralcoast.data.providers.VideoProvider;
import org.androidcru.crucentralcoast.data.providers.events.VideoDataEvent;
import org.androidcru.crucentralcoast.presentation.views.views.VideoView;

public class VideoPresenter extends MvpBasePresenter<VideoView> {

    public VideoPresenter()
    {
        eventBus.register(this);
    }

    public void requestVideoInformation(String videoId)
    {
        VideoProvider.getInstance().requestVideo(videoId);
    }

    public void onEventMainThread(VideoDataEvent videoDataEvent)
    {
        VideoList videoList = videoDataEvent.videoList;
        getView().setTitle(videoList.items.get(0).snippet.title);
        getView().setDescription(videoList.items.get(0).snippet.description);
    }
}

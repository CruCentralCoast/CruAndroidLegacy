package org.androidcru.crucentralcoast.presentation.presenters;

import com.google.api.services.youtube.model.VideoSnippet;

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
        VideoSnippet videoSnippet = videoDataEvent.videoSnippet;
        getView().setTitle(videoSnippet.getTitle());
        getView().setDescription(videoSnippet.getDescription());
    }
}

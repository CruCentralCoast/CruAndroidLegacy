package org.androidcru.crucentralcoast.data.providers.events;

import com.google.api.services.youtube.model.VideoSnippet;

public class VideoDataEvent {

    public VideoSnippet videoSnippet;

    public VideoDataEvent(VideoSnippet videoSnippet) {
        this.videoSnippet = videoSnippet;
    }
}

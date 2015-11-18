package org.androidcru.crucentralcoast.data.providers.events;

import org.androidcru.crucentralcoast.data.models.VideoList;

public class VideoDataEvent {

    public VideoList videoList;

    public VideoDataEvent(VideoList videoList) {
        this.videoList = videoList;
    }
}

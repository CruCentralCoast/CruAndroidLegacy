package org.androidcru.crucentralcoast.presentation.views.hub;

import org.androidcru.crucentralcoast.data.models.CruEvent;
import org.androidcru.crucentralcoast.data.models.youtube.Snippet;
import org.androidcru.crucentralcoast.presentation.views.BasePresenter;
import org.androidcru.crucentralcoast.presentation.views.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class HubContract {
    interface View extends BaseView<Presenter> {
        void showEvents(List<CruEvent> events);

        void showVideos(List<Snippet> videos);
    }

    interface Presenter extends BasePresenter {
        void loadEvents();

        void loadVideos();
    }
}

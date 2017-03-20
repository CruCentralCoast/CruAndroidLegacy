package com.crucentralcoast.app.presentation.views.hub;

import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.presentation.views.BasePresenter;
import com.crucentralcoast.app.presentation.views.BaseView;
import com.crucentralcoast.app.data.models.youtube.Snippet;

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

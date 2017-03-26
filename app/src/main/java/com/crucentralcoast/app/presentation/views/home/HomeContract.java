package com.crucentralcoast.app.presentation.views.home;

import com.crucentralcoast.app.data.models.CruEvent;
import com.crucentralcoast.app.data.models.Ride;
import com.crucentralcoast.app.data.models.youtube.Snippet;
import com.crucentralcoast.app.presentation.views.BasePresenter;
import com.crucentralcoast.app.presentation.views.BaseView;

import java.util.List;

/**
 * @author Tyler Wong
 */

public class HomeContract {
    interface View extends BaseView<Presenter> {
        void showEvents(List<CruEvent> events);

        void showRides(List<Ride> rides);

        void showRidesCompleted();

        void showVideos(List<Snippet> videos);
    }

    interface Presenter extends BasePresenter {
        void loadEvents();

        void loadVideos();

        void loadRides();
    }
}

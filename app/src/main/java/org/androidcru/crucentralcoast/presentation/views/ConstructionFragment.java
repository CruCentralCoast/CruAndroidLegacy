package org.androidcru.crucentralcoast.presentation.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.providers.YouTubeVideoProvider;
import org.androidcru.crucentralcoast.util.RxLoggingUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ConstructionFragment extends Fragment
{
    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_underconstruction, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        /*
        YouTubeVideoProvider.getInstance().requestChannelVideos()
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(searchResultList -> Observable.from(searchResultList))
                .compose(RxLoggingUtil.log("YOUTUBE VIDEO Construction"))
                .subscribe(searchResult -> {
                            Logger.t("INSPECT").d("id: " + searchResult.getId() + "snippet: " + searchResult.getSnippet());
                        },
                        e -> {
                            //some IO error got thrown
                        },
                        () -> {
                            //onCompleted
                        });
        */
    }
}

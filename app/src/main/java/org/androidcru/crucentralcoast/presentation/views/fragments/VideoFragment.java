package org.androidcru.crucentralcoast.presentation.views.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.orhanobut.logger.Logger;

import org.androidcru.crucentralcoast.BuildConfig;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.presentation.presenters.VideoPresenter;
import org.androidcru.crucentralcoast.presentation.views.interactors.VideoInteractor;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoFragment extends MvpFragment<VideoPresenter> implements VideoInteractor
{

    YouTubePlayerSupportFragment youTubePlayerFragment;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.description)
    TextView description;

    YouTubePlayer activePlayer;

    public static final String VIDEO_ID = "dQw4w9WgXcQ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.requestVideoInformation(VIDEO_ID);
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.youtubeplayerfragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(BuildConfig.YOUTUBEAPIKEY, new YouTubePlayer.OnInitializedListener()
        {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored)
            {
                activePlayer = player;
                if (!wasRestored)
                {
                    player.cueVideo("dQw4w9WgXcQ");
                }
                player.loadVideo("dQw4w9WgXcQ");
                player.setShowFullscreenButton(false);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
            {
                Logger.d(youTubeInitializationResult.toString());
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        activePlayer.release();
    }

    @Override
    protected VideoPresenter createPresenter() {
        return new VideoPresenter();
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void setDescription(String description) {
        this.description.setText(description);
    }
}

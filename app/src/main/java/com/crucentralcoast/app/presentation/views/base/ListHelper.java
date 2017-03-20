package com.crucentralcoast.app.presentation.views.base;

import android.view.View;

import com.crucentralcoast.app.data.providers.observer.CruObserver;

import rx.functions.Action0;
import rx.functions.Action1;

public interface ListHelper
{
    View onEmpty(int layoutId);

    void onNoNetwork();

    void showContent();

    <T> CruObserver<T> createListObserver(Action1<T> onNext, Action0 onEmpty);

    <T> CruObserver<T> createListObserver(Action1<T> onNext, Action0 onEmpty, Action0 onNoNetwork);

    <T> CruObserver<T> createListObserver(int emptyLayoutId, Action1<T> onNext);

    <T> CruObserver<T> createListObserver(Action1<T> onNext, Action0 onEmpty, Action0 onNoNetwork, Action0 onNetworkError);

    void inflateEmptyView(View v, int layoutId);
}

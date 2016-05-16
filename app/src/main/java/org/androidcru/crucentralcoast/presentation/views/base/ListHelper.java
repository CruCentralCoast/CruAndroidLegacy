package org.androidcru.crucentralcoast.presentation.views.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.androidcru.crucentralcoast.data.providers.observer.CruObserver;

import rx.functions.Action0;
import rx.functions.Action1;

public interface ListHelper
{
    void onViewCreated(View view, Bundle savedInstanceState);

    View onEmpty(int layoutId);

    void onNoNetwork();

    void showContent();

    <T> CruObserver<T> createListObserver(Context c, Action1<T> onNext, Action0 onEmpty);

    <T> CruObserver<T> createListObserver(Context c, int emptyLayoutId, Action1<T> onNext);

    void inflateEmptyView(View v, int layoutId);
}

package org.androidcru.crucentralcoast.presentation.views.resources;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.androidcru.crucentralcoast.CruApplication;
import org.androidcru.crucentralcoast.R;
import org.androidcru.crucentralcoast.data.models.Resource;
import org.androidcru.crucentralcoast.data.models.ResourceTag;
import org.androidcru.crucentralcoast.data.providers.ResourceProvider;
import org.androidcru.crucentralcoast.presentation.views.base.ListFragment;
import org.androidcru.crucentralcoast.presentation.views.base.SubscriptionsHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.observers.Observers;
import timber.log.Timber;



public class ResourcesFragment extends ListFragment
{
    private CustomTabsIntent.Builder customTabsIntentBuilder;

    private ArrayList<Resource> resources;
    private Observer<List<Resource>> resourceSubscriber;
    private Observer<List<ResourceTag>> resourceTagSubscriber;
    private Subscription subscription;

    //holds all tags
    private ArrayList<ResourceTag> filterTagsList;
    //holds selected options
    private boolean[] selected;

    private AlertDialog dialog;

    public ResourcesFragment()
    {
        resources = new ArrayList<>();
        setupResourceObserver();
        setupResourceTagObserver();
    }

    void setupResourceTagObserver()
    {
        resourceTagSubscriber = new Observer<List<ResourceTag>>()
        {
            @Override
            public void onCompleted()
            {
                Timber.d("resourceTag onCompleted: OAKS");
            }

            @Override
            public void onError(Throwable e)
            {
                Timber.e(e, "Resource Tags failed to retrieve.");
            }

            @Override
            public void onNext(List<ResourceTag> resourceTags)
            {
                Timber.d("resourceTag onNext() called with: " + "resourceTags = [" + resourceTags + "]");
//                filterTags = (ResourceTag[]) new ArrayList<>(resourceTags).toArray();
                filterTagsList = new ArrayList<>(resourceTags);
                selected = new boolean[filterTagsList.size()];
                setHasOptionsMenu(true);
            }

        };
    }

    void setupResourceObserver()
    {
        resourceSubscriber = new Observer<List<Resource>>()
        {
            @Override
            public void onCompleted()
            {
                swipeRefreshLayout.setRefreshing(false);
                Log.d("eeks", "Resource onCompleted: HERE");

                if (resources.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable e)
            {
                Timber.e(e, "Resources failed to retrieve.");
            }

            @Override
            public void onNext(List<Resource> resources)
            {
                setResources(resources);
                Log.d("okes", "Resource onNext: IKES");

            }

        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.list_with_empty_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        inflateEmptyView(R.layout.empty_articles_view);
        //parent class calls ButterKnife for view injection and setups SwipeRefreshLayout
        super.onViewCreated(view, savedInstanceState);

        //Update the list of resources by pulling from the server
        forceUpdate(null);
        ResourceProvider.getResourceTags(ResourcesFragment.this, resourceTagSubscriber);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(() -> forceUpdate(filterTagsList));
    }

    private void forceUpdate(List<ResourceTag> tags)
    {
        swipeRefreshLayout.setRefreshing(true);
        //Start listening for stream data from network call
        this.resources.clear();
        ResourceProvider.findResources(this, resourceSubscriber, Arrays.asList(Resource.ResourceType.values()), tags);
    }

    private void setResources(List<Resource> resources)
    {
        //Adapter for RecyclerView
        ResourcesAdapter resourcesAdapter = new ResourcesAdapter(new ArrayList<>(resources), customTabsIntentBuilder);
        recyclerView.setAdapter(resourcesAdapter);
        this.resources = new ArrayList<>(resources);
    }

    private void displayFilterAlertDialog() {
        String[] tagStrings = getResourceTagStrings(filterTagsList);
        if(dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setMultiChoiceItems(tagStrings, selected,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            selected[which] = isChecked;
                        }
                    });
            builder.setTitle("Filter Tags");
            //alertDialog.
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            forceUpdate(getFilteredTags());
                        }
                    });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            dialog = builder.create();
        }

        dialog.show();
    }

    private List<ResourceTag> getFilteredTags()
    {
        ArrayList<ResourceTag> toReturn = new ArrayList<>();
        for(int i = 0; i < selected.length; i++)
        {
            if(selected[i])
                toReturn.add(filterTagsList.get(i));
        }
        return toReturn;
    }

    private String[] getResourceTagStrings(List<ResourceTag> tags)
    {
        ArrayList<String> strings = new ArrayList<>();
        for (ResourceTag tag : tags)
            strings.add(tag.title);
        return strings.toArray(new String[tags.size()]);
    }

    // Inflate and set the query listener for the search bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.resource_filter, menu);
        MenuItem searchItem = menu.findItem(R.id.filter_by_tag);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                displayFilterAlertDialog();
                return false;
            }
        });
    }
}

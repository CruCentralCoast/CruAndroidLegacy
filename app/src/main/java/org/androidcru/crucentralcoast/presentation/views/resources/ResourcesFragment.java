package org.androidcru.crucentralcoast.presentation.views.resources;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observer;
import timber.log.Timber;



public class ResourcesFragment extends ListFragment
{
    private CustomTabsIntent.Builder customTabsIntentBuilder;

    private ArrayList<Resource> resources;
    private Observer<List<Resource>> resourceSubscriber;
    private Observer<List<ResourceTag>> resourceTagSubscriber;


    //holds all tags
    private ArrayList<ResourceTag> filterTagsList;
    private ArrayList<Resource.ResourceType> filterTypesList;
    //holds selectedTags options
    private boolean[] selectedTags;
    private boolean[] selectedTypes;

    private AlertDialog tagDialog;
    private AlertDialog typeDialog;

    private boolean loadedTags = false;

    public ResourcesFragment()
    {
        resources = new ArrayList<>();
        filterTypesList =  new ArrayList<Resource.ResourceType>(Arrays.asList(Resource.ResourceType.values()));
        selectedTypes = new boolean[filterTypesList.size()];
        Arrays.fill(selectedTypes, true);
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
                if(!CruApplication.isOnline())
                {
                    onNoNetwork();
                }
                Timber.e(e, "Resource Tags failed to retrieve.");
            }

            @Override
            public void onNext(List<ResourceTag> resourceTags)
            {
                filterTagsList = new ArrayList<>(resourceTags);
                selectedTags = new boolean[filterTagsList.size()];
                Arrays.fill(selectedTags, true);
                setHasOptionsMenu(true);
                loadedTags = true;
                //Update the list of resources/tags by pulling from the server
                forceUpdate(filterTypesList, filterTagsList);
            }

        };
    }

    void setupResourceObserver()
    {
        resourceSubscriber = createListObserver(R.layout.empty_articles_view, resources -> setResources(resources));
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
        super.onViewCreated(view, savedInstanceState);

        helper.swipeRefreshLayout.setRefreshing(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        helper.recyclerView.setLayoutManager(layoutManager);

        helper.swipeRefreshLayout.setOnRefreshListener(() -> forceUpdate(getFilteredTypes(), getFilteredTags()));
        ResourceProvider.getResourceTags(ResourcesFragment.this, resourceTagSubscriber);
    }

    private void forceUpdate(List<Resource.ResourceType> types, List<ResourceTag> tags)
    {
        helper.swipeRefreshLayout.setRefreshing(true);
        //Start listening for stream data from network call
        this.resources.clear();
        if(loadedTags)
            ResourceProvider.findResources(this, resourceSubscriber, types, tags);
        else
            ResourceProvider.getResourceTags(ResourcesFragment.this, resourceTagSubscriber);
    }

    private void setResources(List<Resource> resources)
    {
        //Adapter for RecyclerView
        ResourcesAdapter resourcesAdapter = new ResourcesAdapter(new ArrayList<>(resources), customTabsIntentBuilder);
        helper.recyclerView.setAdapter(resourcesAdapter);
        this.resources = new ArrayList<>(resources);
    }

    //Display dialog for filtering resources by tag
    private void displayFilterTagAlertDialog() {
        String[] tagStrings = getResourceTagStrings(filterTagsList);

        if(tagDialog == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setMultiChoiceItems(tagStrings, selectedTags,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            selectedTags[which] = isChecked;
                        }
                    });

            builder.setTitle("Filter Tags");

            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            forceUpdate(getFilteredTypes(), getFilteredTags());
                        }
                    });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            tagDialog = builder.create();
        }

        tagDialog.show();
    }


    //Display dialog for filtering resources by type
    private void displayFilterTypeAlertDialog() {
        if(typeDialog == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setMultiChoiceItems(
                    getResourceTypeStrings(Resource.ResourceType.values()), selectedTypes,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            selectedTypes[which] = isChecked;
                        }
                    });
            builder.setTitle("Filter Types");

            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            forceUpdate(getFilteredTypes(), getFilteredTags());
                        }
                    });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            typeDialog = builder.create();
        }

        typeDialog.show();
    }

    //Todo: make two generic methods out of these four
    //Extract String field from list of ResourceType
    private String[] getResourceTypeStrings(Resource.ResourceType[] types)
    {
        ArrayList<String> strings = new ArrayList<>();
        for (Resource.ResourceType type : types)
            strings.add(type.toString());
        return strings.toArray(new String[types.length]);
    }

    //Generate list of ResourceTags from tags selected in dialog
    private List<ResourceTag> getFilteredTags()
    {
        ArrayList<ResourceTag> toReturn = new ArrayList<>();
        if(selectedTags != null && filterTagsList != null)
        {
            for (int i = 0; i < selectedTags.length; i++)
                if (selectedTags[i])
                    toReturn.add(filterTagsList.get(i));
            return toReturn;
        }
        else
            return new ArrayList<>();

    }

    //Generate list of ResourceTypes from tags selected in dialog
    private List<Resource.ResourceType> getFilteredTypes()
    {
        ArrayList<Resource.ResourceType> toReturn = new ArrayList<>();
        if(selectedTypes != null && filterTypesList != null)
        {
            for (int i = 0; i < selectedTypes.length; i++)
                if (selectedTypes[i])
                    toReturn.add(filterTypesList.get(i));
            return toReturn;
        }
        else
            return new ArrayList<>();
    }

    //Extract String field from list of ResourceTags
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
        //Resource type filter menu item
        inflater.inflate(R.menu.resource_type_filter, menu);
        menu.findItem(R.id.filter_by_type).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                displayFilterTypeAlertDialog();
                return false;
            }
        });

        //Resource tag filter menu item
        inflater.inflate(R.menu.resource_tag_filter, menu);
        menu.findItem(R.id.filter_by_tag).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                displayFilterTagAlertDialog();
                return false;
            }
        });
    }
}

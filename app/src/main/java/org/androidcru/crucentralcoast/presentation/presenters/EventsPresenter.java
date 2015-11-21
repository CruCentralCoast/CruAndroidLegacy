package org.androidcru.crucentralcoast.presentation.presenters;

import org.androidcru.crucentralcoast.data.models.Event;
import org.androidcru.crucentralcoast.presentation.views.views.EventsView;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;

public class EventsPresenter extends MvpBasePresenter<EventsView>
{

    public ArrayList<Event> getEventData()
    {
        ArrayList<Event> list = new ArrayList<>();
        list.add(new Event("Test Event", "garbage data", ZonedDateTime.now(), ZonedDateTime.now().plusHours(3), false));
        list.add(new Event("Test Event 2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi hendrerit nunc justo, in congue ante finibus sed. Sed sed placerat quam. Sed in pretium risus. Nam in diam sit amet nisl blandit tincidunt. Proin non leo vel libero sodales malesuada id a diam. Sed pharetra magna nec mi vestibulum, eget suscipit lacus cursus. Nam faucibus, nisl eu pellentesque tempus, sem sapien mollis enim, eu vulputate odio dui quis mi. Aenean sed consectetur nisi.\n" +
                "\n" +
                "Morbi at dignissim erat. Nulla et gravida nunc. Curabitur tristique ut dui ac placerat. Phasellus tellus eros, tempor a tincidunt nec, tristique et sem. Morbi mattis lacus vel dui accumsan, id sodales magna ultricies. Nulla urna quam, fermentum eu iaculis a, blandit ut dolor. Nulla vitae porttitor lectus. Vestibulum molestie nibh in metus consectetur, vel sagittis libero ultrices. Praesent bibendum iaculis purus, sed pulvinar turpis fermentum ac. Aenean diam orci, elementum ac metus sit amet, mattis sodales massa.\n" +
                "\n" +
                "Duis ac urna lacinia, egestas ex sed, pharetra purus. Duis interdum odio leo, quis gravida leo congue ac. Nulla bibendum hendrerit magna, quis consectetur enim imperdiet vel. Sed ac urna sagittis, feugiat nisi eget, vulputate arcu. Cras ut augue malesuada, fermentum leo sit amet, interdum odio. Sed ut lacinia arcu. Cras felis lacus, maximus sit amet arcu interdum, imperdiet mattis ex. Interdum et malesuada fames ac ante ipsum primis in faucibus.\n" +
                "\n" +
                "Vestibulum massa magna, tincidunt at consectetur sit amet, feugiat a orci. Nam at laoreet velit. Nam dapibus varius risus, laoreet vestibulum lorem posuere quis. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nullam vel est consequat, tristique massa non, fringilla leo. Duis eros arcu, lobortis a fermentum ac, tristique eget nibh. Duis libero mi, molestie sit amet ex nec, facilisis consectetur urna. Ut nec ullamcorper lectus. Cras imperdiet leo et est sagittis, euismod vulputate nunc suscipit. Curabitur egestas odio a magna ultricies, id finibus orci pellentesque. Aliquam euismod erat orci, quis iaculis massa pharetra tempor. Phasellus laoreet at dui id dignissim. Phasellus rhoncus justo vitae leo laoreet, eu auctor diam euismod. Duis eu urna sed leo volutpat elementum non non ipsum. Donec gravida vestibulum nisl sed eleifend.\n" +
                "\n" +
                "Proin vulputate maximus fermentum. Suspendisse non convallis mi. Nunc sodales auctor ultrices. In hac habitasse platea dictumst. In mattis quam non nulla vestibulum cursus. Donec ullamcorper nisi nec mauris vehicula, id convallis lectus posuere. Curabitur vitae elit leo.", ZonedDateTime.now(), ZonedDateTime.now().plusHours(5), false));
        return list;
    }
}

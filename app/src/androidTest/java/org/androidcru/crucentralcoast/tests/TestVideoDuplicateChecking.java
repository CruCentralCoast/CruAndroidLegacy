package org.androidcru.crucentralcoast.tests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import org.androidcru.crucentralcoast.presentation.views.videos.VideosFragment;
import static org.junit.Assert.*;

/**
 * Created by mitch on 4/28/16.
 */
public class TestVideoDuplicateChecking {
    @Test
    public void testAddIfNotDuplicated() {
        List<Integer> intListOne = new ArrayList<>();
        List<Integer> intListTwo = new ArrayList<>();
        int count = 0;

        intListOne.add(1);
        intListOne.add(2);
        intListOne.add(3);

        intListTwo.add(2);
        intListTwo.add(3);
        intListTwo.add(4);

        count = VideosFragment.addIfNotDuplicated(intListOne, intListTwo);
        assertEquals(1, count);
        assertEquals(4, intListOne.size());
    }
}

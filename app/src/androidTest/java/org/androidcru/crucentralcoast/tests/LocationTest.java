package org.androidcru.crucentralcoast.tests;

import org.androidcru.crucentralcoast.data.models.Location;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by main on 5/10/16.
 */
public class LocationTest {

    @Test
    public void testLocationConstructors() {
        double[] geo = {-120.67661229999997, 35.2420425};
        String addr1 = "S Higuera St, San Luis Obispo, CA, USA";
        String addr2 = "200 N Santa Rosa St, San Luis Obispo, CA 93405, USA";

        Location loc1 = new Location(addr1, geo);
        Location loc2 = new Location(addr2, geo);

        assertEquals(null, loc1.postcode);
        assertEquals("CA", loc1.state);
        assertEquals("San Luis Obispo", loc1.suburb);
        assertEquals("S Higuera St", loc1.street1);
        assertEquals("USA", loc1.country);
        assertEquals(loc1.geo[0], geo[0], 0.01);
        assertEquals(loc1.geo[1], geo[1], 0.01);

        assertEquals("93405", loc2.postcode);
        assertEquals("CA", loc2.state);
        assertEquals("San Luis Obispo", loc2.suburb);
        assertEquals("200 N Santa Rosa St", loc2.street1);
        assertEquals("USA", loc2.country);
        assertEquals(loc2.geo[0], geo[0], 0.01);
        assertEquals(loc2.geo[1], geo[1], 0.01);
    }

    @Test
    public void testToString() {
        double[] geo = {-120.67661229999997, 35.2420425};
        String addr1 = "S Higuera St, San Luis Obispo, CA, USA";
        String addr2 = "200 N Santa Rosa St, San Luis Obispo, CA 93405, USA";

        Location loc1 = new Location(addr1, geo);
        Location loc2 = new Location(addr2, geo);

        assertEquals("S Higuera St San Luis Obispo, CA, USA", loc1.toString());
        assertEquals("200 N Santa Rosa St San Luis Obispo, CA, 93405, USA", loc2.toString());
    }
}
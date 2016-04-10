package org.androidcru.crucentralcoast.common;

import java.io.InputStream;
import java.util.Scanner;

public class ResourcesUtil
{
    public static String getResourceAsString(ClassLoader classLoader, String resourceName)
    {
        return convertStreamToString(classLoader.getResourceAsStream(resourceName));
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

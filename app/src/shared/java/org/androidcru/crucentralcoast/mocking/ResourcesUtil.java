package org.androidcru.crucentralcoast.mocking;

import java.io.InputStream;
import java.util.Scanner;

public class ResourcesUtil
{
    public static String getResourceAsString(ClassLoader classLoader, String resourceName)
    {
        InputStream is = classLoader.getResourceAsStream(resourceName);
        return convertStreamToString(is);
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String str = s.hasNext() ? s.next() : "";
        s.close();
        return str;
    }
}

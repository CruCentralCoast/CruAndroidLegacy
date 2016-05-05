package org.androidcru.crucentralcoast.mocking;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ResourcesUtil
{
    public static String getResourceAsString(ClassLoader classLoader, String resourceName) throws IOException
    {
        InputStream is = classLoader.getResourceAsStream(resourceName);
        String str = convertStreamToString(is);
        is.close();
        return str;
    }

    public static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String str = s.hasNext() ? s.next() : "";
        s.close();
        return str;
    }
}

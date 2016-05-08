package org.androidcru.crucentralcoast.data.models.youtube;

import com.google.gson.annotations.SerializedName;

import org.androidcru.crucentralcoast.data.models.Dateable;
import org.androidcru.crucentralcoast.data.models.Image;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.threeten.bp.ZonedDateTime;

import java.util.HashMap;

@Parcel
public class Snippet implements Dateable {

    public static final String sPublishedAt = "publishedAt";
    public static final String sTitle = "title";
    public static final String sDescription = "sDescription";
    public static final String sThumbnails = "thumbnails";
    public static final String sResourceId = "resourceId";

    private static final String DEFAULT = "default";
    private static final String MEDIUM = "medium";
    private static final String HIGH = "high";
    private static final String STANDARD = "standard";
    private static final String MAXRES = "maxres";

    private static final String VIDEOID = "videoId";

    @SerializedName(sPublishedAt) public ZonedDateTime publishedAt;
    @SerializedName(sTitle) public String title;
    @SerializedName(sDescription) public String description;
    @SerializedName(sThumbnails) public HashMap<String, Image> thumbnails;
    @SerializedName(sResourceId) public HashMap<String, String> resourceId;

    @ParcelConstructor
    Snippet() {}

    public Image getDefaultImage()
    {
        return thumbnails.get(DEFAULT);
    }

    public Image getStandardImage()
    {
        return thumbnails.get(STANDARD);
    }

    public Image getHigh()
    {
        return thumbnails.get(HIGH);
    }

    public Image getMediumImage()
    {
        return thumbnails.get(MEDIUM);
    }

    public Image getMaxResImage()
    {
        return thumbnails.get(MAXRES);
    }

    public String getVideoId()
    {
        return resourceId.get(VIDEOID);
    }

    @Override
    public ZonedDateTime getDate()
    {
        return publishedAt;
    }
}

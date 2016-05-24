package org.androidcru.crucentralcoast.presentation.views.subscriptions;

public class Item<H, I>
{
    public H header;
    public I item;

    public Item(H header, I item)
    {
        this.header = header;
        this.item = item;
    }

    public boolean isHeader()
    {
        return header != null;
    }

    public boolean isItem()
    {
        return item != null;
    }
}

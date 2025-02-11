package com.lzyc.ybtappcal.view.pagestate;

import android.view.View;

public abstract class PageListener
{


    public abstract void setRetryEvent(View retryView);

    public void setLoadingEvent(View loadingView)
    {
    }

    public void setEmptyEvent(View emptyView)
    {
    }

    public int generateLoadingLayoutId()
    {
        return PageManager.NO_LAYOUT_ID;
    }

    public int generateRetryLayoutId()
    {
        return PageManager.NO_LAYOUT_ID;
    }

    public int generateEmptyLayoutId()
    {
        return PageManager.NO_LAYOUT_ID;
    }

    public View generateLoadingLayout()
    {
        return null;
    }

    public View generateRetryLayout()
    {
        return null;
    }

    public View generateEmptyLayout()
    {
        return null;
    }

    public boolean isSetLoadingLayout()
    {
        if (generateLoadingLayoutId() != PageManager.NO_LAYOUT_ID || generateLoadingLayout() != null)
            return true;
        return false;
    }

    public boolean isSetRetryLayout()
    {
        if (generateRetryLayoutId() != PageManager.NO_LAYOUT_ID || generateRetryLayout() != null)
            return true;
        return false;
    }

    public boolean isSetEmptyLayout()
    {
        if (generateEmptyLayoutId() != PageManager.NO_LAYOUT_ID || generateEmptyLayout() != null)
            return true;
        return false;
    }


}
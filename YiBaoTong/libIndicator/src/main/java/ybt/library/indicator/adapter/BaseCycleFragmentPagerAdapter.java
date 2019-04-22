package ybt.library.indicator.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseCycleFragmentPagerAdapter<Item> extends FragmentPagerAdapter implements BaseCycleAdapterInterface<Item> {

    private List<Item> mItems = new ArrayList<Item>();

    public BaseCycleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BaseCycleFragmentPagerAdapter(FragmentManager fm, final List<Item> items) {
        super(fm);
        this.mItems = items;
    }

    /**
     * 根据Item和position，返回Fragment
     */
    protected abstract Fragment getItemFragment(final Item item, final int position);

    @Override
    public Fragment getItem(int position) {
        final int itemsSize = mItems.size();
        if (position == 0) {
            return getItemFragment(mItems.get(itemsSize - 1), (itemsSize - 1));
        } else if (position == itemsSize + 1) {
            return getItemFragment(mItems.get(0), 0);
        } else {
            return getItemFragment(mItems.get(position - 1), (position - 1));
        }
    }

    @Override
    public int getCount() {
        final int itemsSize = mItems.size();
        return itemsSize > 1 ? itemsSize + 2 : itemsSize;
    }

    @Override
    public int getRealCount() {
        return mItems.size();
    }

    @Override
    public void setItems(List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public Item getItemObject(int position) {
        return mItems.get(position);
    }
}

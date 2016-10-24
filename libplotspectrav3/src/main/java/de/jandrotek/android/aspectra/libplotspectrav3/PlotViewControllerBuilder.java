package de.jandrotek.android.aspectra.libplotspectrav3;

import java.util.ArrayList;

public class PlotViewControllerBuilder {
    private int param1;
    private ArrayList<String> items = null;
    private int itemsCount = 1; // minimal count if not initialized
    private PlotViewController mController = null;

    public PlotViewControllerBuilder setParam1(int param1) {
        this.param1 = param1;
        return this;
    }

    public PlotViewControllerBuilder setItems(ArrayList<String> items) {
        this.items = items;
        return this;
    }

    public PlotViewControllerBuilder setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
        return this;
    }

    public PlotViewController getInstancePlotViewController() {
        if (mController == null) {
            mController = new PlotViewController(param1, items, itemsCount);
        }
        return mController;
    }

}
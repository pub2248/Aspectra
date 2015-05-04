package de.jandrotek.android.aspectra.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ListContent {

    /**
     * An array of spectra items.
     */

    public static List<SpectrumItem> ITEMS = new ArrayList<SpectrumItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, SpectrumItem> ITEM_MAP = new HashMap<String, SpectrumItem>();

    static {
    	if(SpectrumFiles.mFilelNameListOutput.length > 0){
    	for(int i = 0; i < SpectrumFiles.mFilelNameListOutput.length; i++)
    		addItem(new SpectrumItem("1", SpectrumFiles.mFilelNameListOutput[i], "some notes"));
    	}
    	else
    	{
    		addItem(new SpectrumItem("1","Unknown Path", "some notes"));
    		addItem(new SpectrumItem("2","Unknown Path", "some notes"));
    		addItem(new SpectrumItem("3","Unknown Path", "some notes"));

    	}
    }

    private static void addItem(SpectrumItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class SpectrumItem {
    	public String id;
    	public String name;
    	public String notes;
		private boolean selected;

    	public SpectrumItem(String id, String name, String notes){
    		this.id = id;
    		this.name = name;
    		this.notes = notes;
			selected = false;
    	}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}


		@Override
    	public String toString(){
    		return name;
    	}

    	public String getName(){
    		return name;
    	}

    	public String getNotes(){
    		return notes;
    	}
    }
}
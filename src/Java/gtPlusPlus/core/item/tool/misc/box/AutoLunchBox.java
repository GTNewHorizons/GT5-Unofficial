package gtPlusPlus.core.item.tool.misc.box;

public class AutoLunchBox extends BaseBoxItem {

	public final static int SLOTS = 9;

	public AutoLunchBox(String displayName) {
		super(displayName, new String[] {"Stores 9 pieces of food", "Food will automatically be eaten from slot 1, through to "+gtPlusPlus.core.item.tool.misc.box.AutoLunchBox.SLOTS}, gtPlusPlus.core.item.tool.misc.box.AutoLunchBox.SLOTS);
	}

}

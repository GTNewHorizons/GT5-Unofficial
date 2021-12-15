package gtPlusPlus.core.item.tool.misc.box;

public class MagicToolBag extends BaseBoxItem {

	public final static int SLOTS = 24;

	public MagicToolBag(String displayName) {
		super(displayName, new String[] {"Can store magic tools from TC, BM, etc", "Please ask for additional mod support on Github"}, gtPlusPlus.core.item.tool.misc.box.MagicToolBag.SLOTS);
	}
	
}

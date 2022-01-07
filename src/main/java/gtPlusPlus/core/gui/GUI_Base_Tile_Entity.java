package gtPlusPlus.core.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GUI_Base_Tile_Entity extends GuiContainer {

	public final Container mContainer;
	
	public GUI_Base_Tile_Entity(Container aContainer) {
		super(aContainer);
		mContainer = aContainer;
	}

}

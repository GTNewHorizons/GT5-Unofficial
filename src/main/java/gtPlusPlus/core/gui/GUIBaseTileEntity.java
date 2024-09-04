package gtPlusPlus.core.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public abstract class GUIBaseTileEntity extends GuiContainer {

    public final Container mContainer;

    public GUIBaseTileEntity(Container aContainer) {
        super(aContainer);
        mContainer = aContainer;
    }
}

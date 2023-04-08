package gtPlusPlus.core.gui.item.box;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.util.ResourceLocation;

import gtPlusPlus.core.item.tool.misc.box.ContainerBoxBase;

public class LunchBoxGui extends GuiBaseBox {

    public LunchBoxGui(ContainerBoxBase containerItem) {
        super(containerItem, new ResourceLocation(GTPlusPlus.ID, "textures/gui/schematic_rocket_GS1.png"));
    }
}

package gtPlusPlus.core.item.base.cell;

import net.minecraft.item.ItemStack;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;

public class BaseItemCell extends BaseItemComponent {

    public BaseItemCell(final Material material) {
        super(material, BaseItemComponent.ComponentTypes.CELL);
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (renderPass == 1) {
            return Utils.rgbtoHexValue(255, 255, 255);
        }
        return this.componentColour;
    }
}

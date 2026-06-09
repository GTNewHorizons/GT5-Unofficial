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
        if (this.componentMaterial == null) {
            if (extraData != null) {
                return Utils.rgbtoHexValue(extraData[0], extraData[1], extraData[2]);
            }
            return this.componentColour;
        }

        if (this.componentMaterial.getRGBA()[3] <= 1) {
            return this.componentColour;
        }
        // See BaseItemComponent.getColorFromItemStack: animated materials ship baked textures, rendered untinted.
        return Utils.rgbtoHexValue(255, 255, 255);
    }
}

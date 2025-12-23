package gtPlusPlus.core.item.base.cell;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;

public class BaseItemCell extends BaseItemComponent {

    public BaseItemCell(final Material material) {
        super(material, BaseItemComponent.ComponentTypes.CELL);
    }

    @Override
    public void registerIcons(final IIconRegister i) {
        this.base = i.registerIcon(GregTech.ID + ":" + "materialicons/METALLIC/" + "cell");
        this.overlay = i.registerIcon(GregTech.ID + ":" + "materialicons/METALLIC/" + "cell_OVERLAY");
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (renderPass == 1) {
            return Utils.rgbtoHexValue(255, 255, 255);
        }
        return this.componentColour;
    }
}

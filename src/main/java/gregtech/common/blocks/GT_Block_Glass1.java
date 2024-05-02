package gregtech.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;

/**
 * The glass is split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class GT_Block_Glass1 extends GT_Block_Casings_Abstract {

    public GT_Block_Glass1() {
        super(GT_Item_Casings9.class, "gt.blockglass1", Material.glass, 2);

    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (1 << 7) | (aMeta + 64);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}

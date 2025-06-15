package gregtech.common.blocks;

import gregtech.api.enums.ItemList;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings13 extends BlockCasingsAbstract {

    public BlockCasings13() {
        super(ItemCasings.class, "gt.blockcasings13", MaterialCasings.INSTANCE, 16);

        register(15, ItemList.Casing_Solidifier_Atomic, "Atomic Solidifier Casing");
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 96);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 15 -> Textures.BlockIcons.CASING_SOLIDIFIER_ATOMIC.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}

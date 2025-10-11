package gregtech.common.blocks;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings14 extends BlockCasingsAbstract {

    public BlockCasings14() {
        super(ItemCasings.class, "gt.blockcasings14", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.CokeOvenCasing, "Coke Oven Bricks");
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "pickaxe";
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 112);
    }

    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.COKE_OVEN_CASING.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }
}

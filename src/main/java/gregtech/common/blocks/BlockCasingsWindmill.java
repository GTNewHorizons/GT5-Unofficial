package gregtech.common.blocks;

import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

public class BlockCasingsWindmill extends BlockCasingsAbstract {

    public BlockCasingsWindmill() {
        super(ItemCasings.class, "gt.blockcasingswindmill", MaterialCasings.INSTANCE, 6);
        register(0, ItemList.WindmillBaseCasing);
        register(1, ItemList.WindmillShaftCasing);
        register(2, ItemList.WindmillBaseCasingDirty);
        register(3, ItemList.WindmillShaftCasingDirty);
        register(4, ItemList.WindmillMillstoneHousing);
        register(5, ItemList.WindmillMixerHousing);
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.WINDMILL_BASE_CASING.getIcon();
            case 1 -> Textures.BlockIcons.WINDMILL_SHAFT_CASING.getIcon();
            case 2 -> Textures.BlockIcons.WINDMILL_BASE_CASING_DIRTY.getIcon();
            case 3 -> Textures.BlockIcons.WINDMILL_SHAFT_CASING_DIRTY.getIcon();
            case 4 -> Textures.BlockIcons.WINDMILL_MILLSTONE_HOUSING.getIcon();
            default -> Textures.BlockIcons.WINDMILL_MIXER_HOUSING.getIcon();
        };
    }
}

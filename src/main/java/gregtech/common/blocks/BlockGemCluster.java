package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.common.misc.GTStructureChannels;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockGemCluster extends BlockCasingsAbstract {

    public BlockGemCluster() {
        super(ItemCasings.class, "gt.blockgemcluster", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.GemClusterLV);
        register(1, ItemList.GemClusterMV);
        register(2, ItemList.GemClusterHV);
        register(3, ItemList.GemClusterEV);
        register(4, ItemList.GemClusterIV);
        register(5, ItemList.GemClusterLuV);
        register(6, ItemList.GemClusterZPM);
        register(7, ItemList.GemClusterUV);
        register(8, ItemList.GemClusterUHV);
        register(9, ItemList.GemClusterUEV);
        register(10, ItemList.GemClusterUIV);
        register(11, ItemList.GemClusterUMV);
        register(12, ItemList.GemClusterUXV);
        register(13, ItemList.GemClusterMAX);

        for (int i = 0; i < 14; i++) {
            GTStructureChannels.TIER_GEM_CLUSTER.registerAsIndicator(new ItemStack(this, 1, i), i + 1);
        }
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 1 -> Textures.BlockIcons.GEM_CLUSTER_MV.getIcon();
            case 2 -> Textures.BlockIcons.GEM_CLUSTER_HV.getIcon();
            case 3 -> Textures.BlockIcons.GEM_CLUSTER_EV.getIcon();
            case 4 -> Textures.BlockIcons.GEM_CLUSTER_IV.getIcon();
            case 5 -> Textures.BlockIcons.GEM_CLUSTER_LuV.getIcon();
            case 6 -> Textures.BlockIcons.GEM_CLUSTER_ZPM.getIcon();
            case 7 -> Textures.BlockIcons.GEM_CLUSTER_UV.getIcon();
            case 8 -> Textures.BlockIcons.GEM_CLUSTER_UHV.getIcon();
            case 9 -> Textures.BlockIcons.GEM_CLUSTER_UEV.getIcon();
            case 10 -> Textures.BlockIcons.GEM_CLUSTER_UIV.getIcon();
            case 11 -> Textures.BlockIcons.GEM_CLUSTER_UMV.getIcon();
            case 12 -> Textures.BlockIcons.GEM_CLUSTER_UXV.getIcon();
            case 13 -> Textures.BlockIcons.GEM_CLUSTER_MAX.getIcon();
            default -> Textures.BlockIcons.GEM_CLUSTER_LV.getIcon();
        };
    }
}

package gregtech.common.blocks;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The base class for casings. Casings are the blocks that are mainly used to build multiblocks.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasingsNH extends BlockCasingsAbstract {

    public BlockCasingsNH() {
        super(ItemCasings.class, "gt.blockcasingsNH", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.Casing_AirFilter_Turbine_T1);
        register(1, ItemList.Casing_AirFilter_Vent_T1);
        register(2, ItemList.Casing_Pyrolyse);
        register(3, ItemList.Casing_AirFilter_Turbine_T2);
        register(4, ItemList.Casing_AirFilter_Vent_T2);
        register(5, ItemList.Casing_AirFilter_Turbine_T3);
        register(6, ItemList.Casing_AirFilter_Vent_T3);

        register(10, ItemList.Casing_UEV);
        register(11, ItemList.Casing_UIV);
        register(12, ItemList.Casing_UMV);
        register(13, ItemList.Casing_UXV);
        register(14, ItemList.Casing_MAXV);
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (8 << 7) | (aMeta + 64);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STEEL.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_PIPE_STEEL.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_ULV_SIDE.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_PIPE_TITANIUM.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_PIPE_TUNGSTENSTEEL.getIcon();
            default -> {
                if (aSide == 0) {
                    yield Textures.BlockIcons.MACHINECASINGS_BOTTOM[aMeta].getIcon();
                }
                if (aSide == 1) {
                    yield Textures.BlockIcons.MACHINECASINGS_TOP[aMeta].getIcon();
                }
                yield Textures.BlockIcons.MACHINECASINGS_SIDE[aMeta].getIcon();
            }
        };
    }

    @Override
    public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ) <= 9 ? super.colorMultiplier(aWorld, aX, aY, aZ)
            : Dyes.MACHINE_METAL.toInt();
    }
}

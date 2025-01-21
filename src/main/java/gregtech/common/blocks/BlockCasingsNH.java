package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GTLanguageManager;

public class BlockCasingsNH extends BlockCasingsAbstract {

    public BlockCasingsNH() {
        super(ItemCasingsNH.class, "gt.blockcasingsNH", MaterialCasings.INSTANCE, 16);

        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Air Filter Turbine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Air Filter Vent Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Pyrolyse Oven Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Advanced Air Filter Turbine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Advanced Air Filter Vent Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Super Air Filter Turbine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Super Air Filter Vent Casing");

        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "UEV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "UIV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "UMV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "UXV Machine Casing");
        GTLanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "MAX Machine Casing");

        ItemList.Casing_AirFilter_Turbine_T1.set(new ItemStack(this, 1, 0));
        ItemList.Casing_AirFilter_Vent_T1.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Pyrolyse.set(new ItemStack(this, 1, 2));
        ItemList.Casing_AirFilter_Turbine_T2.set(new ItemStack(this, 1, 3));
        ItemList.Casing_AirFilter_Vent_T2.set(new ItemStack(this, 1, 4));
        ItemList.Casing_AirFilter_Turbine_T3.set(new ItemStack(this, 1, 5));
        ItemList.Casing_AirFilter_Vent_T3.set(new ItemStack(this, 1, 6));

        ItemList.Casing_UEV.set(new ItemStack(this, 1, 10));
        ItemList.Casing_UIV.set(new ItemStack(this, 1, 11));
        ItemList.Casing_UMV.set(new ItemStack(this, 1, 12));
        ItemList.Casing_UXV.set(new ItemStack(this, 1, 13));
        ItemList.Casing_MAXV.set(new ItemStack(this, 1, 14));
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
            case 2 -> Textures.BlockIcons.MACHINE_8V_SIDE.getIcon();
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
            : Dyes.MACHINE_METAL.mRGBa[0] << 16 | Dyes.MACHINE_METAL.mRGBa[1] << 8 | Dyes.MACHINE_METAL.mRGBa[2];
    }
}

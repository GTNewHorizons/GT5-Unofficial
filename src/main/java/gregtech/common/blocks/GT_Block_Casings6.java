package gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;

public class GT_Block_Casings6 extends GT_Block_Casings_Abstract {

    public GT_Block_Casings6() {
        super(GT_Item_Casings6.class, "gt.blockcasings6", GT_Material_Casings.INSTANCE, 16);

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Hermetic Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Hermetic Casing I");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Hermetic Casing II");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Hermetic Casing III");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "Hermetic Casing IV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Hermetic Casing V");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Hermetic Casing VI");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Hermetic Casing VII");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Hermetic Casing VIII");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Hermetic Casing IX");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Hermetic Casing X");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Hermetic Casing XI");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "Hermetic Casing XII");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "Hermetic Casing XIII");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Hermetic Casing XIV");

        ItemList.Casing_Tank_0.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Tank_1.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Tank_2.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Tank_3.set(new ItemStack(this, 1, 3));
        ItemList.Casing_Tank_4.set(new ItemStack(this, 1, 4));
        ItemList.Casing_Tank_5.set(new ItemStack(this, 1, 5));
        ItemList.Casing_Tank_6.set(new ItemStack(this, 1, 6));
        ItemList.Casing_Tank_7.set(new ItemStack(this, 1, 7));
        ItemList.Casing_Tank_8.set(new ItemStack(this, 1, 8));
        ItemList.Casing_Tank_9.set(new ItemStack(this, 1, 9));
        ItemList.Casing_Tank_10.set(new ItemStack(this, 1, 10));
        ItemList.Casing_Tank_11.set(new ItemStack(this, 1, 11));
        ItemList.Casing_Tank_12.set(new ItemStack(this, 1, 12));
        ItemList.Casing_Tank_13.set(new ItemStack(this, 1, 13));
        ItemList.Casing_Tank_14.set(new ItemStack(this, 1, 14));
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (8 << 7) | (aMeta + 112);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        if (aSide == 0) {
            return Textures.BlockIcons.MACHINECASINGS_BOTTOM[aMeta].getIcon();
        }
        if (aSide == 1) {
            return Textures.BlockIcons.MACHINECASINGS_TOP[aMeta].getIcon();
        }
        return switch (aMeta) {
            case 1 -> Textures.BlockIcons.MACHINE_CASING_TANK_1.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_TANK_2.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_TANK_3.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_TANK_4.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_TANK_5.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_TANK_6.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_TANK_7.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_TANK_8.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_TANK_9.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_TANK_10.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_TANK_11.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_TANK_12.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_CASING_TANK_13.getIcon();
            case 14 -> Textures.BlockIcons.MACHINE_CASING_TANK_14.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_TANK_0.getIcon();
        };
    }

    @Override
    public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[0] << 16
            | gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[1] << 8
            | gregtech.api.enums.Dyes.MACHINE_METAL.mRGBa[2];
    }
}

package tectech.thing.casing;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Block_Casings_Abstract;
import gregtech.common.blocks.GT_Material_Casings;
import tectech.TecTech;
import tectech.thing.CustomItemList;

/**
 * Created by danie_000 on 03.10.2016.
 */
public class BlockGTCasingsNH extends GT_Block_Casings_Abstract {

    public static boolean mConnectedMachineTextures = true;

    public BlockGTCasingsNH() {
        super(ItemCasingsNH.class, "gt.blockcasingsNH", GT_Material_Casings.INSTANCE);
        setCreativeTab(TecTech.creativeTabTecTech);

        for (byte b = 0; b < 16; b = (byte) (b + 1)) {
            Textures.BlockIcons.casingTexturePages[8][b + 64] = new GT_CopiedBlockTexture(this, 6, b);
            /* IMPORTANT for block recoloring */
        }

        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "UEV Machine Casing"); // adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "UIV Machine Casing"); // adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "UMV Machine Casing"); // adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "UXV Machine Casing"); // adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "MAX Machine Casing"); // adding

        CustomItemList.Casing_UEV.set(new ItemStack(this, 1, 10));
        CustomItemList.Casing_UIV.set(new ItemStack(this, 1, 11));
        CustomItemList.Casing_UMV.set(new ItemStack(this, 1, 12));
        CustomItemList.Casing_UXV.set(new ItemStack(this, 1, 13));
        CustomItemList.Casing_MAXV.set(new ItemStack(this, 1, 14));
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        // super.registerBlockIcons(aIconRegister);
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        if (ordinalSide == 0) {
            return tectech.thing.metaTileEntity.Textures.MACHINECASINGS_BOTTOM_TT[aMeta].getIcon();
        }
        if (ordinalSide == 1) {
            return tectech.thing.metaTileEntity.Textures.MACHINECASINGS_TOP_TT[aMeta].getIcon();
        }
        return tectech.thing.metaTileEntity.Textures.MACHINECASINGS_SIDE_TT[aMeta].getIcon();
    }
}

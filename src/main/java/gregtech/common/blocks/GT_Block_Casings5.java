package gregtech.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class GT_Block_Casings5
        extends GT_Block_Casings_Abstract {
    public GT_Block_Casings5() {
        super(GT_Item_Casings5.class, "gt.blockcasings5", GT_Material_Casings.INSTANCE);
        for (int i = 0; i < 16; i++) {
            Textures.BlockIcons.casingTexturePages[1][i] = new GT_CopiedBlockTexture(this, 6, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Cupronickel Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Kanthal Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Nichrome Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Tungstensteel Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "HSS-G Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Naquadah Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Naquadah Alloy Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Electrum Flux Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Awakened Draconium Coil Block");

        ItemList.Casing_Coil_Cupronickel.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Coil_Kanthal.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Coil_Nichrome.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Coil_TungstenSteel.set(new ItemStack(this, 1, 3));
        ItemList.Casing_Coil_HSSG.set(new ItemStack(this, 1, 4));
        ItemList.Casing_Coil_Naquadah.set(new ItemStack(this, 1, 5));
        ItemList.Casing_Coil_NaquadahAlloy.set(new ItemStack(this, 1, 6));
        ItemList.Casing_Coil_ElectrumFlux.set(new ItemStack(this, 1, 7));
        ItemList.Casing_Coil_AwakenedDraconium.set(new ItemStack(this, 1, 8));
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta) {
        switch (aMeta) {
            case 0:
                return Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
            case 1:
                return Textures.BlockIcons.MACHINE_COIL_KANTHAL.getIcon();
            case 2:
                return Textures.BlockIcons.MACHINE_COIL_NICHROME.getIcon();
            case 3:
                return Textures.BlockIcons.MACHINE_COIL_TUNGSTENSTEEL.getIcon();
            case 4:
                return Textures.BlockIcons.MACHINE_COIL_HSSG.getIcon();
            case 5:
                return Textures.BlockIcons.MACHINE_COIL_NAQUADAH.getIcon();
            case 6:
                return Textures.BlockIcons.MACHINE_COIL_NAQUADAHALLOY.getIcon();
            case 7:
                return Textures.BlockIcons.MACHINE_COIL_ELECTRUMFLUX.getIcon();
            case 8:
                return Textures.BlockIcons.MACHINE_COIL_AWAKENEDDRACONIUM.getIcon();
        }
        return Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
    }
}

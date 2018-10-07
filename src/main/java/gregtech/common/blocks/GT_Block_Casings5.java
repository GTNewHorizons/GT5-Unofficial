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
        GT_Utility.addTexturePage((byte) 1);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            Textures.BlockIcons.casingTexturePages[1][i] = new GT_CopiedBlockTexture(this, 6, i);
        }
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".0.name", "Cupronickel Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".1.name", "Kanthal Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".2.name", "Nichrome Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".3.name", "Tungstensteel Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".4.name", "HSS-G Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".5.name", "Naquadah Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".6.name", "Naquadah Alloy Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".7.name", "Magnetic Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".8.name", "Intermix Chamber Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".9.name", "Fusion Machine Casing MK III");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "Fusion Machine Casing MK IV");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "Superconductor Fusion Coil Block");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "Dyson Ring Casing");
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "Core Chamber Casing");
        

        ItemList.Casing_Coil_Cupronickel.set(new ItemStack(this, 1, 0));
        ItemList.Casing_Coil_Kanthal.set(new ItemStack(this, 1, 1));
        ItemList.Casing_Coil_Nichrome.set(new ItemStack(this, 1, 2));
        ItemList.Casing_Coil_TungstenSteel.set(new ItemStack(this, 1, 3));
        ItemList.Casing_Coil_HSSG.set(new ItemStack(this, 1, 4));
        ItemList.Casing_Coil_Naquadah.set(new ItemStack(this, 1, 5));
        ItemList.Casing_Coil_NaquadahAlloy.set(new ItemStack(this, 1, 6));
        ItemList.Block_Magnetic_Coil.set(new ItemStack(this, 1, 7));
        ItemList.Casing_Internix_Chamber.set(new ItemStack(this, 1, 8));
        ItemList.Casing_Fusion3.set(new ItemStack(this, 1, 9));
        ItemList.Casing_Fusion4.set(new ItemStack(this, 1, 10));
        ItemList.Casing_Fusion_Coil2.set(new ItemStack(this, 1, 11));
        ItemList.Casing_Dyson_Ring.set(new ItemStack(this, 1, 14));
        ItemList.Casing_Core_Chamber.set(new ItemStack(this, 1, 15));
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
                return Textures.BlockIcons.MACHINE_COIL_MAGNETIC.getIcon();
            case 8:
            	return Textures.BlockIcons.MACHINE_INTERMIX_CHAMBER.getIcon();
            case 9:
            	return Textures.BlockIcons.MACHINE_CASING_FUSION_3.getIcon();
            case 10:
            	return Textures.BlockIcons.MACHINE_CASING_FUSION_4.getIcon();
            case 11:
                return Textures.BlockIcons.MACHINE_CASING_FUSION_COIL2.getIcon();
            case 14:
            	return Textures.BlockIcons.MACHINE_CASING_DYSON_RING.getIcon();
            case 15:
            	return Textures.BlockIcons.MACHINE_CASING_CORE_CHAMBER.getIcon();
        }
        return Textures.BlockIcons.MACHINE_COIL_CUPRONICKEL.getIcon();
    }
}


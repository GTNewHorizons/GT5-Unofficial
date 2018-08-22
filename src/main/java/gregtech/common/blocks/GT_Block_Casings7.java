package gregtech.common.blocks;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class GT_Block_Casings7
        extends GT_Block_Casings_Abstract {
	
	/**
	 * Texture Index Information
	 * Textures.BlockIcons.casingTexturePages[0][0-63] - Gregtech
	 * Textures.BlockIcons.casingTexturePages[0][64-127] - GT++
	 * Textures.BlockIcons.casingTexturePages[1][0-127] - Gregtech
	 * Textures.BlockIcons.casingTexturePages[2][0-127] - Free
	 * Textures.BlockIcons.casingTexturePages[3][0-127] - Free
	 * Textures.BlockIcons.casingTexturePages[4][0-127] - Free
	 * Textures.BlockIcons.casingTexturePages[5][0-127] - Free
	 * Textures.BlockIcons.casingTexturePages[6][0-127] - Free
	 * Textures.BlockIcons.casingTexturePages[7][0-127] - Free
	 * Textures.BlockIcons.casingTexturePages[8][0-127] - TecTech	  
	 */
	
	
    public GT_Block_Casings7() {
        super(GT_Item_Casings7.class, "gt.blockcasings7", GT_Material_Casings.INSTANCE);
    	for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            Textures.BlockIcons.casingTexturePages[1][i + 64] = new GT_CopiedBlockTexture(this, 6, i);
        }

    	GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".10.name", "UEV Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".11.name", "UIV Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".12.name", "UMV Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".13.name", "UXV Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".14.name", "OPV Machine Casing");//adding
        GT_LanguageManager.addStringLocalization(getUnlocalizedName() + ".15.name", "MAX Machine Casing");//adding
        
        ItemList.Casing_UEV.set(new ItemStack(this,1,10));
        ItemList.Casing_UIV.set(new ItemStack(this,1,11));
        ItemList.Casing_UMV.set(new ItemStack(this,1,12));
        ItemList.Casing_UXV.set(new ItemStack(this,1,13));
        ItemList.Casing_OPV.set(new ItemStack(this,1,14));
        ItemList.Casing_MAXV.set(new ItemStack(this,1,15));
    }

    public IIcon getIcon(int aSide, int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            switch (aMeta) {
                
            }
            if (aSide == 0) {
                return Textures.BlockIcons.MACHINECASINGS_BOTTOM[aMeta].getIcon();
            }
            if (aSide == 1) {
                return Textures.BlockIcons.MACHINECASINGS_TOP[aMeta].getIcon();
            }
            return Textures.BlockIcons.MACHINECASINGS_SIDE[aMeta].getIcon();
        }
        return Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
    }

    public int colorMultiplier(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return aWorld.getBlockMetadata(aX, aY, aZ) <= 9 ? super.colorMultiplier(aWorld, aX, aY, aZ) : Dyes.MACHINE_METAL.mRGBa[0] << 16 | Dyes.MACHINE_METAL.mRGBa[1] << 8 | Dyes.MACHINE_METAL.mRGBa[2];
   }
}